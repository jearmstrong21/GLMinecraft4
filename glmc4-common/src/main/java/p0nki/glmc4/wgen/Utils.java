package p0nki.glmc4.wgen;

public class Utils {

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

}
