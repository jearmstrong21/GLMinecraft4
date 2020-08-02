package p0nki.glmc4.utils.math;

import org.joml.Random;
import org.joml.SimplexNoise;
import org.joml.Vector2f;

public class SimplexGenerator {

    private final Vector2f offset;

    public SimplexGenerator(long seed) {
        Random random = new Random();
        offset = new Vector2f(random.nextFloat() * 1000, random.nextFloat() * 1000);
    }

    public float simplex(float x, float y) {
        return SimplexNoise.noise(offset.x + x, offset.y + y);
    }

}
