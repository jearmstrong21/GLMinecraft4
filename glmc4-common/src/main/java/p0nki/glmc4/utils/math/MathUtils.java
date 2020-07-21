package p0nki.glmc4.utils.math;

import org.joml.Vector2i;

public class MathUtils {

    private MathUtils() {

    }

    public static float PI = (float) Math.PI;

    public static float clamp(float t, float min, float max) {
        return Math.max(min, Math.min(max, t));
    }

    public static float lerp(float t, float a, float b) {
        return a + (b - a) * t;
    }

    public static float norm(float t, float a, float b) {
        return (t - a) / (b - a);
    }

    public static float map(float t, float a1, float b1, float a2, float b2) {
        return lerp(norm(t, a1, b1), a2, b2);
    }

    public static long pack(int a, int b) {
        return (((long) a) << 32) | (b & 0xffffffffL);
    }

    public static long pack(Vector2i value) {
        return pack(value.x, value.y);
    }

    public static int unpackFirst(long l) {
        return (int) (l >> 32);
    }

    public static int unpackSecond(long l) {
        return (int) l;
    }

    public static Vector2i unpack(long l) {
        return new Vector2i(unpackFirst(l), unpackSecond(l));
    }

}
