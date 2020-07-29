package p0nki.glmc4.client.render.block.renderers;

import p0nki.glmc4.block.Block;
import p0nki.glmc4.block.BlockState;
import p0nki.glmc4.client.assets.AtlasPosition;
import p0nki.glmc4.client.render.block.BakedConstantSidedBlockRenderer;
import p0nki.glmc4.utils.Identifier;

public class ErrorBlockRenderer extends BakedConstantSidedBlockRenderer {

    public ErrorBlockRenderer(Block block) {
        super(block);
    }

    @Override
    protected AtlasPosition xmiTexture(BlockState blockState) {
        return AtlasPosition.block(new Identifier("minecraft:error"));
    }

    @Override
    protected AtlasPosition xplTexture(BlockState blockState) {
        return AtlasPosition.block(new Identifier("minecraft:error"));
    }

    @Override
    protected AtlasPosition ymiTexture(BlockState blockState) {
        return AtlasPosition.block(new Identifier("minecraft:error"));
    }

    @Override
    protected AtlasPosition yplTexture(BlockState blockState) {
        return AtlasPosition.block(new Identifier("minecraft:error"));
    }

    @Override
    protected AtlasPosition zmiTexture(BlockState blockState) {
        return AtlasPosition.block(new Identifier("minecraft:error"));
    }

    @Override
    protected AtlasPosition zplTexture(BlockState blockState) {
        return AtlasPosition.block(new Identifier("minecraft:error"));
    }
}
