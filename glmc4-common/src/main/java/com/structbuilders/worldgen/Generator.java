package com.structbuilders.worldgen;

import com.structbuilders.worldgen.layer.LayerSampler;
import com.structbuilders.worldgen.layer.Layers;

public class Generator {
    /**
     * generates shit
     * @param seed the seed
     * @param w width of chunk (usually 16)
     * @param h height (z dir) of chunk (usually 16)
     * @param cx chunk x
     * @param cz chunk z
     * @return chunk data or something idk
     */
    public static int[][] generate(long seed, int w, int h, int cx, int cz) {
        LayerSampler sampler = Layers.build(seed, w, h);
        int[][] data = new int[w][h];

        for (int x = 0; x < w; x++) {
            for (int z = 0; z < h; z++) {
                data[x][z] = sampler.sample(x + cx * w, z + cz * h);
            }
        }

        return data;
    }
}
