package com.structbuilders.worldgen.layer;

import com.structbuilders.worldgen.RandomContext;

public enum ApplyIslandsLayer implements BiParentedLayer {
    INSTANCE;

    @Override
    public int sample(RandomContext context, LayerSampler a, LayerSampler b, int x, int z) {
        int _a=a.sample(x,z);
        int _b=b.sample(x,z);
        if(!Layers.isOcean(_b))return _b;
        return _a;
    }
}
