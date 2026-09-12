package net.budgetgamer.titanium.v262.rso;

import net.budgetgamer.titanium.config.ConfigManager;
import net.budgetgamer.titanium.config.TitaniumConfig;
import net.caffeinemc.mods.sodium.api.config.ConfigEntryPoint;
import net.caffeinemc.mods.sodium.api.config.structure.BooleanOptionBuilder;
import net.caffeinemc.mods.sodium.api.config.structure.ConfigBuilder;
import net.caffeinemc.mods.sodium.api.config.structure.ModOptionsBuilder;
import net.caffeinemc.mods.sodium.api.config.structure.OptionGroupBuilder;
import net.caffeinemc.mods.sodium.api.config.structure.OptionPageBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.lang.reflect.Method;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class SodiumConfigEntryPoint implements ConfigEntryPoint {

    private static final String MOD_ID = "titanium";

    @Override
    public void registerConfigLate(ConfigBuilder builder) {
        try {
            OptionPageBuilder page = builder.createOptionPage();
            Component pageName = createComponent("Titanium");
            if (pageName != null) {
                page.setName(pageName);
            }

            OptionGroupBuilder group = builder.createOptionGroup();
            Component groupName = createComponent("General Optimizations");
            if (groupName != null) {
                group.setName(groupName);
            }

            addToggle(builder, group, "entity_culling", "Entity Culling",
                    "Culls mobs, animals, and items outside camera view for higher FPS",
                    cfg -> cfg.entityCullingEnabled,
                    (cfg, val) -> cfg.entityCullingEnabled = val,
                    true);

            addToggle(builder, group, "tile_entity_culling", "Tile Entity Culling",
                    "Culls chests, hoppers, banners, and signs to stop storage room lag",
                    cfg -> cfg.tileEntityCullingEnabled,
                    (cfg, val) -> cfg.tileEntityCullingEnabled = val,
                    true);

            addToggle(builder, group, "dynamic_frame_pacer", "Dynamic Frame Pacer",
                    "Dynamically tunes render load during FPS drops to eliminate stutter",
                    cfg -> cfg.dynamicFramePacerEnabled,
                    (cfg, val) -> cfg.dynamicFramePacerEnabled = val,
                    true);

            addToggle(builder, group, "cull_monster_mobs", "Cull Monster Mobs",
                    "Skips rendering zombies, skeletons, and other hostile mobs behind walls",
                    cfg -> cfg.cullMonsterMobs,
                    (cfg, val) -> cfg.cullMonsterMobs = val,
                    true);

            addToggle(builder, group, "cull_item_drops", "Cull Item Drops",
                    "Skips rendering floating dropped items outside camera frustum",
                    cfg -> cfg.cullItemDrops,
                    (cfg, val) -> cfg.cullItemDrops = val,
                    true);

            addToggle(builder, group, "cull_item_frames", "Cull Item Frames & Stands",
                    "Skips rendering item frames and armor stands outside field of view",
                    cfg -> cfg.cullItemFrames,
                    (cfg, val) -> {
                        cfg.cullItemFrames = val;
                        cfg.cullArmorStands = val;
                    },
                    true);

            page.addOptionGroup(group);

            ModOptionsBuilder modOptions = builder.registerModOptions(MOD_ID);
            try {
                modOptions.setNonTintedIcon(Identifier.fromNamespaceAndPath(MOD_ID, "icon.png"));
            } catch (Throwable ignored) {
                try {
                    modOptions.setIcon(Identifier.fromNamespaceAndPath(MOD_ID, "icon.png"));
                } catch (Throwable ignored2) {}
            }

            try {
                modOptions.addPage(page);
            } catch (Throwable t2) {
                try {
                    for (Method m : modOptions.getClass().getMethods()) {
                        if (m.getName().equals("addPage") && m.getParameterCount() == 1) {
                            m.invoke(modOptions, page);
                            break;
                        }
                    }
                } catch (Throwable ignored3) {}
            }
        } catch (Throwable t) {
            System.err.println("[Titanium] Failed to register Sodium config: " + t);
        }
    }

    private void addToggle(
            ConfigBuilder builder,
            OptionGroupBuilder group,
            String id,
            String name,
            String tooltip,
            Function<TitaniumConfig, Boolean> getter,
            BiConsumer<TitaniumConfig, Boolean> setter,
            boolean defaultValue
    ) {
        try {
            BooleanOptionBuilder opt = builder.createBooleanOption(Identifier.fromNamespaceAndPath(MOD_ID, id));
            Component compName = createComponent(name);
            if (compName != null) {
                opt.setName(compName);
            }
            Component compTooltip = createComponent(tooltip);
            if (compTooltip != null) {
                opt.setTooltip(compTooltip);
            }
            opt.setDefaultValue(defaultValue);
            opt.setStorageHandler(ConfigManager::save);
            opt.setBinding(
                    val -> {
                        setter.accept(ConfigManager.getConfig(), val);
                        ConfigManager.save();
                    },
                    () -> getter.apply(ConfigManager.getConfig())
            );
            group.addOption(opt);
        } catch (Throwable t) {
            System.err.println("[Titanium] Failed to create toggle " + name + ": " + t);
        }
    }

    private static Component createComponent(String text) {
        if (text == null) {
            return null;
        }
        try {
            Method m = Component.class.getMethod("literal", String.class);
            return (Component) m.invoke(null, text);
        } catch (Throwable ignored) {}
        try {
            Method m = Component.class.getMethod("nullToEmpty", String.class);
            return (Component) m.invoke(null, text);
        } catch (Throwable ignored) {}
        try {
            Method m = Component.class.getMethod("translatable", String.class);
            return (Component) m.invoke(null, text);
        } catch (Throwable ignored) {}
        return null;
    }
}
