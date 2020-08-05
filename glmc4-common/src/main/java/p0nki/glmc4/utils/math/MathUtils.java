package p0nki.glmc4.utils.math;

import org.joml.Vector2i;

import java.text.DecimalFormat;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MathUtils {

    public static final float PI = (float) Math.PI;

    private MathUtils() {

    }

    public static Stream<Vector2i> streamCoordinatesInChunk() {
        return IntStream.range(0, 16).boxed().flatMap(x -> IntStream.range(0, 16).mapToObj(z -> new Vector2i(x, z)));
    }

    // https://stackoverflow.com/a/5599842/9609025
    public static String readableBytes(long size) {
        if (size <= 0) return "0";
        final String[] units = new String[]{"B", "kB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
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

    public static float map(float t, float a1, float a2, float b1, float b2) {
        return lerp(norm(t, a1, a2), b1, b2);
    }

    public static long hash(int x, int z) {
        return (long) x & 4294967295L | ((long) z & 4294967295L) << 32;
    }

    public static int floor(double d) {
        int i = (int) d;
        return d < i ? i - 1 : i;
    }

    public static double perlinFade(double d) {
        return d * d * d * (d * (d * 6 - 15) + 10);
    }

    public static double lerp(double t, double a, double b) {
        return a + t * (b - a);
    }

    public static double lerp2(double tx, double ty, double d, double e, double f, double g) {
        return lerp(ty, lerp(tx, d, e), lerp(tx, f, g));
    }

    public static double lerp3(double tx, double ty, double tz, double d, double e, double f, double g, double h, double i, double j, double k) {
        return lerp(tz, lerp2(tx, ty, d, e, f, g), lerp2(tx, ty, h, i, j, k));
    }

    public static long mixSeed(long seed, long salt) {
        seed *= seed * 6364136223846793005L + 1442695040888963407L;
        seed += salt;
        return seed;
    }

    public static Stream<Vector2i> adjacentNoCenter(Vector2i v) {
        return Stream.of(
                new Vector2i(v.x - 1, v.y - 1),
                new Vector2i(v.x - 1, v.y),
                new Vector2i(v.x - 1, v.y + 1),
                new Vector2i(v.x, v.y - 1),
                new Vector2i(v.x, v.y + 1),
                new Vector2i(v.x + 1, v.y - 1),
                new Vector2i(v.x + 1, v.y),
                new Vector2i(v.x + 1, v.y + 1)
        );
    }

    public static Stream<Vector2i> adjacentWithCenter(Vector2i v) {
        return Stream.of(
                new Vector2i(v.x - 1, v.y - 1),
                new Vector2i(v.x - 1, v.y),
                new Vector2i(v.x - 1, v.y + 1),
                new Vector2i(v.x, v.y - 1),
                new Vector2i(v.x, v.y),
                new Vector2i(v.x, v.y + 1),
                new Vector2i(v.x + 1, v.y - 1),
                new Vector2i(v.x + 1, v.y),
                new Vector2i(v.x + 1, v.y + 1)
        );
    }

}
