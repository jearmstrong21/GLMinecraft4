package p0nki.glmc4.client.render.block.renderers;

import p0nki.glmc4.block.Block;
import p0nki.glmc4.block.BlockState;
import p0nki.glmc4.block.Blocks;
import p0nki.glmc4.client.assets.AtlasPosition;
import p0nki.glmc4.client.assets.TextureQuad;
import p0nki.glmc4.client.render.Colors;
import p0nki.glmc4.client.render.block.BakedConstantSidedBlockRenderer;
import p0nki.glmc4.utils.Identifier;

public class OakLeavesBlockRenderer extends BakedConstantSidedBlockRenderer {

    public OakLeavesBlockRenderer() {
        super(Blocks.OAK_LEAVES);
    }

    @Override
    protected TextureQuad xmiTexture(BlockState blockState) {
        return AtlasPosition.block(new Identifier("minecraft", "oak_leaves")).layer(Colors.GRASS_COLOR).quad();
    }

    @Override
    protected TextureQuad xplTexture(BlockState blockState) {
        return AtlasPosition.block(new Identifier("minecraft", "oak_leaves")).layer(Colors.GRASS_COLOR).quad();
    }

    @Override
    protected TextureQuad ymiTexture(BlockState blockState) {
        return AtlasPosition.block(new Identifier("minecraft", "oak_leaves")).layer(Colors.GRASS_COLOR).quad();
    }

    @Override
    protected TextureQuad yplTexture(BlockState blockState) {
        return AtlasPosition.block(new Identifier("minecraft", "oak_leaves")).layer(Colors.GRASS_COLOR).quad();
    }

    @Override
    protected TextureQuad zmiTexture(BlockState blockState) {
        return AtlasPosition.block(new Identifier("minecraft", "oak_leaves")).layer(Colors.GRASS_COLOR).quad();
    }

    @Override
    protected TextureQuad zplTexture(BlockState blockState) {
        return AtlasPosition.block(new Identifier("minecraft", "oak_leaves")).layer(Colors.GRASS_COLOR).quad();
    }
}
