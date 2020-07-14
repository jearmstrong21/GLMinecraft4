package p0nki.glmc4.client.render.block.renderers;

import p0nki.glmc4.block.BlockState;
import p0nki.glmc4.block.Blocks;
import p0nki.glmc4.client.assets.AtlasPosition;
import p0nki.glmc4.client.render.block.BakedConstantSidedBlockRenderer;
import p0nki.glmc4.utils.Identifier;

public class StoneBlockRenderer extends BakedConstantSidedBlockRenderer {

    public StoneBlockRenderer() {
        super(Blocks.STONE);
    }

    @Override
    protected AtlasPosition xmiTexture(BlockState blockState) {
        return AtlasPosition.block(new Identifier("minecraft:stone"));
    }

    @Override
    protected AtlasPosition xplTexture(BlockState blockState) {
        return AtlasPosition.block(new Identifier("minecraft:stone"));
    }

    @Override
    protected AtlasPosition ymiTexture(BlockState blockState) {
        return AtlasPosition.block(new Identifier("minecraft:stone"));
    }

    @Override
    protected AtlasPosition yplTexture(BlockState blockState) {
        return AtlasPosition.block(new Identifier("minecraft:stone"));
    }

    @Override
    protected AtlasPosition zmiTexture(BlockState blockState) {
        return AtlasPosition.block(new Identifier("minecraft:stone"));
    }

    @Override
    protected AtlasPosition zplTexture(BlockState blockState) {
        return AtlasPosition.block(new Identifier("minecraft:stone"));
    }
}
