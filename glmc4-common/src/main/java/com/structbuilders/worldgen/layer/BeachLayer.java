package com.structbuilders.worldgen.layer;

import com.structbuilders.worldgen.Biomes;
import com.structbuilders.worldgen.RandomContext;

public enum BeachLayer implements ParentedLayer {
    INSTANCE;

    @Override
    public int sample(RandomContext context, LayerSampler parent, int x, int z) {
        int c=parent.sample(x,z);
        if(parent.sample(x-1,z)==0||parent.sample(x+1,z)==0||parent.sample(x,z-1)==0||parent.sample(x,z+1)==0){
            if(c!=0)return Biomes.BEACH.id;
        }
        return c;
    }
}
