package p0nki.glmc4.client.render.block.renderers;

import p0nki.glmc4.block.Blocks;
import p0nki.glmc4.client.assets.AtlasPosition;
import p0nki.glmc4.client.render.block.BakedConstantSidedBlockRenderer;
import p0nki.glmc4.utils.Identifier;

public class StoneBlockRenderer extends BakedConstantSidedBlockRenderer {

    public StoneBlockRenderer() {
        super(Blocks.STONE);
    }

    @Override
    protected AtlasPosition renderXmi() {
        return AtlasPosition.block(new Identifier("minecraft:stone"));
    }

    @Override
    protected AtlasPosition renderXpl() {
        return AtlasPosition.block(new Identifier("minecraft:stone"));
    }

    @Override
    protected AtlasPosition renderYmi() {
        return AtlasPosition.block(new Identifier("minecraft:stone"));
    }

    @Override
    protected AtlasPosition renderYpl() {
        return AtlasPosition.block(new Identifier("minecraft:stone"));
    }

    @Override
    protected AtlasPosition renderZmi() {
        return AtlasPosition.block(new Identifier("minecraft:stone"));
    }

    @Override
    protected AtlasPosition renderZpl() {
        return AtlasPosition.block(new Identifier("minecraft:stone"));
    }
}
