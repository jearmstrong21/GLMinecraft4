package p0nki.glmc4.wgen.layer;

import p0nki.glmc4.wgen.Biomes;
import p0nki.glmc4.wgen.RandomContext;

public enum ApplyBeechLayer implements ParentedLayer {
    INSTANCE;

    public int lookDist = 3;
    public int lookDistRiver = 1;

    @Override
    public int sample(RandomContext context, LayerSampler a, int x, int z) {
        boolean hasFoundLand = false;
        boolean hasFoundWater = false;

        for (int dx = -lookDist; dx <= lookDist; dx++) {
            for (int dz = -lookDist; dz <= lookDist; dz++) {
                int biome = a.sample(x + dx, z + dz);
                if ((Math.abs(dx) <= lookDistRiver && Math.abs(dz) <= lookDistRiver && biome == Biomes.RIVER.id) || (Layers.isOcean(biome))) {
                    hasFoundWater = true;
                } else {
                    hasFoundLand = true;
                }
            }
        }

        boolean beach = hasFoundLand && hasFoundWater;

        int biome = a.sample(x, z);
        return beach && !(Layers.isOcean(biome) || biome == Biomes.RIVER.id) ? Biomes.BEACH.id : biome;
    }
}
