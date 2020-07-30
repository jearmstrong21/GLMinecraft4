package p0nki.glmc4.client.render.block;

import org.joml.Vector3i;
import p0nki.glmc4.block.BlockState;

public class BlockRenderContext {

    private final Vector3i blockPos;
    private final BlockState xmi;
    private final BlockState xpl;
    private final BlockState ymi;
    private final BlockState ypl;
    private final BlockState zmi;
    private final BlockState zpl;
    private final BlockState cur;

    public BlockRenderContext(Vector3i blockPos, BlockState xmi, BlockState xpl, BlockState ymi, BlockState ypl, BlockState zmi, BlockState zpl, BlockState cur) {
        this.blockPos = blockPos;
        this.xmi = xmi;
        this.xpl = xpl;
        this.ymi = ymi;
        this.ypl = ypl;
        this.zmi = zmi;
        this.zpl = zpl;
        this.cur = cur;
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

}