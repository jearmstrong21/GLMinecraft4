package p0nki.glmc4.client.render.block;

import p0nki.glmc4.block.Block;
import p0nki.glmc4.block.BlockState;
import p0nki.glmc4.client.gl.MeshData;

public abstract class BakedSidedBlockRenderer extends SidedBlockRenderer {

    private final BakedBlockState xmi;
    private final BakedBlockState xpl;
    private final BakedBlockState ymi;
    private final BakedBlockState ypl;
    private final BakedBlockState zmi;
    private final BakedBlockState zpl;

    public BakedSidedBlockRenderer(Block block) {
        xmi = new BakedBlockState(block, this::renderXmi);
        xpl = new BakedBlockState(block, this::renderXpl);
        ymi = new BakedBlockState(block, this::renderYmi);
        ypl = new BakedBlockState(block, this::renderYpl);
        zmi = new BakedBlockState(block, this::renderZmi);
        zpl = new BakedBlockState(block, this::renderZpl);
    }

    protected abstract MeshData renderXmi(BlockState blockState);

    protected abstract MeshData renderXpl(BlockState blockState);

    protected abstract MeshData renderYmi(BlockState blockState);

    protected abstract MeshData renderYpl(BlockState blockState);

    protected abstract MeshData renderZmi(BlockState blockState);

    protected abstract MeshData renderZpl(BlockState blockState);

    @Override
    protected MeshData renderXmi(BlockRenderContext context) {
        return xmi.render(context.getCur());
    }

    @Override
    protected MeshData renderXpl(BlockRenderContext context) {
        return xpl.render(context.getCur());
    }

    @Override
    protected MeshData renderYmi(BlockRenderContext context) {
        return ymi.render(context.getCur());
    }

    @Override
    protected MeshData renderYpl(BlockRenderContext context) {
        return ypl.render(context.getCur());
    }

    @Override
    protected MeshData renderZmi(BlockRenderContext context) {
        return zmi.render(context.getCur());
    }

    @Override
    protected MeshData renderZpl(BlockRenderContext context) {
        return zpl.render(context.getCur());
    }
}
