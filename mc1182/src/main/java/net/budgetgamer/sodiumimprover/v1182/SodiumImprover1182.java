package net.budgetgamer.sodiumimprover.v1182;

import net.budgetgamer.sodiumimprover.config.ConfigManager;
import net.budgetgamer.sodiumimprover.core.CompatibilityManager;
import net.budgetgamer.sodiumimprover.v1182.rso.RSOOptionRegister;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class SodiumImprover1182 implements ClientModInitializer {

    public static final String MOD_ID = "sodiumimprover";

    @Override
    public void onInitializeClient() {
        ConfigManager.init(FabricLoader.getInstance().getConfigDir().toFile());
        CompatibilityManager.init();
        RSOOptionRegister.register();
    }
}
