package p0nki.glmc4.wgen;

import p0nki.glmc4.utils.math.MathUtils;

import java.util.Random;

public class PerlinNoiseSampler {

    protected static final int[][] gradients = new int[][]{{1, 1, 0}, {-1, 1, 0}, {1, -1, 0}, {-1, -1, 0}, {1, 0, 1}, {-1, 0, 1}, {1, 0, -1}, {-1, 0, -1}, {0, 1, 1}, {0, -1, 1}, {0, 1, -1}, {0, -1, -1}, {1, 1, 0}, {0, -1, 1}, {-1, 1, 0}, {0, -1, -1}};
    public final double originX, originY, originZ;
    private final byte[] permutations;

    public PerlinNoiseSampler(Random random) {
        originX = 256 * random.nextDouble();
        originY = 256 * random.nextDouble();
        originZ = 256 * random.nextDouble();
        permutations = new byte[256];
        for (int i = 0; i < 256; i++) permutations[i] = (byte) i;
        for (int i = 0; i < 256; i++) {
            int k = random.nextInt(256 - i);
            byte b = permutations[i];
            permutations[i] = permutations[i + k];
            permutations[i + k] = b;
        }
    }

    protected static double dot(int[] g, double x, double y, double z) {
        return g[0] * x + g[1] * y + g[2] * z;
    }

    private static double grad(int hash, double x, double y, double z) {
        int i = hash & 15;
        return dot(gradients[i], x, y, z);
    }

    public double sample(double x, double y, double z, double d, double e) {
        double f = x + originX;
        double g = y + originY;
        double h = z + originZ;
        int i = MathUtils.floor(f);
        int j = MathUtils.floor(g);
        int k = MathUtils.floor(h);
        double l = f - i;
        double m = g - j;
        double n = h - k;
        double o = MathUtils.perlinFade(l);
        double p = MathUtils.perlinFade(m);
        double q = MathUtils.perlinFade(n);
        double t;
        if (d != 0) {
            double r = Math.min(e, m);
            t = MathUtils.floor(r / d) * d;
        } else {
            t = 0;
        }
        return sample(i, j, k, l, m - t, n, o, p, q);
    }

    private int getGradient(int hash) {
        return permutations[hash & 255] & 255;
    }

    public double sample(int sectionX, int sectionY, int sectionZ, double localX, double localY, double localZ, double fadeLocalX, double fadeLocalY, double fadeLocalZ) {
        int i = getGradient(sectionX) + sectionY;
        int j = getGradient(i) + sectionZ;
        int k = getGradient(i + 1) + sectionZ;
        int l = getGradient(sectionX + 1) + sectionY;
        int m = getGradient(l) + sectionZ;
        int n = getGradient(l + 1) + sectionZ;
        double d = grad(getGradient(j), localX, localY, localZ);
        double e = grad(getGradient(m), localX - 1, localY, localZ);
        double f = grad(getGradient(k), localX, localY - 1, localZ);
        double g = grad(getGradient(n), localX - 1, localY - 1, localZ);
        double h = grad(getGradient(j + 1), localX, localY, localZ - 1);
        double o = grad(getGradient(m + 1), localX - 1, localY, localZ - 1);
        double p = grad(getGradient(k + 1), localX, localY - 1, localZ - 1);
        double q = grad(getGradient(n + 1), localX - 1, localY - 1, localZ - 1);
        return MathUtils.lerp3(fadeLocalX, fadeLocalY, fadeLocalZ, d, e, f, g, h, o, p, q);
    }

}
