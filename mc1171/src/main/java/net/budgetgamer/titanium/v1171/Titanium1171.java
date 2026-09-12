package net.budgetgamer.titanium.v1171;

import net.budgetgamer.titanium.config.ConfigManager;
import net.budgetgamer.titanium.core.CompatibilityManager;
import net.budgetgamer.titanium.v1171.rso.RSOOptionRegister;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class Titanium1171 implements ClientModInitializer {

    public static final String MOD_ID = "titanium";

    @Override
    public void onInitializeClient() {
        ConfigManager.init(FabricLoader.getInstance().getConfigDir().toFile());
        CompatibilityManager.init();
        RSOOptionRegister.register();
    }
}
