package p0nki.glmc4.client.render.block.renderers;

import p0nki.glmc4.block.BlockState;
import p0nki.glmc4.block.Blocks;
import p0nki.glmc4.client.assets.AtlasPosition;
import p0nki.glmc4.client.assets.TextureQuad;
import p0nki.glmc4.client.render.block.NoContextConstantSidedBlockRenderer;
import p0nki.glmc4.utils.Identifier;

public class StoneBlockRenderer extends NoContextConstantSidedBlockRenderer {

    public StoneBlockRenderer() {
        super(Blocks.STONE);
    }

    @Override
    protected TextureQuad xmiTexture(BlockState blockState) {
        return AtlasPosition.block(new Identifier("minecraft:stone")).layer().quad();
    }

    @Override
    protected TextureQuad xplTexture(BlockState blockState) {
        return AtlasPosition.block(new Identifier("minecraft:stone")).layer().quad();
    }

    @Override
    protected TextureQuad ymiTexture(BlockState blockState) {
        return AtlasPosition.block(new Identifier("minecraft:stone")).layer().quad();
    }

    @Override
    protected TextureQuad yplTexture(BlockState blockState) {
        return AtlasPosition.block(new Identifier("minecraft:stone")).layer().quad();
    }

    @Override
    protected TextureQuad zmiTexture(BlockState blockState) {
        return AtlasPosition.block(new Identifier("minecraft:stone")).layer().quad();
    }

    @Override
    protected TextureQuad zplTexture(BlockState blockState) {
        return AtlasPosition.block(new Identifier("minecraft:stone")).layer().quad();
    }
}
