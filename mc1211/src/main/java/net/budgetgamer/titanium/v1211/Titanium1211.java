package net.budgetgamer.titanium.v1211;

import net.budgetgamer.titanium.config.ConfigManager;
import net.budgetgamer.titanium.v1211.rso.RSOOptionRegister;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class Titanium1211 implements ClientModInitializer {

    public static final String MOD_ID = "titanium";

    @Override
    public void onInitializeClient() {
        ConfigManager.init(FabricLoader.getInstance().getConfigDir().toFile());
        net.budgetgamer.titanium.core.CompatibilityManager.init();
        RSOOptionRegister.register();
    }
}
