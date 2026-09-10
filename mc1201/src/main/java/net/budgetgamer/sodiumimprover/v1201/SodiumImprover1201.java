package net.budgetgamer.sodiumimprover.v1201;

import net.budgetgamer.sodiumimprover.config.ConfigManager;
import net.budgetgamer.sodiumimprover.v1201.rso.RSOOptionRegister;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class SodiumImprover1201 implements ClientModInitializer {

    public static final String MOD_ID = "sodiumimprover";

    @Override
    public void onInitializeClient() {
        ConfigManager.init(FabricLoader.getInstance().getConfigDir().toFile());
        RSOOptionRegister.register();
    }
}
