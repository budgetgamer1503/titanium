package net.budgetgamer.sodiumimprover;

import net.budgetgamer.sodiumimprover.config.ConfigManager;
import net.budgetgamer.sodiumimprover.config.ImproverConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class ImproverConfigTest {

    @Test
    public void testPresets() {
        ImproverConfig config = new ImproverConfig();

        config.applyPreset(ImproverConfig.Preset.POTATO_PC);
        assertEquals(32.0, config.maxEntityDistance);
        assertEquals(ImproverConfig.ParticleMode.AGGRESSIVE_THROTTLE, config.particleMode);
        assertEquals(0.4, config.minDistanceScale);

        config.applyPreset(ImproverConfig.Preset.MAX_FPS);
        assertEquals(48.0, config.maxEntityDistance);
        assertEquals(ImproverConfig.ParticleMode.CULL_OFFSCREEN, config.particleMode);

        config.applyPreset(ImproverConfig.Preset.BALANCED);
        assertEquals(64.0, config.maxEntityDistance);
    }

    @Test
    public void testConfigSaveAndLoad(@TempDir File tempDir) {
        ConfigManager.init(tempDir);

        ImproverConfig config = ConfigManager.getConfig();
        config.targetFps = 144;
        config.maxEntityDistance = 55.0;
        ConfigManager.setConfig(config);

        ConfigManager.init(tempDir);
        ImproverConfig reloaded = ConfigManager.getConfig();

        assertEquals(144, reloaded.targetFps);
        assertEquals(55.0, reloaded.maxEntityDistance);
    }
}
