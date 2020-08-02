package p0nki.glmc4.world.gen.biomes.layer;

import p0nki.glmc4.utils.math.RandomContext;
import p0nki.glmc4.world.gen.biomes.Biomes;

public enum ApplyBeachLayer implements ParentedLayer {
    INSTANCE;

    public final int lookDist = 3;
    public final int lookDistRiver = 1;

    @Override
    public int sample(RandomContext context, LayerSampler a, int x, int z) {
        boolean hasFoundLand = false;
        boolean hasFoundWater = false;

        for (int dx = -lookDist; dx <= lookDist; dx++) {
            for (int dz = -lookDist; dz <= lookDist; dz++) {
                int biome = a.sample(x + dx, z + dz);
                if ((Math.abs(dx) <= lookDistRiver && Math.abs(dz) <= lookDistRiver && biome == Biomes.RIVER.getIndex()) || (Layers.isOcean(biome))) {
                    hasFoundWater = true;
                } else {
                    hasFoundLand = true;
                }
            }
        }

        boolean beach = hasFoundLand && hasFoundWater;

        int biome = a.sample(x, z);
        return beach && !(Layers.isOcean(biome) || biome == Biomes.RIVER.getIndex()) ? Biomes.BEACH.getIndex() : biome;
    }
}
