package com.structbuilders.worldgen.layer;

import com.structbuilders.worldgen.Biomes;
import com.structbuilders.worldgen.RandomContext;

public enum IslandLayer implements InitLayer {
    INSTANCE;

    @Override
    public int sample(RandomContext context, int x, int z) {
        if(context.nextInt(10)==0)return Biomes.PLAINS.id;
        return 0;
    }
}
