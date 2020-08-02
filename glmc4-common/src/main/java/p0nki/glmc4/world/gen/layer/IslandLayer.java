package p0nki.glmc4.world.gen.layer;

import p0nki.glmc4.world.gen.Biomes;
import p0nki.glmc4.world.gen.RandomContext;

public enum IslandLayer implements InitLayer {
    INSTANCE;

    @Override
    public int sample(RandomContext context, int x, int z) {
        if (context.nextInt(10) == 0) return Biomes.PLAINS.getIndex();
        return 0;
    }
}
