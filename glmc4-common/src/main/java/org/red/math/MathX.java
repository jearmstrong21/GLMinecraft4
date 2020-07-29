package org.red.math;

/**
 * Just some random math functions.
 */
public class MathX {
    /**
     * Clamps a value.
     * @param value The value that we're clamping.
     * @param min The minimum value.
     * @param max The maximum value.
     * @return The clamped value.
     */
    public static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }
}