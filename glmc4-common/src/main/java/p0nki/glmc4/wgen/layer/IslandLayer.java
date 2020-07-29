package p0nki.glmc4.wgen.layer;

import p0nki.glmc4.wgen.Biomes;
import p0nki.glmc4.wgen.RandomContext;

public enum IslandLayer implements InitLayer {
    INSTANCE;

    @Override
    public int sample(RandomContext context, int x, int z) {
        if (context.nextInt(10) == 0) return Biomes.PLAINS.id;
        return 0;
    }
}
