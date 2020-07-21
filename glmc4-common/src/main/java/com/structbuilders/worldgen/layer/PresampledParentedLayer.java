package com.structbuilders.worldgen.layer;

import com.structbuilders.worldgen.RandomContext;

public interface PresampledParentedLayer {
    default LayerFactory create(RandomContext context, LayerSampler parent) {
        return () -> {
            LayerSampler p = parent;
            return CachedLayerSampler.create((x, z) -> {
                context.initSeed(x, z);
                return sample(context, p, x, z);
            });
        };
    }

    int sample(RandomContext context, LayerSampler parent, int x, int z);
}
