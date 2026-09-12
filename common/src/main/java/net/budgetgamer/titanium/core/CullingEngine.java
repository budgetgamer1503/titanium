package net.budgetgamer.titanium.core;

import net.budgetgamer.titanium.config.ConfigManager;
import net.budgetgamer.titanium.config.TitaniumConfig;

public final class CullingEngine {

    public enum BlockEntityType {
        CHEST,
        HOPPER_SHULKER,
        SIGN_BANNER,
        BEACON_BELL,
        OTHER
    }

    private static final FrustumEngine FRUSTUM = new FrustumEngine();

    private static int culledEntitiesThisFrame = 0;
    private static int renderedEntitiesThisFrame = 0;
    private static int culledBlockEntitiesThisFrame = 0;
    private static int renderedBlockEntitiesThisFrame = 0;

    private CullingEngine() {}

    public static FrustumEngine getFrustum() {
        return FRUSTUM;
    }

    public static void onFrameStart() {
        FramePacer.onFrameStart();
        culledEntitiesThisFrame = 0;
        renderedEntitiesThisFrame = 0;
        culledBlockEntitiesThisFrame = 0;
        renderedBlockEntitiesThisFrame = 0;
    }

    public static boolean shouldCullEntity(
            double minX, double minY, double minZ,
            double maxX, double maxY, double maxZ,
            boolean isMonster, boolean isPassiveAnimal,
            boolean isItemDrop, boolean isItemFrame, boolean isArmorStand
    ) {
        TitaniumConfig config = ConfigManager.getConfig();
        if (!config.entityCullingEnabled) {
            renderedEntitiesThisFrame++;
            return false;
        }

        if (isMonster && !config.cullMonsterMobs) return false;
        if (isPassiveAnimal && !config.cullPassiveAnimals) return false;
        if (isItemDrop && !config.cullItemDrops) return false;
        if (isItemFrame && !config.cullItemFrames) return false;
        if (isArmorStand && !config.cullArmorStands) return false;

        double centerX = (minX + maxX) * 0.5;
        double centerY = (minY + maxY) * 0.5;
        double centerZ = (minZ + maxZ) * 0.5;

        double effectiveMaxDistance = config.maxEntityDistance * FramePacer.getDynamicScale();
        double distSq = FRUSTUM.distanceSqToCamera(centerX, centerY, centerZ);
        if (distSq > (effectiveMaxDistance * effectiveMaxDistance)) {
            culledEntitiesThisFrame++;
            return true;
        }

        double pad = 0.25;
        if (!FRUSTUM.isBoxVisible(minX - pad, minY - pad, minZ - pad, maxX + pad, maxY + pad, maxZ + pad)) {
            culledEntitiesThisFrame++;
            return true;
        }

        renderedEntitiesThisFrame++;
        return false;
    }

    public static boolean isBackfaceCulled(double x, double y, double z, double normalX, double normalY, double normalZ) {
        double camX = FRUSTUM.getCameraX();
        double camY = FRUSTUM.getCameraY();
        double camZ = FRUSTUM.getCameraZ();

        double toCamX = camX - x;
        double toCamY = camY - y;
        double toCamZ = camZ - z;

        double dot = toCamX * normalX + toCamY * normalY + toCamZ * normalZ;
        return dot < 0.0;
    }

    public static boolean shouldCullBlockEntity(
            double minX, double minY, double minZ,
            double maxX, double maxY, double maxZ,
            BlockEntityType type
    ) {
        TitaniumConfig config = ConfigManager.getConfig();
        if (!config.tileEntityCullingEnabled) {
            renderedBlockEntitiesThisFrame++;
            return false;
        }

        if (CompatibilityManager.shouldSkipBlockEntityCulling(type)) {
            renderedBlockEntitiesThisFrame++;
            return false;
        }

        switch (type) {
            case CHEST:
                if (!config.cullChests) return false;
                break;
            case HOPPER_SHULKER:
                if (!config.cullHoppersAndShulkers) return false;
                break;
            case SIGN_BANNER:
                if (!config.cullSignsAndBanners) return false;
                break;
            case BEACON_BELL:
                if (!config.cullBeaconsAndBells) return false;
                break;
            case OTHER:
            default:
                break;
        }

        double centerX = (minX + maxX) * 0.5;
        double centerY = (minY + maxY) * 0.5;
        double centerZ = (minZ + maxZ) * 0.5;

        double effectiveMaxDistance = config.maxTileEntityDistance * FramePacer.getDynamicScale();
        double distSq = FRUSTUM.distanceSqToCamera(centerX, centerY, centerZ);
        if (distSq > (effectiveMaxDistance * effectiveMaxDistance)) {
            culledBlockEntitiesThisFrame++;
            return true;
        }

        if (!FRUSTUM.isBoxVisible(minX, minY, minZ, maxX, maxY, maxZ)) {
            culledBlockEntitiesThisFrame++;
            return true;
        }

        renderedBlockEntitiesThisFrame++;
        return false;
    }

    public static boolean shouldCullParticle(double x, double y, double z) {
        TitaniumConfig config = ConfigManager.getConfig();
        if (config.particleMode == TitaniumConfig.ParticleMode.ALL) {
            return false;
        }

        double maxDist = config.maxParticleDistance * FramePacer.getDynamicScale();
        if (FRUSTUM.distanceSqToCamera(x, y, z) > (maxDist * maxDist)) {
            return true;
        }

        if (config.particleMode == TitaniumConfig.ParticleMode.CULL_OFFSCREEN ||
            config.particleMode == TitaniumConfig.ParticleMode.AGGRESSIVE_THROTTLE) {
            return !FRUSTUM.isSphereVisible(x, y, z, 0.5);
        }

        return false;
    }

    public static int getCulledEntitiesThisFrame() {
        return culledEntitiesThisFrame;
    }

    public static int getRenderedEntitiesThisFrame() {
        return renderedEntitiesThisFrame;
    }

    public static int getCulledBlockEntitiesThisFrame() {
        return culledBlockEntitiesThisFrame;
    }

    public static int getRenderedBlockEntitiesThisFrame() {
        return renderedBlockEntitiesThisFrame;
    }
}
