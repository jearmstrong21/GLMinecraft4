package p0nki.glmc4.world.gen.biomes.layer;

import p0nki.glmc4.utils.math.RandomContext;

public enum ApplyIslandsLayer implements BiParentedLayer {
    INSTANCE;

    @Override
    public int sample(RandomContext context, LayerSampler a, LayerSampler b, int x, int z) {
        int _a = a.sample(x, z);
        int _b = b.sample(x, z);
        if (!Layers.isOcean(_b)) return _b;
        return _a;
    }
}
