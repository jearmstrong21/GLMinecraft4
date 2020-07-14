package p0nki.glmc4.client.render.block.renderers;

import p0nki.glmc4.block.BlockState;
import p0nki.glmc4.block.Blocks;
import p0nki.glmc4.block.blocks.GrassBlock;
import p0nki.glmc4.client.assets.AtlasPosition;
import p0nki.glmc4.client.render.block.BakedConstantSidedBlockRenderer;
import p0nki.glmc4.utils.Identifier;

public class GrassBlockRenderer extends BakedConstantSidedBlockRenderer {

    public GrassBlockRenderer() {
        super(Blocks.GRASS);
    }

    @Override
    protected AtlasPosition xmiTexture(BlockState blockState) {
        return AtlasPosition.block(new Identifier(blockState.get(GrassBlock.SNOWED) ? "minecraft:grass_side_snowed" : "minecraft:grass_side"));
    }

    @Override
    protected AtlasPosition xplTexture(BlockState blockState) {
        return AtlasPosition.block(new Identifier(blockState.get(GrassBlock.SNOWED) ? "minecraft:grass_side_snowed" : "minecraft:grass_side"));
    }

    @Override
    protected AtlasPosition ymiTexture(BlockState blockState) {
        return AtlasPosition.block(new Identifier("minecraft:dirt"));
    }

    @Override
    protected AtlasPosition yplTexture(BlockState blockState) {
        return AtlasPosition.block(new Identifier("minecraft:grass_top"));
    }

    @Override
    protected AtlasPosition zmiTexture(BlockState blockState) {
        return AtlasPosition.block(new Identifier(blockState.get(GrassBlock.SNOWED) ? "minecraft:grass_side_snowed" : "minecraft:grass_side"));
    }

    @Override
    protected AtlasPosition zplTexture(BlockState blockState) {
        return AtlasPosition.block(new Identifier(blockState.get(GrassBlock.SNOWED) ? "minecraft:grass_side_snowed" : "minecraft:grass_side"));
    }
}
