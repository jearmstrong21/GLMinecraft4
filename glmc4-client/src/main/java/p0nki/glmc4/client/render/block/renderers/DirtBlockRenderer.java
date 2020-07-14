package p0nki.glmc4.client.render.block.renderers;

import p0nki.glmc4.block.Blocks;
import p0nki.glmc4.client.assets.AtlasPosition;
import p0nki.glmc4.client.render.block.BakedConstantSidedBlockRenderer;
import p0nki.glmc4.utils.Identifier;

public class DirtBlockRenderer extends BakedConstantSidedBlockRenderer {

    public DirtBlockRenderer() {
        super(Blocks.DIRT);
    }

    @Override
    protected AtlasPosition renderXmi() {
        return AtlasPosition.block(new Identifier("minecraft:dirt"));
    }

    @Override
    protected AtlasPosition renderXpl() {
        return AtlasPosition.block(new Identifier("minecraft:dirt"));
    }

    @Override
    protected AtlasPosition renderYmi() {
        return AtlasPosition.block(new Identifier("minecraft:dirt"));
    }

    @Override
    protected AtlasPosition renderYpl() {
        return AtlasPosition.block(new Identifier("minecraft:dirt"));
    }

    @Override
    protected AtlasPosition renderZmi() {
        return AtlasPosition.block(new Identifier("minecraft:dirt"));
    }

    @Override
    protected AtlasPosition renderZpl() {
        return AtlasPosition.block(new Identifier("minecraft:dirt"));
    }
}
