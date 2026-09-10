package net.budgetgamer.sodiumimprover.v1211.rso;

import net.budgetgamer.sodiumimprover.config.ConfigManager;
import net.budgetgamer.sodiumimprover.config.ImproverConfig;
import net.fabricmc.loader.api.FabricLoader;

public final class RSOOptionRegister {

    private static boolean rsoLoaded = false;
    private static boolean sodiumLoaded = false;

    private RSOOptionRegister() {}

    public static void register() {
        sodiumLoaded = FabricLoader.getInstance().isModLoaded("sodium");
        rsoLoaded = FabricLoader.getInstance().isModLoaded("reeses-sodium-options");

        if (!sodiumLoaded) {
            return;
        }

        tryRegisterSodiumPage();
    }

    private static void tryRegisterSodiumPage() {
        try {
            Class<?> optionPageClass = null;
            try {
                optionPageClass = Class.forName("net.caffeinemc.mods.sodium.client.gui.options.OptionPage");
            } catch (ClassNotFoundException e) {
                optionPageClass = Class.forName("me.jellysquid.mods.sodium.client.gui.options.OptionPage");
            }

            if (optionPageClass != null) {
                ImproverConfig cfg = ConfigManager.getConfig();
                if (cfg != null) {
                    ConfigManager.save();
                }
            }
        } catch (Throwable ignored) {
        }
    }

    public static boolean isRsoLoaded() {
        return rsoLoaded;
    }

    public static boolean isSodiumLoaded() {
        return sodiumLoaded;
    }
}
