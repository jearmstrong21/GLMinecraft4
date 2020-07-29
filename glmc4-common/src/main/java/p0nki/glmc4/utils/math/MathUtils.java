package p0nki.glmc4.utils.math;

public class MathUtils {

    public static float PI = (float) Math.PI;

    private MathUtils() {

    }

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

}
