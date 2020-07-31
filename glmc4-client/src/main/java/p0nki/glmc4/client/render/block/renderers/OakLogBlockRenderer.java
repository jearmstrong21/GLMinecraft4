package p0nki.glmc4.client.render.block.renderers;

import p0nki.glmc4.block.BlockState;
import p0nki.glmc4.block.Blocks;
import p0nki.glmc4.block.blocks.OakLogBlock;
import p0nki.glmc4.client.assets.AtlasPosition;
import p0nki.glmc4.client.assets.TextureQuad;
import p0nki.glmc4.client.render.block.NoContextConstantSidedBlockRenderer;
import p0nki.glmc4.utils.Identifier;
import p0nki.glmc4.utils.math.Axis;

public class OakLogBlockRenderer extends NoContextConstantSidedBlockRenderer {

    public OakLogBlockRenderer() {
        super(Blocks.OAK_LOG);
    }

    @Override
    protected TextureQuad xmiTexture(BlockState blockState) {
        return AtlasPosition.block(new Identifier("minecraft", blockState.get(OakLogBlock.AXIS) == Axis.X ? "oak_log_top" : "oak_log_side")).layer().quad();
    }

    @Override
    protected TextureQuad xplTexture(BlockState blockState) {
        return AtlasPosition.block(new Identifier("minecraft", blockState.get(OakLogBlock.AXIS) == Axis.X ? "oak_log_top" : "oak_log_side")).layer().quad();
    }

    @Override
    protected TextureQuad ymiTexture(BlockState blockState) {
        return AtlasPosition.block(new Identifier("minecraft", blockState.get(OakLogBlock.AXIS) == Axis.Y ? "oak_log_top" : "oak_log_side")).layer().quad();
    }

    @Override
    protected TextureQuad yplTexture(BlockState blockState) {
        return AtlasPosition.block(new Identifier("minecraft", blockState.get(OakLogBlock.AXIS) == Axis.Y ? "oak_log_top" : "oak_log_side")).layer().quad();
    }

    @Override
    protected TextureQuad zmiTexture(BlockState blockState) {
        return AtlasPosition.block(new Identifier("minecraft", blockState.get(OakLogBlock.AXIS) == Axis.Z ? "oak_log_top" : "oak_log_side")).layer().quad();
    }

    @Override
    protected TextureQuad zplTexture(BlockState blockState) {
        return AtlasPosition.block(new Identifier("minecraft", blockState.get(OakLogBlock.AXIS) == Axis.Z ? "oak_log_top" : "oak_log_side")).layer().quad();
    }
}
