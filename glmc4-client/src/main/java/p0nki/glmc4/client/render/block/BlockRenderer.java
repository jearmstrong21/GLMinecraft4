package p0nki.glmc4.client.render.block;

import p0nki.glmc4.client.render.MeshData;
import p0nki.glmc4.registry.Registrable;
import p0nki.glmc4.registry.Registry;

public abstract class BlockRenderer extends Registrable<BlockRenderer> {

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
