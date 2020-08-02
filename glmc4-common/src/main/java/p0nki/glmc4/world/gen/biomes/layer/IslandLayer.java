package p0nki.glmc4.world.gen.biomes.layer;

import p0nki.glmc4.utils.math.RandomContext;
import p0nki.glmc4.world.gen.biomes.Biomes;

public enum IslandLayer implements InitLayer {
    INSTANCE;

    @Override
    public int sample(RandomContext context, int x, int z) {
        if (context.nextInt(10) == 0) return Biomes.PLAINS.getIndex();
        return 0;
    }
}
