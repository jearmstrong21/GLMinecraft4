package p0nki.glmc4.client.render.block;

import org.joml.Random;
import org.joml.Vector3i;
import p0nki.glmc4.block.BlockState;

public class BlockRenderContext {

    private final RenderLayer renderLayer;
    private final Vector3i blockPos;
    private final BlockState xmi;
    private final BlockState xpl;
    private final BlockState ymi;
    private final BlockState ypl;
    private final BlockState zmi;
    private final BlockState zpl;
    private final BlockState cur;
    private final Random random;

    public BlockRenderContext(RenderLayer renderLayer, Vector3i blockPos, BlockState xmi, BlockState xpl, BlockState ymi, BlockState ypl, BlockState zmi, BlockState zpl, BlockState cur) {
        this.renderLayer = renderLayer;
        this.blockPos = blockPos;
        this.xmi = xmi;
        this.xpl = xpl;
        this.ymi = ymi;
        this.ypl = ypl;
        this.zmi = zmi;
        this.zpl = zpl;
        this.cur = cur;
        random = new Random(blockPos.hashCode());
    }

    public Random getRandom() {
        return random;
    }

    public RenderLayer getRenderLayer() {
        return renderLayer;
    }

    public Vector3i getBlockPos() {
        return blockPos;
    }

    public BlockState getXmi() {
        return xmi;
    }

    public BlockState getXpl() {
        return xpl;
    }

    public BlockState getYmi() {
        return ymi;
    }

    public BlockState getYpl() {
        return ypl;
    }

    public BlockState getZmi() {
        return zmi;
    }

    public BlockState getZpl() {
        return zpl;
    }

    public BlockState getCur() {
        return cur;
    }

    private boolean show(BlockState blockState) {
        return !blockState.isFullBlock() || BlockRenderers.REGISTRY.get(blockState.getIndex()).getValue().getRenderLayer().showsTo(renderLayer);
    }

    public boolean showXmi() {
        return show(xmi);
    }

    public boolean showXpl() {
        return show(xpl);
    }

    public boolean showYmi() {
        return show(ymi);
    }

    public boolean showYpl() {
        return show(ypl);
    }

    public boolean showZmi() {
        return show(zmi);
    }

    public boolean showZpl() {
        return show(zpl);
    }

}