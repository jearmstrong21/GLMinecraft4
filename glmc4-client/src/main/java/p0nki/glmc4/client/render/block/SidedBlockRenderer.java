package p0nki.glmc4.client.render.block;

import p0nki.glmc4.block.Block;
import p0nki.glmc4.client.render.MeshData;

public abstract class SidedBlockRenderer extends BlockRenderer {

    public SidedBlockRenderer(Block block) {
        super(block);
    }

    public SidedBlockRenderer(Block block, RenderLayer renderLayer) {
        super(block, renderLayer);
    }

    protected abstract MeshData renderXmi(BlockRenderContext context);

    protected abstract MeshData renderXpl(BlockRenderContext context);

    protected abstract MeshData renderYmi(BlockRenderContext context);

    protected abstract MeshData renderYpl(BlockRenderContext context);

    protected abstract MeshData renderZmi(BlockRenderContext context);

    protected abstract MeshData renderZpl(BlockRenderContext context);

    @Override
    public MeshData render(BlockRenderContext context) {
        MeshData data = MeshData.chunk();
        if (context.showXmi()) data.append(renderXmi(context));
        if (context.showXpl()) data.append(renderXpl(context));
        if (context.showYmi()) data.append(renderYmi(context));
        if (context.showYpl()) data.append(renderYpl(context));
        if (context.showZmi()) data.append(renderZmi(context));
        if (context.showZpl()) data.append(renderZpl(context));
        return data;
    }
}
