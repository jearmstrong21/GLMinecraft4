package p0nki.glmc4.client.render.block.renderers;

import org.joml.Vector3i;
import p0nki.glmc4.block.BlockState;
import p0nki.glmc4.block.Blocks;
import p0nki.glmc4.client.assets.AtlasPosition;
import p0nki.glmc4.client.assets.TextureQuad;
import p0nki.glmc4.client.render.Colors;
import p0nki.glmc4.utils.Identifier;

public class OakLeavesBlockRenderer extends NoContextTexturedSidedBlockRenderer {

    public OakLeavesBlockRenderer() {
        super(Blocks.OAK_LEAVES);
    }

    @Override
    protected TextureQuad xmiTexture(Vector3i blockPos, BlockState blockState) {
        return AtlasPosition.block(new Identifier("minecraft", "oak_leaves")).layer(Colors.GRASS_COLOR).quad();
    }

    @Override
    protected TextureQuad xplTexture(Vector3i blockPos, BlockState blockState) {
        return AtlasPosition.block(new Identifier("minecraft", "oak_leaves")).layer(Colors.GRASS_COLOR).quad();
    }

    @Override
    protected TextureQuad ymiTexture(Vector3i blockPos, BlockState blockState) {
        return AtlasPosition.block(new Identifier("minecraft", "oak_leaves")).layer(Colors.GRASS_COLOR).quad();
    }

    @Override
    protected TextureQuad yplTexture(Vector3i blockPos, BlockState blockState) {
        return AtlasPosition.block(new Identifier("minecraft", "oak_leaves")).layer(Colors.GRASS_COLOR).quad();
    }

    @Override
    protected TextureQuad zmiTexture(Vector3i blockPos, BlockState blockState) {
        return AtlasPosition.block(new Identifier("minecraft", "oak_leaves")).layer(Colors.GRASS_COLOR).quad();
    }

    @Override
    protected TextureQuad zplTexture(Vector3i blockPos, BlockState blockState) {
        return AtlasPosition.block(new Identifier("minecraft", "oak_leaves")).layer(Colors.GRASS_COLOR).quad();
    }
}