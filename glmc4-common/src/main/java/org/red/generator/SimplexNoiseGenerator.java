package org.red.generator;

import org.joml.SimplexNoise;
import org.red.math.*;

/**
 * An implementation of IGenerator using Simplex noise.
 */
public class SimplexNoiseGenerator implements IGenerator {
    private float _scale;
    private float _seed;
    /**
     * Creates a new Simplex noise generator,
     * @param scale The scale of the values before going into the function.
     * @param seed An offset for the noise.
     */
    public SimplexNoiseGenerator(float scale, float seed) {
        this._scale = scale;
        this._seed = seed;
    }
    /**
     * Generates 2D Simplex noise.
     * @param coords X and Y coordinates for the Simplex noise.
     * @return The result of the Simplex noise.
     * @throws Exception A generic exception if you give it the wrong parameters.
     */
    public float generate(float... coords) throws Exception {
        if (coords.length == 2) return 8 + 4 * SimplexNoise.noise((coords[0]) * _scale, (coords[1]) * _scale) + (SimplexNoise.noise((coords[0]) * _scale, (coords[1]) * _scale) * 0.5f) + (SimplexNoise.noise((coords[0]) * _scale, (coords[1]) * _scale) * 0.25f);
        else throw new Exception("You need to pass in 2 floats!");
    }
}