package com.structbuilders.worldgen.layer;

import com.structbuilders.worldgen.Biomes;
import com.structbuilders.worldgen.RandomContext;

public enum ApplyRiverLayer implements ParentedLayer {
    INSTANCE;

    @Override
    public int sample(RandomContext context, LayerSampler a, int x, int z) {
        boolean river = Math.abs(context.getNoiseSampler().sample(x / 80d, z / 80d, 7, 0, 0)) < 0.05;

        int biome = a.sample(x, z);
        return !Layers.isOcean(biome) && river ? Biomes.RIVER.id : biome;
    }
}
