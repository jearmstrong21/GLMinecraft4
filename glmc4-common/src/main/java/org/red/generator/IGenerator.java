package org.red.generator;

/**
 * An interface for generic float generators.
 */
public interface IGenerator {

    /**
     * Generates a float value, designed for modular noise generators.
     *
     * @param coords Any number of coordinates, designed for noise-based generators.
     * @return A float value, examples include the height of a block.
     * @throws Exception May throw a generic exception.
     */
    public float generate(float... coords) throws Exception;
}