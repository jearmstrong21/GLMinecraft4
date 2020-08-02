package p0nki.glmc4.world.gen.biomes.layer;

import p0nki.glmc4.utils.math.RandomContext;
import p0nki.glmc4.world.gen.biomes.Biomes;

public enum BeachLayer implements ParentedLayer {
    INSTANCE;

    @Override
    public int sample(RandomContext context, LayerSampler parent, int x, int z) {
        int c = parent.sample(x, z);
        if (parent.sample(x - 1, z) == 0 || parent.sample(x + 1, z) == 0 || parent.sample(x, z - 1) == 0 || parent.sample(x, z + 1) == 0) {
            if (c != 0) return Biomes.BEACH.getIndex();
        }
        return c;
    }
}
