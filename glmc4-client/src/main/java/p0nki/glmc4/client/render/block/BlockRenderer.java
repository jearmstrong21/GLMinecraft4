package p0nki.glmc4.client.render.block;

import p0nki.glmc4.block.Block;
import p0nki.glmc4.client.render.MeshData;
import p0nki.glmc4.registry.Registrable;
import p0nki.glmc4.registry.Registry;

public abstract class BlockRenderer extends Registrable<BlockRenderer> {

    private final Block block;
    private final RenderLayer renderLayer;

    protected BlockRenderer(Block block) {
        this(block, RenderLayer.MAIN);
    }

    protected BlockRenderer(Block block, RenderLayer renderLayer) {
        this.block = block;
        this.renderLayer = renderLayer;
    }

    public final RenderLayer getRenderLayer() {
        return renderLayer;
    }

    public final Block getBlock() {
        return block;
    }

    @Override
    public final Registry<BlockRenderer> getRegistry() {
        return BlockRenderers.REGISTRY;
    }

    @Override
    public final BlockRenderer getValue() {
        return this;
    }

    public abstract MeshData render(BlockRenderContext context);

}
