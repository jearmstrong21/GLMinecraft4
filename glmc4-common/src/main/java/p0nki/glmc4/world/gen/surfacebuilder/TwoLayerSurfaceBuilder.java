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
    public void generate(Random random, Chunk chunk, Biome biome, int x, int z, int surfaceDepth) {
        BlockState realTop = top;
        BlockState realMiddle = middle;
        for (int y = 255; y >= 0; y--) {
            BlockState b = chunk.getBlock(x, y, z);
            if (b.getIndex() != Blocks.AIR.getIndex()) {
                if (b.getIndex() == Blocks.WATER.getIndex()) {
                    realTop = realMiddle = Blocks.SAND.getDefaultState();
                } else {
                    chunk.setBlock(x, y, z, realTop);
                    for (int i = 0; i < surfaceDepth; i++) {
                        chunk.setBlock(x, y - i - 1, z, realMiddle);
                    }
                    return;
                }
            }
        }
    }

}
