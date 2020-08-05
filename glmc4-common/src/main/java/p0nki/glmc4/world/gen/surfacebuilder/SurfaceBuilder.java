package p0nki.glmc4.world.gen.surfacebuilder;

import p0nki.glmc4.world.Chunk;
import p0nki.glmc4.world.gen.biomes.Biome;

import java.util.Random;

public abstract class SurfaceBuilder {

    public abstract void generate(Random random, Chunk chunk, Biome biome, int x, int z, int height, int surfaceDepth);

}
