package p0nki.glmc4.client.render.block;

import p0nki.glmc4.block.Blocks;
import p0nki.glmc4.client.gl.MeshData;

public abstract class SidedBlockRenderer implements BlockRenderer {

    protected abstract MeshData renderXmi(BlockRenderContext context);

    protected abstract MeshData renderXpl(BlockRenderContext context);

    protected abstract MeshData renderYmi(BlockRenderContext context);

    protected abstract MeshData renderYpl(BlockRenderContext context);

    protected abstract MeshData renderZmi(BlockRenderContext context);

    protected abstract MeshData renderZpl(BlockRenderContext context);

    @Override
    public MeshData render(BlockRenderContext context) {
        MeshData data = MeshData.chunk();
        if (context.getXmi().getBlock() == Blocks.AIR) data.append(renderXmi(context));
        if (context.getXpl().getBlock() == Blocks.AIR) data.append(renderXpl(context));
        if (context.getYmi().getBlock() == Blocks.AIR) data.append(renderYmi(context));
        if (context.getYpl().getBlock() == Blocks.AIR) data.append(renderYpl(context));
        if (context.getZmi().getBlock() == Blocks.AIR) data.append(renderZmi(context));
        if (context.getZpl().getBlock() == Blocks.AIR) data.append(renderZpl(context));
        return data;
    }
}
