package p0nki.glmc4.wgen;

import p0nki.glmc4.utils.math.MathUtils;

import java.util.Random;

public class RandomContext {

    private final long worldSeed;
    private final PerlinNoiseSampler noiseSampler;
    private long localSeed;

    public RandomContext(long seed, long salt) {
        worldSeed = addSalt(seed, salt);
        noiseSampler = new PerlinNoiseSampler(new Random(seed));
    }

    private static long addSalt(long seed, long salt) {
        long l = MathUtils.mixSeed(salt, salt);
        l = MathUtils.mixSeed(l, salt);
        l = MathUtils.mixSeed(l, salt);
        long m = MathUtils.mixSeed(seed, l);
        m = MathUtils.mixSeed(m, l);
        m = MathUtils.mixSeed(m, l);
        return m;
    }

    public void initSeed(int x, int z) {
        long l = worldSeed;
        l = MathUtils.mixSeed(l, x);
        l = MathUtils.mixSeed(l, z);
        l = MathUtils.mixSeed(l, x);
        l = MathUtils.mixSeed(l, z);
        localSeed = l;
    }

    public int nextInt(int bound) {
        int i = (int) Math.floorMod(localSeed >> 24, (long) bound);
        localSeed = MathUtils.mixSeed(localSeed, worldSeed);
        return i;
    }

    public PerlinNoiseSampler getNoiseSampler() {
        return noiseSampler;
    }

}
