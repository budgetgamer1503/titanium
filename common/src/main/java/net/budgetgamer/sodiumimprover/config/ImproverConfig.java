package net.budgetgamer.sodiumimprover.config;

public class ImproverConfig {

    public enum Preset {
        POTATO_PC,
        MAX_FPS,
        BALANCED,
        CUSTOM
    }

    public enum ParticleMode {
        ALL,
        CULL_OFFSCREEN,
        AGGRESSIVE_THROTTLE
    }

    public Preset activePreset = Preset.MAX_FPS;

    public boolean entityCullingEnabled = true;
    public double maxEntityDistance = 64.0;
    public boolean cullMonsterMobs = true;
    public boolean cullPassiveAnimals = true;
    public boolean cullItemDrops = true;
    public boolean cullItemFrames = true;
    public boolean cullArmorStands = true;

    public boolean tileEntityCullingEnabled = true;
    public double maxTileEntityDistance = 64.0;
    public boolean cullChests = true;
    public boolean cullHoppersAndShulkers = true;
    public boolean cullSignsAndBanners = true;
    public boolean cullBeaconsAndBells = true;

    public boolean dynamicFramePacerEnabled = true;
    public int targetFps = 60;
    public boolean dynamicDistanceScaling = true;
    public double minDistanceScale = 0.5;

    public ParticleMode particleMode = ParticleMode.CULL_OFFSCREEN;
    public double maxParticleDistance = 32.0;

    public void applyPreset(Preset preset) {
        this.activePreset = preset;
        switch (preset) {
            case POTATO_PC:
                this.entityCullingEnabled = true;
                this.maxEntityDistance = 32.0;
                this.cullItemDrops = true;
                this.cullItemFrames = true;
                this.cullArmorStands = true;
                this.tileEntityCullingEnabled = true;
                this.maxTileEntityDistance = 32.0;
                this.cullChests = true;
                this.cullHoppersAndShulkers = true;
                this.cullSignsAndBanners = true;
                this.dynamicFramePacerEnabled = true;
                this.targetFps = 60;
                this.dynamicDistanceScaling = true;
                this.minDistanceScale = 0.4;
                this.particleMode = ParticleMode.AGGRESSIVE_THROTTLE;
                this.maxParticleDistance = 20.0;
                break;

            case MAX_FPS:
                this.entityCullingEnabled = true;
                this.maxEntityDistance = 48.0;
                this.cullItemDrops = true;
                this.cullItemFrames = true;
                this.cullArmorStands = true;
                this.tileEntityCullingEnabled = true;
                this.maxTileEntityDistance = 48.0;
                this.cullChests = true;
                this.cullHoppersAndShulkers = true;
                this.cullSignsAndBanners = true;
                this.dynamicFramePacerEnabled = true;
                this.targetFps = 60;
                this.dynamicDistanceScaling = true;
                this.minDistanceScale = 0.6;
                this.particleMode = ParticleMode.CULL_OFFSCREEN;
                this.maxParticleDistance = 32.0;
                break;

            case BALANCED:
                this.entityCullingEnabled = true;
                this.maxEntityDistance = 64.0;
                this.cullItemDrops = true;
                this.cullItemFrames = false;
                this.cullArmorStands = false;
                this.tileEntityCullingEnabled = true;
                this.maxTileEntityDistance = 64.0;
                this.cullChests = true;
                this.cullHoppersAndShulkers = true;
                this.cullSignsAndBanners = false;
                this.dynamicFramePacerEnabled = true;
                this.targetFps = 60;
                this.dynamicDistanceScaling = false;
                this.minDistanceScale = 0.8;
                this.particleMode = ParticleMode.CULL_OFFSCREEN;
                this.maxParticleDistance = 48.0;
                break;

            case CUSTOM:
                break;
        }
    }
}
