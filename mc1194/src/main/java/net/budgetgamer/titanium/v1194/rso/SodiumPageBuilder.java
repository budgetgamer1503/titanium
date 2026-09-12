package net.budgetgamer.titanium.v1194.rso;

import net.budgetgamer.titanium.config.ConfigManager;
import net.budgetgamer.titanium.config.TitaniumConfig;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public final class SodiumPageBuilder {

    private SodiumPageBuilder() {}

    @SuppressWarnings("unchecked")
    public static void injectTitaniumPage(Object gui) {
        if (gui == null) {
            return;
        }

        try {
            List<Object> pagesList = null;

            pagesList = findPagesList(gui.getClass(), gui);

            if (pagesList == null) {
                Class<?> current = gui.getClass().getSuperclass();
                while (current != null && current != Object.class && pagesList == null) {
                    pagesList = findPagesList(current, gui);
                    current = current.getSuperclass();
                }
            }

            if (pagesList == null) {
                return;
            }

            for (Object page : pagesList) {
                if (page != null) {
                    String str = page.toString();
                    if (str != null && str.contains("Titanium")) {
                        return;
                    }
                    try {
                        Method getName = page.getClass().getMethod("getName");
                        Object nameObj = getName.invoke(page);
                        if (nameObj != null && nameObj.toString().contains("Titanium")) {
                            return;
                        }
                    } catch (Throwable ignored) {}
                }
            }

            Class<?> optionPageClass = null;
            if (!pagesList.isEmpty() && pagesList.get(0) != null) {
                optionPageClass = pagesList.get(0).getClass();
            } else {
                for (String candidate : new String[]{
                    "net.caffeinemc.mods.sodium.client.gui.options.OptionPage",
                    "me.jellysquid.mods.sodium.client.gui.options.OptionPage"
                }) {
                    try {
                        optionPageClass = Class.forName(candidate);
                        break;
                    } catch (ClassNotFoundException ignored) {}
                }
            }

            if (optionPageClass == null) {
                return;
            }

            String sodiumPkg = optionPageClass.getPackage().getName();

            Constructor<?> pageConstructor = null;
            for (Constructor<?> c : optionPageClass.getConstructors()) {
                if (c.getParameterCount() == 2 && List.class.isAssignableFrom(c.getParameterTypes()[1])) {
                    pageConstructor = c;
                    break;
                }
            }
            if (pageConstructor == null && optionPageClass.getConstructors().length > 0) {
                pageConstructor = optionPageClass.getConstructors()[0];
            }
            if (pageConstructor == null || pageConstructor.getParameterTypes().length < 2) {
                return;
            }

            Class<?> textClass = pageConstructor.getParameterTypes()[0];
            Class<?> listParamType = pageConstructor.getParameterTypes()[1];

            Object pageName = createText(textClass, "Titanium");
            if (pageName == null) {
                return;
            }

            Class<?> optionGroupClass = Class.forName(sodiumPkg + ".OptionGroup");
            Method createGroupBuilderMethod = optionGroupClass.getMethod("createBuilder");
            Object groupBuilder = createGroupBuilderMethod.invoke(null);

            Class<?> optionImplClass = Class.forName(sodiumPkg + ".OptionImpl");
            Class<?> optionStorageClass = Class.forName(sodiumPkg + ".storage.OptionStorage");
            Class<?> optionClass = Class.forName(sodiumPkg + ".Option");
            Class<?> tickBoxControlClass = Class.forName(sodiumPkg + ".control.TickBoxControl");

            Object storageProxy = Proxy.newProxyInstance(
                optionStorageClass.getClassLoader(),
                new Class<?>[]{optionStorageClass},
                (proxy, method, args) -> {
                    String name = method.getName();
                    if ("getData".equals(name)) {
                        return ConfigManager.getConfig();
                    } else if ("save".equals(name)) {
                        ConfigManager.save();
                        return null;
                    }
                    return null;
                }
            );

            Method createOptionBuilderMethod = optionImplClass.getMethod("createBuilder", Class.class, optionStorageClass);
            Method addOptionMethod = groupBuilder.getClass().getMethod("add", optionClass);

            createToggle(
                createOptionBuilderMethod, addOptionMethod, groupBuilder, storageProxy,
                textClass, sodiumPkg, tickBoxControlClass,
                "Entity Culling", "Culls mobs, animals, and items outside camera view for higher FPS",
                cfg -> cfg.entityCullingEnabled,
                (cfg, val) -> { cfg.entityCullingEnabled = val; ConfigManager.save(); }
            );

            createToggle(
                createOptionBuilderMethod, addOptionMethod, groupBuilder, storageProxy,
                textClass, sodiumPkg, tickBoxControlClass,
                "Tile Entity Culling", "Culls chests, hoppers, banners, and signs to stop storage room lag",
                cfg -> cfg.tileEntityCullingEnabled,
                (cfg, val) -> { cfg.tileEntityCullingEnabled = val; ConfigManager.save(); }
            );

            createToggle(
                createOptionBuilderMethod, addOptionMethod, groupBuilder, storageProxy,
                textClass, sodiumPkg, tickBoxControlClass,
                "Dynamic Frame Pacer", "Dynamically tunes render load during FPS drops to eliminate stutter",
                cfg -> cfg.dynamicFramePacerEnabled,
                (cfg, val) -> { cfg.dynamicFramePacerEnabled = val; ConfigManager.save(); }
            );

            createToggle(
                createOptionBuilderMethod, addOptionMethod, groupBuilder, storageProxy,
                textClass, sodiumPkg, tickBoxControlClass,
                "Cull Monster Mobs", "Skips rendering zombies, skeletons, and other hostile mobs behind walls",
                cfg -> cfg.cullMonsterMobs,
                (cfg, val) -> { cfg.cullMonsterMobs = val; ConfigManager.save(); }
            );

            createToggle(
                createOptionBuilderMethod, addOptionMethod, groupBuilder, storageProxy,
                textClass, sodiumPkg, tickBoxControlClass,
                "Cull Item Drops", "Skips rendering floating dropped items outside camera frustum",
                cfg -> cfg.cullItemDrops,
                (cfg, val) -> { cfg.cullItemDrops = val; ConfigManager.save(); }
            );

            createToggle(
                createOptionBuilderMethod, addOptionMethod, groupBuilder, storageProxy,
                textClass, sodiumPkg, tickBoxControlClass,
                "Cull Item Frames & Stands", "Skips rendering item frames and armor stands outside field of view",
                cfg -> cfg.cullItemFrames,
                (cfg, val) -> { cfg.cullItemFrames = val; cfg.cullArmorStands = val; ConfigManager.save(); }
            );

            Method buildGroupMethod = groupBuilder.getClass().getMethod("build");
            Object builtGroup = buildGroupMethod.invoke(groupBuilder);

            Object groupsList;
            if (listParamType.getName().contains("ImmutableList")) {
                Class<?> immutableListClass = Class.forName("com.google.common.collect.ImmutableList");
                Method ofMethod = immutableListClass.getMethod("of", Object.class);
                groupsList = ofMethod.invoke(null, builtGroup);
            } else {
                groupsList = Collections.singletonList(builtGroup);
            }

            Object improverPage = pageConstructor.newInstance(pageName, groupsList);
            pagesList.add(improverPage);
        } catch (Throwable t) {
            System.err.println("[Titanium] Failed to inject Sodium options page: " + t);
        }
    }

    @SuppressWarnings("unchecked")
    private static List<Object> findPagesList(Class<?> clazz, Object gui) {
        try {
            Field pagesField = clazz.getDeclaredField("pages");
            if (List.class.isAssignableFrom(pagesField.getType())) {
                pagesField.setAccessible(true);
                Object val = pagesField.get(gui);
                if (val instanceof List) {
                    return (List<Object>) val;
                }
            }
        } catch (Throwable ignored) {}

        for (Field f : clazz.getDeclaredFields()) {
            if (List.class.isAssignableFrom(f.getType())) {
                try {
                    f.setAccessible(true);
                    Object val = f.get(gui);
                    if (val instanceof List) {
                        List<?> list = (List<?>) val;
                        if (!list.isEmpty() && list.get(0) != null &&
                            list.get(0).getClass().getSimpleName().contains("OptionPage")) {
                            return (List<Object>) val;
                        }
                    }
                } catch (Throwable ignored) {}
            }
        }
        return null;
    }

    public static Object createText(Class<?> textClass, String text) {
        if (textClass == null) {
            return null;
        }

        try {
            Method m = textClass.getMethod("literal", String.class);
            return m.invoke(null, text);
        } catch (Throwable ignored) {}

        try {
            Method m = textClass.getMethod("method_43470", String.class);
            return m.invoke(null, text);
        } catch (Throwable ignored) {}

        try {
            Method m = textClass.getMethod("of", String.class);
            return m.invoke(null, text);
        } catch (Throwable ignored) {}

        for (String clsName : new String[]{"net.minecraft.text.LiteralText", "net.minecraft.class_2585"}) {
            try {
                Class<?> lt = Class.forName(clsName);
                Constructor<?> cons = lt.getConstructor(String.class);
                return cons.newInstance(text);
            } catch (Throwable ignored) {}
        }

        try {
            Method m = textClass.getMethod("method_43471", String.class);
            return m.invoke(null, text);
        } catch (Throwable ignored) {}
        try {
            Method m = textClass.getMethod("translatable", String.class);
            return m.invoke(null, text);
        } catch (Throwable ignored) {}

        try {
            Method m = textClass.getMethod("method_30163", String.class);
            return m.invoke(null, text);
        } catch (Throwable ignored) {}
        try {
            Method m = textClass.getMethod("nullToEmpty", String.class);
            return m.invoke(null, text);
        } catch (Throwable ignored) {}

        for (Method m : textClass.getMethods()) {
            if (Modifier.isStatic(m.getModifiers()) &&
                m.getParameterCount() == 1 &&
                m.getParameterTypes()[0].equals(String.class) &&
                textClass.isAssignableFrom(m.getReturnType())) {
                try {
                    return m.invoke(null, text);
                } catch (Throwable ignored) {}
            }
        }
        return null;
    }

    private static void createToggle(
            Method createOptionBuilderMethod, Method addOptionMethod, Object groupBuilder, Object storageProxy,
            Class<?> textClass, String sodiumPkg, Class<?> tickBoxControlClass,
            String name, String tooltip,
            Function<TitaniumConfig, Boolean> getter,
            BiConsumer<TitaniumConfig, Boolean> setter
    ) {
        try {
            Object builder = createOptionBuilderMethod.invoke(null, Boolean.class, storageProxy);

            Method setName = builder.getClass().getMethod("setName", textClass);
            setName.invoke(builder, createText(textClass, name));

            Method setTooltip = builder.getClass().getMethod("setTooltip", textClass);
            setTooltip.invoke(builder, createText(textClass, tooltip));

            boolean bindingSet = false;
            try {
                Method directBinding = builder.getClass().getMethod("setBinding", BiConsumer.class, Function.class);
                directBinding.invoke(builder, setter, getter);
                bindingSet = true;
            } catch (Throwable ignored) {}

            if (!bindingSet) {
                try {
                    Class<?> genericBindingClass = Class.forName(sodiumPkg + ".binding.GenericBinding");
                    Class<?> optionBindingClass = Class.forName(sodiumPkg + ".binding.OptionBinding");
                    Constructor<?> bindingConstructor = genericBindingClass.getConstructor(BiConsumer.class, Function.class);
                    Object binding = bindingConstructor.newInstance(setter, getter);
                    Method setBinding = builder.getClass().getMethod("setBinding", optionBindingClass);
                    setBinding.invoke(builder, binding);
                } catch (Throwable ignored) {}
            }

            Constructor<?> tickBoxConstructor = tickBoxControlClass.getConstructors()[0];
            Function<Object, Object> controlFn = (opt) -> {
                try {
                    return tickBoxConstructor.newInstance(opt);
                } catch (Exception e) {
                    return null;
                }
            };
            Method setControl = builder.getClass().getMethod("setControl", Function.class);
            setControl.invoke(builder, controlFn);

            Method buildOption = builder.getClass().getMethod("build");
            Object option = buildOption.invoke(builder);

            addOptionMethod.invoke(groupBuilder, option);
        } catch (Throwable t) {
            System.err.println("[Titanium] Failed to create toggle " + name + ": " + t);
        }
    }
}
