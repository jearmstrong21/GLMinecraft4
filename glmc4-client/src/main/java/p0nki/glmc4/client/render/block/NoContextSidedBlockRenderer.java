package p0nki.glmc4.client.render.block;

import org.joml.Vector3i;
import p0nki.glmc4.block.Block;
import p0nki.glmc4.block.BlockState;
import p0nki.glmc4.client.render.MeshData;

public abstract class NoContextSidedBlockRenderer extends SidedBlockRenderer {

    protected NoContextSidedBlockRenderer(Block block) {
        super(block);
    }

    protected NoContextSidedBlockRenderer(Block block, RenderLayer renderLayer) {
        super(block, renderLayer);
    }

    protected abstract MeshData renderXmi(Vector3i blockPos, BlockState blockState);

    protected abstract MeshData renderXpl(Vector3i blockPos, BlockState blockState);

    protected abstract MeshData renderYmi(Vector3i blockPos, BlockState blockState);

    protected abstract MeshData renderYpl(Vector3i blockPos, BlockState blockState);

    protected abstract MeshData renderZmi(Vector3i blockPos, BlockState blockState);

    protected abstract MeshData renderZpl(Vector3i blockPos, BlockState blockState);

    @Override
    protected MeshData renderXmi(BlockRenderContext context) {
        return renderXmi(context.getBlockPos(), context.getCur());
    }

    @Override
    protected MeshData renderXpl(BlockRenderContext context) {
        return renderXpl(context.getBlockPos(), context.getCur());
    }

    @Override
    protected MeshData renderYmi(BlockRenderContext context) {
        return renderYmi(context.getBlockPos(), context.getCur());
    }

    @Override
    protected MeshData renderYpl(BlockRenderContext context) {
        return renderYpl(context.getBlockPos(), context.getCur());
    }

    @Override
    protected MeshData renderZmi(BlockRenderContext context) {
        return renderZmi(context.getBlockPos(), context.getCur());
    }

    @Override
    protected MeshData renderZpl(BlockRenderContext context) {
        return renderZpl(context.getBlockPos(), context.getCur());
    }
}
