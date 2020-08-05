package p0nki.glmc4.utils.math;

import org.joml.Random;
import org.joml.SimplexNoise;
import org.joml.Vector4f;

import java.util.concurrent.atomic.AtomicLong;

public class SimplexGenerator {

    private final Vector4f offset;

    public SimplexGenerator(AtomicLong seed) {
        Random random = new Random(seed.incrementAndGet());
        offset = new Vector4f(random.nextFloat() * 1000, random.nextFloat() * 1000, random.nextFloat() * 1000, random.nextFloat() * 1000);
    }

    public float simplex(float x, float y) {
        return SimplexNoise.noise(offset.x + x, offset.y + y);
    }

    public float simplex(float x, float y, float z) {
        return SimplexNoise.noise(offset.x + x, offset.y + y, offset.z + z);
    }

    public float simplex(float x, float y, float z, float w) {
        return SimplexNoise.noise(offset.x + x, offset.y + y, offset.z + z, offset.w + w);
    }

}
