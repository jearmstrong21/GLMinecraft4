package p0nki.glmc4.world.gen;

import org.joml.Random;
import org.joml.SimplexNoise;

public class SimplexNoiseGenerator {
    private final float scale;
    private final float seed;

    public SimplexNoiseGenerator(float scale, long seed) {
        this.scale = scale;
        this.seed = new Random(seed).nextFloat() * 100;
    }

    public float generate(float x, float y) {
        return SimplexNoise.noise(x * scale + seed, y * scale + seed);
    }
}