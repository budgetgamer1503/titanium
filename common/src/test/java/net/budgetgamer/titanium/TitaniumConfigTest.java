package net.budgetgamer.titanium;

import net.budgetgamer.titanium.config.ConfigManager;
import net.budgetgamer.titanium.config.TitaniumConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class TitaniumConfigTest {

    @Test
    public void testPresets() {
        TitaniumConfig config = new TitaniumConfig();

        config.applyPreset(TitaniumConfig.Preset.POTATO_PC);
        assertEquals(32.0, config.maxEntityDistance);
        assertEquals(TitaniumConfig.ParticleMode.AGGRESSIVE_THROTTLE, config.particleMode);
        assertEquals(0.4, config.minDistanceScale);

        config.applyPreset(TitaniumConfig.Preset.MAX_FPS);
        assertEquals(48.0, config.maxEntityDistance);
        assertEquals(TitaniumConfig.ParticleMode.CULL_OFFSCREEN, config.particleMode);

        config.applyPreset(TitaniumConfig.Preset.BALANCED);
        assertEquals(64.0, config.maxEntityDistance);
    }

    @Test
    public void testConfigSaveAndLoad(@TempDir File tempDir) {
        ConfigManager.init(tempDir);

        TitaniumConfig config = ConfigManager.getConfig();
        config.targetFps = 144;
        config.maxEntityDistance = 55.0;
        ConfigManager.setConfig(config);

        ConfigManager.init(tempDir);
        TitaniumConfig reloaded = ConfigManager.getConfig();

        assertEquals(144, reloaded.targetFps);
        assertEquals(55.0, reloaded.maxEntityDistance);
    }
}
