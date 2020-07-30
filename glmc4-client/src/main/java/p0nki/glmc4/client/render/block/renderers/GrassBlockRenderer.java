package p0nki.glmc4.client.render.block.renderers;

import p0nki.glmc4.block.BlockState;
import p0nki.glmc4.block.Blocks;
import p0nki.glmc4.block.blocks.GrassBlock;
import p0nki.glmc4.client.assets.AtlasPosition;
import p0nki.glmc4.client.assets.TextureQuad;
import p0nki.glmc4.client.render.Colors;
import p0nki.glmc4.client.render.block.BakedConstantSidedBlockRenderer;
import p0nki.glmc4.utils.Identifier;

public class GrassBlockRenderer extends BakedConstantSidedBlockRenderer {

    public GrassBlockRenderer() {
        super(Blocks.GRASS);
    }

    @Override
    protected TextureQuad xmiTexture(BlockState blockState) {
        return AtlasPosition.block(new Identifier(blockState.get(GrassBlock.SNOWED) ? "minecraft:grass_side_snowed" : "minecraft:grass_side")).layer().quad();
    }

    @Override
    protected TextureQuad xplTexture(BlockState blockState) {
        return AtlasPosition.block(new Identifier(blockState.get(GrassBlock.SNOWED) ? "minecraft:grass_side_snowed" : "minecraft:grass_side")).layer().quad();
    }

    @Override
    protected TextureQuad ymiTexture(BlockState blockState) {
        return AtlasPosition.block(new Identifier("minecraft:dirt")).layer().quad();
    }

    @Override
    protected TextureQuad yplTexture(BlockState blockState) {
        return (blockState.get(GrassBlock.SNOWED) ? AtlasPosition.block(new Identifier("minecraft:snow")).layer() : AtlasPosition.block(new Identifier("minecraft:grass_top")).layer(Colors.GRASS_COLOR)).quad();
    }

    @Override
    protected TextureQuad zmiTexture(BlockState blockState) {
        return AtlasPosition.block(new Identifier(blockState.get(GrassBlock.SNOWED) ? "minecraft:grass_side_snowed" : "minecraft:grass_side")).layer().quad();
    }

    @Override
    protected TextureQuad zplTexture(BlockState blockState) {
        return AtlasPosition.block(new Identifier(blockState.get(GrassBlock.SNOWED) ? "minecraft:grass_side_snowed" : "minecraft:grass_side")).layer().quad();
    }
}
