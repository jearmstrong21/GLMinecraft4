package org.red.generator;

import java.util.Random;

/**
 * An implementation of IGenerator using random positive integers.
 */
public class RandomGenerator implements IGenerator {
    private Random _gen;

    /**
     * Initializes the generator with the given seed.
     *
     * @param seed The seed for the generator.
     */
    public RandomGenerator(long seed) {
        _gen = new Random(seed);
    }

    /**
     * Generates a pseudorandom integer between 0 and a value.
     *
     * @param coords The upper limit for the random numbers.
     * @return A random integer between 0 and coords.
     * @throws Exception A generic exception if you give it more than one value.
     */
    public float generate(float... coords) throws Exception {
        if (coords.length == 1) return _gen.nextInt((int) coords[0]);
        else throw new Exception("You need to pass in one integer!");
    }
}