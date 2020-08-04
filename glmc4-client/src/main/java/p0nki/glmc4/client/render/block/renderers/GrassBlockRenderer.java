package p0nki.glmc4.client.render.block.renderers;

import org.joml.Vector2i;
import org.joml.Vector3i;
import p0nki.glmc4.block.BlockState;
import p0nki.glmc4.block.Blocks;
import p0nki.glmc4.block.blocks.GrassBlock;
import p0nki.glmc4.client.GLMC4Client;
import p0nki.glmc4.client.assets.AtlasPosition;
import p0nki.glmc4.client.assets.TextureQuad;
import p0nki.glmc4.client.render.block.NoContextConstantSidedBlockRenderer;
import p0nki.glmc4.utils.Identifier;

public class GrassBlockRenderer extends NoContextConstantSidedBlockRenderer {

    public GrassBlockRenderer() {
        super(Blocks.GRASS);
    }

    @Override
    protected TextureQuad xmiTexture(Vector3i blockPos, BlockState blockState) {
        return AtlasPosition.block(new Identifier(blockState.get(GrassBlock.SNOWED) ? "minecraft:grass_side_snowed" : "minecraft:grass_side")).layer().quad();
    }

    @Override
    protected TextureQuad xplTexture(Vector3i blockPos, BlockState blockState) {
        return AtlasPosition.block(new Identifier(blockState.get(GrassBlock.SNOWED) ? "minecraft:grass_side_snowed" : "minecraft:grass_side")).layer().quad();
    }

    @Override
    protected TextureQuad ymiTexture(Vector3i blockPos, BlockState blockState) {
        return AtlasPosition.block(new Identifier("minecraft:dirt")).layer().quad();
    }

    @Override
    protected TextureQuad yplTexture(Vector3i blockPos, BlockState blockState) {
        return (blockState.get(GrassBlock.SNOWED) ? AtlasPosition.block(new Identifier("minecraft:snow")).layer() : AtlasPosition.block(new Identifier("minecraft:grass_top")).layer(GLMC4Client.getClientWorld().getBiome(new Vector2i(blockPos.x, blockPos.z)).getGrassColor())).quad();
    }

    @Override
    protected TextureQuad zmiTexture(Vector3i blockPos, BlockState blockState) {
        return AtlasPosition.block(new Identifier(blockState.get(GrassBlock.SNOWED) ? "minecraft:grass_side_snowed" : "minecraft:grass_side")).layer().quad();
    }

    @Override
    protected TextureQuad zplTexture(Vector3i blockPos, BlockState blockState) {
        return AtlasPosition.block(new Identifier(blockState.get(GrassBlock.SNOWED) ? "minecraft:grass_side_snowed" : "minecraft:grass_side")).layer().quad();
    }
}
