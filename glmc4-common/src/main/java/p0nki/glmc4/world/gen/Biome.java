package p0nki.glmc4.world.gen;

import p0nki.glmc4.block.BlockState;
import p0nki.glmc4.registry.Registrable;
import p0nki.glmc4.registry.Registry;

public class Biome extends Registrable<Biome> {

    private final BlockState topBlock;

    public Biome(BlockState topBlock) {
        this.topBlock = topBlock;
    }

    public BlockState getTopBlockState() {
        return topBlock;
    }

    @Override
    public Registry<Biome> getRegistry() {
        return Biomes.REGISTRY;
    }

    @Override
    public Biome getValue() {
        return this;
    }
}
