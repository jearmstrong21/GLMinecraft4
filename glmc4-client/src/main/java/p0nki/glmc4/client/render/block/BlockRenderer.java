package p0nki.glmc4.client.render.block;

import p0nki.glmc4.block.Block;
import p0nki.glmc4.client.render.MeshData;
import p0nki.glmc4.registry.Registrable;
import p0nki.glmc4.registry.Registry;

public abstract class BlockRenderer extends Registrable<BlockRenderer> {

    private final Block block;

    public BlockRenderer(Block block) {
        this.block = block;
    }

    public final Block getBlock() {
        return block;
    }

    @Override
    public Registry<BlockRenderer> getRegistry() {
        return BlockRenderers.REGISTRY;
    }

    @Override
    public BlockRenderer getValue() {
        return this;
    }

    public abstract MeshData render(BlockRenderContext context);

}
