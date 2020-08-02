package p0nki.glmc4.world.gen.layer;

import p0nki.glmc4.world.gen.Biomes;
import p0nki.glmc4.world.gen.RandomContext;

public enum ApplyRiverLayer implements ParentedLayer {
    INSTANCE;

    @Override
    public int sample(RandomContext context, LayerSampler a, int x, int z) {
        boolean river = Math.abs(context.getNoiseSampler().sample(x / 80d, z / 80d, 7, 0, 0)) < 0.05;

        int biome = a.sample(x, z);
        return !Layers.isOcean(biome) && river ? Biomes.RIVER.getIndex() : biome;
    }
}
