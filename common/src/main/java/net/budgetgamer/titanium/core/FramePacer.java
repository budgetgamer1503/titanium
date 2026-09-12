package net.budgetgamer.titanium.core;

import net.budgetgamer.titanium.config.ConfigManager;
import net.budgetgamer.titanium.config.TitaniumConfig;

public final class FramePacer {

    private static final int SAMPLE_COUNT = 60;
    private static final long[] FRAME_TIMES_NS = new long[SAMPLE_COUNT];
    private static int sampleIndex = 0;
    private static long lastFrameTimeNs = System.nanoTime();

    private static double currentFps = 60.0;
    private static double dynamicScale = 1.0;

    private FramePacer() {}

    public static void onFrameStart() {
        long now = System.nanoTime();
        long deltaNs = now - lastFrameTimeNs;
        lastFrameTimeNs = now;

        if (deltaNs <= 0 || deltaNs > 1_000_000_000L) {
            deltaNs = 16_666_666L;
        }

        FRAME_TIMES_NS[sampleIndex] = deltaNs;
        sampleIndex = (sampleIndex + 1) % SAMPLE_COUNT;

        long totalNs = 0;
        for (int i = 0; i < SAMPLE_COUNT; i++) {
            totalNs += FRAME_TIMES_NS[i];
        }
        double avgNs = (double) totalNs / SAMPLE_COUNT;
        currentFps = (avgNs > 0) ? (1_000_000_000.0 / avgNs) : 60.0;

        updateDynamicScaling();
    }

    private static void updateDynamicScaling() {
        TitaniumConfig config = ConfigManager.getConfig();
        if (!config.dynamicFramePacerEnabled || !config.dynamicDistanceScaling) {
            dynamicScale = 1.0;
            return;
        }

        double target = (double) Math.max(15, config.targetFps);
        if (currentFps < target) {
            double ratio = currentFps / target;
            double targetScale = Math.max(config.minDistanceScale, ratio);
            dynamicScale = dynamicScale * 0.9 + targetScale * 0.1;
        } else {
            dynamicScale = Math.min(1.0, dynamicScale * 0.95 + 1.0 * 0.05);
        }
    }

    public static double getDynamicScale() {
        return dynamicScale;
    }

    public static double getCurrentFps() {
        return currentFps;
    }
}
