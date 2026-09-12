package net.budgetgamer.sodiumimprover.v1206.rso;

import net.budgetgamer.sodiumimprover.config.ConfigManager;
import net.budgetgamer.sodiumimprover.config.ImproverConfig;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public final class SodiumPageBuilder {

    private SodiumPageBuilder() {}

    @SuppressWarnings("unchecked")
    public static void injectImproverPage(Object gui) {
        if (gui == null) {
            return;
        }

        try {
            List<Object> pagesList = null;
            Field[] fields = gui.getClass().getDeclaredFields();
            for (Field f : fields) {
                if (List.class.isAssignableFrom(f.getType())) {
                    f.setAccessible(true);
                    Object val = f.get(gui);
                    if (val instanceof List) {
                        List<?> list = (List<?>) val;
                        if (!list.isEmpty() && list.get(0).getClass().getSimpleName().equals("OptionPage")) {
                            pagesList = (List<Object>) val;
                            break;
                        }
                    }
                }
            }

            if (pagesList == null) {
                Class<?> superClass = gui.getClass().getSuperclass();
                if (superClass != null) {
                    for (Field f : superClass.getDeclaredFields()) {
                        if (List.class.isAssignableFrom(f.getType())) {
                            f.setAccessible(true);
                            Object val = f.get(gui);
                            if (val instanceof List) {
                                List<?> list = (List<?>) val;
                                if (!list.isEmpty() && list.get(0).getClass().getSimpleName().equals("OptionPage")) {
                                    pagesList = (List<Object>) val;
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            if (pagesList == null || pagesList.isEmpty()) {
                return;
            }

            for (Object page : pagesList) {
                if (page.toString().contains("Sodium Improver")) {
                    return;
                }
            }

            Object firstPage = pagesList.get(0);
            Class<?> optionPageClass = firstPage.getClass();
            String sodiumPkg = optionPageClass.getPackage().getName();

            MappingResolver resolver = FabricLoader.getInstance().getMappingResolver();
            String textClassName = resolver.mapClassName("intermediary", "net.minecraft.class_2561");
            Class<?> textClass = Class.forName(textClassName);

            Method textFactory = null;
            for (Method m : textClass.getMethods()) {
                if (Modifier.isStatic(m.getModifiers()) &&
                    m.getParameterCount() == 1 &&
                    m.getParameterTypes()[0].equals(String.class) &&
                    textClass.isAssignableFrom(m.getReturnType())) {
                    textFactory = m;
                    if ("literal".equals(m.getName()) || "of".equals(m.getName())) {
                        break;
                    }
                }
            }

            if (textFactory == null) {
                return;
            }

            Object pageName = textFactory.invoke(null, "Sodium Improver");

            Class<?> optionGroupClass = Class.forName(sodiumPkg + ".OptionGroup");
            Method createGroupBuilderMethod = optionGroupClass.getMethod("createBuilder");
            Object groupBuilder = createGroupBuilderMethod.invoke(null);

            Class<?> optionImplClass = Class.forName(sodiumPkg + ".OptionImpl");
            Class<?> optionStorageClass = Class.forName(sodiumPkg + ".storage.OptionStorage");
            Class<?> optionClass = Class.forName(sodiumPkg + ".Option");
            Class<?> optionBindingClass = Class.forName(sodiumPkg + ".binding.OptionBinding");
            Class<?> genericBindingClass = Class.forName(sodiumPkg + ".binding.GenericBinding");
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
                textClass, textFactory, optionBindingClass, genericBindingClass, tickBoxControlClass,
                "Entity Culling", "Culls mobs, animals, and items outside camera view for higher FPS",
                cfg -> cfg.entityCullingEnabled,
                (cfg, val) -> { cfg.entityCullingEnabled = val; ConfigManager.save(); }
            );

            createToggle(
                createOptionBuilderMethod, addOptionMethod, groupBuilder, storageProxy,
                textClass, textFactory, optionBindingClass, genericBindingClass, tickBoxControlClass,
                "Tile Entity Culling", "Culls chests, hoppers, banners, and signs to stop storage room lag",
                cfg -> cfg.tileEntityCullingEnabled,
                (cfg, val) -> { cfg.tileEntityCullingEnabled = val; ConfigManager.save(); }
            );

            createToggle(
                createOptionBuilderMethod, addOptionMethod, groupBuilder, storageProxy,
                textClass, textFactory, optionBindingClass, genericBindingClass, tickBoxControlClass,
                "Dynamic Frame Pacer", "Dynamically tunes render load during FPS drops to eliminate stutter",
                cfg -> cfg.dynamicFramePacerEnabled,
                (cfg, val) -> { cfg.dynamicFramePacerEnabled = val; ConfigManager.save(); }
            );

            createToggle(
                createOptionBuilderMethod, addOptionMethod, groupBuilder, storageProxy,
                textClass, textFactory, optionBindingClass, genericBindingClass, tickBoxControlClass,
                "Cull Monster Mobs", "Skips rendering zombies, skeletons, and other hostile mobs behind walls",
                cfg -> cfg.cullMonsterMobs,
                (cfg, val) -> { cfg.cullMonsterMobs = val; ConfigManager.save(); }
            );

            createToggle(
                createOptionBuilderMethod, addOptionMethod, groupBuilder, storageProxy,
                textClass, textFactory, optionBindingClass, genericBindingClass, tickBoxControlClass,
                "Cull Item Drops", "Skips rendering floating dropped items outside camera frustum",
                cfg -> cfg.cullItemDrops,
                (cfg, val) -> { cfg.cullItemDrops = val; ConfigManager.save(); }
            );

            createToggle(
                createOptionBuilderMethod, addOptionMethod, groupBuilder, storageProxy,
                textClass, textFactory, optionBindingClass, genericBindingClass, tickBoxControlClass,
                "Cull Item Frames & Stands", "Skips rendering item frames and armor stands outside field of view",
                cfg -> cfg.cullItemFrames,
                (cfg, val) -> { cfg.cullItemFrames = val; cfg.cullArmorStands = val; ConfigManager.save(); }
            );

            Method buildGroupMethod = groupBuilder.getClass().getMethod("build");
            Object builtGroup = buildGroupMethod.invoke(groupBuilder);

            Class<?> immutableListClass = Class.forName("com.google.common.collect.ImmutableList");
            Method ofMethod = immutableListClass.getMethod("of", Object.class);
            Object groupsList = ofMethod.invoke(null, builtGroup);

            Constructor<?> pageConstructor = optionPageClass.getConstructor(textClass, immutableListClass);
            Object improverPage = pageConstructor.newInstance(pageName, groupsList);

            pagesList.add(improverPage);
        } catch (Throwable ignored) {
        }
    }

    private static void createToggle(
            Method createOptionBuilderMethod, Method addOptionMethod, Object groupBuilder, Object storageProxy,
            Class<?> textClass, Method textFactory, Class<?> optionBindingClass, Class<?> genericBindingClass,
            Class<?> tickBoxControlClass,
            String name, String tooltip,
            Function<ImproverConfig, Boolean> getter,
            BiConsumer<ImproverConfig, Boolean> setter
    ) {
        try {
            Object builder = createOptionBuilderMethod.invoke(null, Boolean.class, storageProxy);

            Method setName = builder.getClass().getMethod("setName", textClass);
            setName.invoke(builder, textFactory.invoke(null, name));

            Method setTooltip = builder.getClass().getMethod("setTooltip", textClass);
            setTooltip.invoke(builder, textFactory.invoke(null, tooltip));

            Constructor<?> bindingConstructor = genericBindingClass.getConstructor(BiConsumer.class, Function.class);
            Object binding = bindingConstructor.newInstance(setter, getter);
            Method setBinding = builder.getClass().getMethod("setBinding", optionBindingClass);
            setBinding.invoke(builder, binding);

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
        } catch (Throwable ignored) {
        }
    }
}
