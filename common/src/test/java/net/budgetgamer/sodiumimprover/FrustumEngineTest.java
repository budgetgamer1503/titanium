package net.budgetgamer.sodiumimprover;

import net.budgetgamer.sodiumimprover.core.FrustumEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FrustumEngineTest {

    private FrustumEngine frustum;

    @BeforeEach
    public void setUp() {
        frustum = new FrustumEngine();
        frustum.setCameraPosition(0.0, 0.0, 0.0);

        float near = 0.1f;
        float far = 100.0f;
        float f = 1.0f;

        float[] mvp = new float[]{
            f, 0, 0, 0,
            0, f, 0, 0,
            0, 0, (far + near) / (near - far), -1.0f,
            0, 0, (2.0f * far * near) / (near - far), 0
        };

        frustum.updateFromMatrix(mvp);
    }

    @Test
    public void testObjectInFrontOfCameraIsVisible() {
        boolean visible = frustum.isBoxVisible(-1.0, -1.0, -11.0, 1.0, 1.0, -9.0);
        assertTrue(visible);
    }

    @Test
    public void testObjectBehindCameraIsCulled() {
        boolean visible = frustum.isBoxVisible(-1.0, -1.0, 9.0, 1.0, 1.0, 11.0);
        assertFalse(visible);
    }

    @Test
    public void testObjectFarToLeftIsCulled() {
        boolean visible = frustum.isBoxVisible(-51.0, -1.0, -11.0, -49.0, 1.0, -9.0);
        assertFalse(visible);
    }

    @Test
    public void testSphereVisibility() {
        assertTrue(frustum.isSphereVisible(0.0, 0.0, -10.0, 2.0));
        assertFalse(frustum.isSphereVisible(0.0, 0.0, 10.0, 2.0));
    }
}
