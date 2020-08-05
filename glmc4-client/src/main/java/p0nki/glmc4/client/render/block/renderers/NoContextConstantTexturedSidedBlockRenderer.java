package p0nki.glmc4.client.render.block.renderers;

import org.joml.Vector3i;
import p0nki.glmc4.block.Block;
import p0nki.glmc4.block.BlockState;
import p0nki.glmc4.client.assets.TextureQuad;

public class NoContextConstantTexturedSidedBlockRenderer extends NoContextTexturedSidedBlockRenderer {

    private final TextureQuad xmi;
    private final TextureQuad xpl;
    private final TextureQuad ymi;
    private final TextureQuad ypl;
    private final TextureQuad zmi;
    private final TextureQuad zpl;

    public NoContextConstantTexturedSidedBlockRenderer(Block block, TextureQuad side) {
        this(block, side, side, side, side, side, side);
    }

    public NoContextConstantTexturedSidedBlockRenderer(Block block, TextureQuad top, TextureQuad side, TextureQuad bottom) {
        this(block, side, side, bottom, top, side, side);
    }

    public NoContextConstantTexturedSidedBlockRenderer(Block block, TextureQuad xmi, TextureQuad xpl, TextureQuad ymi, TextureQuad ypl, TextureQuad zmi, TextureQuad zpl) {
        super(block);
        this.xmi = xmi;
        this.xpl = xpl;
        this.ymi = ymi;
        this.ypl = ypl;
        this.zmi = zmi;
        this.zpl = zpl;
    }

    @Override
    protected TextureQuad xmiTexture(Vector3i blockPos, BlockState blockState) {
        return xmi;
    }

    @Override
    protected TextureQuad xplTexture(Vector3i blockPos, BlockState blockState) {
        return xpl;
    }

    @Override
    protected TextureQuad ymiTexture(Vector3i blockPos, BlockState blockState) {
        return ymi;
    }

    @Override
    protected TextureQuad yplTexture(Vector3i blockPos, BlockState blockState) {
        return ypl;
    }

    @Override
    protected TextureQuad zmiTexture(Vector3i blockPos, BlockState blockState) {
        return zmi;
    }

    @Override
    protected TextureQuad zplTexture(Vector3i blockPos, BlockState blockState) {
        return zpl;
    }
}
