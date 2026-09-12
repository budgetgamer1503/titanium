package net.budgetgamer.titanium.core;

import java.util.logging.Logger;

public final class CompatibilityManager {

    private static final Logger LOGGER = Logger.getLogger("Titanium-Compat");

    private static boolean initialized = false;

    private static boolean sodiumLoaded = false;
    private static boolean entityCullingLoaded = false;
    private static boolean moreCullingLoaded = false;
    private static boolean immediatelyFastLoaded = false;
    private static boolean ebeLoaded = false;
    private static boolean rsoLoaded = false;
    private static boolean sodiumExtraLoaded = false;
    private static boolean ferriteCoreLoaded = false;

    private CompatibilityManager() {}

    public static synchronized void init() {
        if (initialized) {
            return;
        }
        initialized = true;

        sodiumLoaded = checkModLoaded("sodium");
        entityCullingLoaded = checkModLoaded("entityculling");
        moreCullingLoaded = checkModLoaded("moreculling");
        immediatelyFastLoaded = checkModLoaded("immediatelyfast");
        ebeLoaded = checkModLoaded("enhancedblockentities");
        rsoLoaded = checkModLoaded("reeses-sodium-options");
        sodiumExtraLoaded = checkModLoaded("sodium-extra");
        ferriteCoreLoaded = checkModLoaded("ferritecore");

        LOGGER.info("[Titanium] Compatibility layer initialized.");
        LOGGER.info("[Titanium] Sodium detected: " + sodiumLoaded);

        if (entityCullingLoaded) {
            LOGGER.info("[Titanium] Synergy active: EntityCulling detected. Sodium Improver handles fast frustum & dynamic distance culling to minimize background raytrace overhead.");
        }
        if (moreCullingLoaded) {
            LOGGER.info("[Titanium] Synergy active: MoreCulling detected. Coordinated block entity and sign culling enabled.");
        }
        if (ebeLoaded) {
            LOGGER.info("[Titanium] Synergy active: Enhanced Block Entities detected. Chunk-baked block entities will bypass redundant dispatcher checks.");
        }
        if (immediatelyFastLoaded) {
            LOGGER.info("[Titanium] Synergy active: ImmediatelyFast detected. Immediate mode rendering optimized alongside Sodium Improver.");
        }
        if (rsoLoaded) {
            LOGGER.info("[Titanium] Reese's Sodium Options detected. Dedicated vertical tab injected.");
        }
    }

    private static boolean checkModLoaded(String modId) {
        try {
            Class<?> loaderClass = Class.forName("net.fabricmc.loader.api.FabricLoader");
            Object loader = loaderClass.getMethod("getInstance").invoke(null);
            Object result = loaderClass.getMethod("isModLoaded", String.class).invoke(loader, modId);
            return Boolean.TRUE.equals(result);
        } catch (Throwable ignored) {
            return false;
        }
    }

    public static boolean isSodiumLoaded() {
        return sodiumLoaded;
    }

    public static boolean isEntityCullingLoaded() {
        return entityCullingLoaded;
    }

    public static boolean isMoreCullingLoaded() {
        return moreCullingLoaded;
    }

    public static boolean isImmediatelyFastLoaded() {
        return immediatelyFastLoaded;
    }

    public static boolean isEBELoaded() {
        return ebeLoaded;
    }

    public static boolean isRSOLoaded() {
        return rsoLoaded;
    }

    public static boolean isSodiumExtraLoaded() {
        return sodiumExtraLoaded;
    }

    public static boolean isFerriteCoreLoaded() {
        return ferriteCoreLoaded;
    }

    public static boolean shouldSkipBlockEntityCulling(CullingEngine.BlockEntityType type) {
        if (ebeLoaded) {
            if (type == CullingEngine.BlockEntityType.CHEST || type == CullingEngine.BlockEntityType.SIGN_BANNER) {
                return true;
            }
        }
        return false;
    }
}
