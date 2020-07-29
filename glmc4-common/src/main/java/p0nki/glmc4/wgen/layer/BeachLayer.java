package p0nki.glmc4.wgen.layer;

import p0nki.glmc4.wgen.Biomes;
import p0nki.glmc4.wgen.RandomContext;

public enum BeachLayer implements ParentedLayer {
    INSTANCE;

    @Override
    public int sample(RandomContext context, LayerSampler parent, int x, int z) {
        int c = parent.sample(x, z);
        if (parent.sample(x - 1, z) == 0 || parent.sample(x + 1, z) == 0 || parent.sample(x, z - 1) == 0 || parent.sample(x, z + 1) == 0) {
            if (c != 0) return Biomes.BEACH.id;
        }
        return c;
    }
}
