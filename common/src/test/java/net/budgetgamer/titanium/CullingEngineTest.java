package net.budgetgamer.titanium;

import net.budgetgamer.titanium.config.ConfigManager;
import net.budgetgamer.titanium.config.TitaniumConfig;
import net.budgetgamer.titanium.core.CullingEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CullingEngineTest {

    @BeforeEach
    public void setUp() {
        TitaniumConfig config = new TitaniumConfig();
        config.applyPreset(TitaniumConfig.Preset.MAX_FPS);
        ConfigManager.setConfig(config);

        CullingEngine.onFrameStart();
        CullingEngine.getFrustum().setCameraPosition(0.0, 0.0, 0.0);

        float f = 1.0f;
        float near = 0.1f;
        float far = 100.0f;
        float[] mvp = new float[]{
            f, 0, 0, 0,
            0, f, 0, 0,
            0, 0, (far + near) / (near - far), -1.0f,
            0, 0, (2.0f * far * near) / (near - far), 0
        };
        CullingEngine.getFrustum().updateFromMatrix(mvp);
    }

    @Test
    public void testEntityInFrontIsNotCulled() {
        boolean culled = CullingEngine.shouldCullEntity(
            -1.0, -1.0, -10.0,
            1.0, 1.0, -8.0,
            true, false, false, false, false
        );
        assertFalse(culled);
    }

    @Test
    public void testEntityBehindIsCulled() {
        boolean culled = CullingEngine.shouldCullEntity(
            -1.0, -1.0, 10.0,
            1.0, 1.0, 12.0,
            true, false, false, false, false
        );
        assertTrue(culled);
    }

    @Test
    public void testBlockEntityChestCulledBehind() {
        boolean culled = CullingEngine.shouldCullBlockEntity(
            -0.5, 0.0, 5.0,
            0.5, 1.0, 6.0,
            CullingEngine.BlockEntityType.CHEST
        );
        assertTrue(culled);
    }

    @Test
    public void testDistanceCullingExceedsMaxDistance() {
        boolean culled = CullingEngine.shouldCullEntity(
            -1.0, -1.0, -91.0,
            1.0, 1.0, -89.0,
            true, false, false, false, false
        );
        assertTrue(culled);
    }
}
