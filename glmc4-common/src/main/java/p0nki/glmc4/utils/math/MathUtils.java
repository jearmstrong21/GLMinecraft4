package p0nki.glmc4.utils.math;

import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MathUtils {

    public static final float PI = (float) Math.PI;

    private MathUtils() {

    }

    public static Vector2i getCoordinateInChunk(Vector3i worldPos) {
        return getCoordinateInChunk(new Vector2i(worldPos.x, worldPos.z));
    }

    public static Vector2i getCoordinateInChunk(int x, int z) {
        return new Vector2i(x, z);
    }

    public static Vector2i getCoordinateInChunk(Vector2i worldPos) {
        int x = worldPos.x;
        int z = worldPos.y;
        while (x < 0) x += 16;
        while (z < 0) z += 16;
        while (x >= 16) x -= 16;
        while (z >= 16) z -= 16;
        return new Vector2i(x, z);
    }

    public static Vector2i getChunkCoordinate(Vector3i worldPos) {
        return getChunkCoordinate(new Vector2i(worldPos.x, worldPos.z));
    }

    public static Vector2i getChunkCoordinate(int x, int z) {
        return getChunkCoordinate(new Vector2i(x, z));
    }

    public static Vector2i getChunkCoordinate(Vector2i worldPos) {
        int x = worldPos.x;
        int z = worldPos.y;
        while (x < 0) x += 16;
        while (z < 0) z += 16;
        while (x >= 16) x -= 16;
        while (z >= 16) z -= 16;
        return new Vector2i((worldPos.x - x) / 16, (worldPos.y - z) / 16);
    }

    public static Stream<Vector2i> streamCoordinatesInChunk() {
        return IntStream.range(0, 16).boxed().flatMap(x -> IntStream.range(0, 16).mapToObj(z -> new Vector2i(x, z)));
    }

    public static Stream<Vector3i> streamSphere(int radius) {
        return IntStream.range(-radius, radius).boxed().flatMap(x -> IntStream.range(-radius, radius).boxed().flatMap(y -> IntStream.range(-radius, radius).mapToObj(z -> new Vector3i(x, y, z)))).filter(pos -> pos.x * pos.x + pos.y * pos.y + pos.z * pos.z < radius * radius);
    }

    public static Stream<Vector3i> streamSphereBorder(int radius) {
        List<Vector3i> insideSphere = streamSphere(radius).collect(Collectors.toList());
        return streamSphere(radius + 1).filter(pos -> !insideSphere.contains(pos));
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

    public static Vector3i floor(Vector3f value) {
        return new Vector3i(floor(value.x), floor(value.y), floor(value.z));
    }

    public static Stream<Vector2i> fiveNeighbors(Vector2i v) {
        return Stream.of(new Vector2i(v.x - 1, v.y), new Vector2i(v.x + 1, v.y), new Vector2i(v.x, v.y - 1), new Vector2i(v.x, v.y + 1), new Vector2i(v));
    }
}
