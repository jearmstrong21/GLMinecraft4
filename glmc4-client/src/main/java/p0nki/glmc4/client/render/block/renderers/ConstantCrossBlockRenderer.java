package p0nki.glmc4.client.render.block.renderers;

import p0nki.glmc4.block.Block;
import p0nki.glmc4.block.BlockState;
import p0nki.glmc4.client.assets.TextureQuad;

public class ConstantCrossBlockRenderer extends CrossBlockRenderer {

    private final TextureQuad quad;

    public ConstantCrossBlockRenderer(Block block, TextureQuad quad) {
        super(block);
        this.quad = quad;
    }

    @Override
    protected TextureQuad getQuad(BlockState blockState) {
        return quad;
    }

}
