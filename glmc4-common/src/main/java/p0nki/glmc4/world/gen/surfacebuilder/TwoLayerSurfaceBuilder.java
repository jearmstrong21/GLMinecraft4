package p0nki.glmc4.world.gen.surfacebuilder;

import p0nki.glmc4.block.BlockState;
import p0nki.glmc4.block.Blocks;
import p0nki.glmc4.world.Chunk;
import p0nki.glmc4.world.gen.biomes.Biome;

import java.util.Random;

public class TwoLayerSurfaceBuilder extends SurfaceBuilder {

    private final BlockState top;
    private final BlockState middle;

    public TwoLayerSurfaceBuilder(BlockState top, BlockState middle) {
        this.top = top;
        this.middle = middle;
    }


    @Override
    public void generate(Random random, Chunk chunk, Biome biome, int x, int z, int height, int surfaceDepth) {
        BlockState realTop = top;
        BlockState realMiddle = middle;
        if (height <= 64) {
            realTop = realMiddle = Blocks.SAND.getDefaultState();
        }
        chunk.set(x, height, z, realTop);
        for (int y = height - surfaceDepth; y < height; y++) {
            chunk.set(x, y, z, realMiddle);
        }
    }

}
