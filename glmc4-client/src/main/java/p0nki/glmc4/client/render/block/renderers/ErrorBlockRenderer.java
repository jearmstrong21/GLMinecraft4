package p0nki.glmc4.client.render.block.renderers;

import org.joml.Vector3i;
import p0nki.glmc4.block.Block;
import p0nki.glmc4.block.BlockState;
import p0nki.glmc4.client.assets.AtlasPosition;
import p0nki.glmc4.client.assets.TextureQuad;
import p0nki.glmc4.client.render.block.NoContextConstantSidedBlockRenderer;
import p0nki.glmc4.utils.Identifier;

public class ErrorBlockRenderer extends NoContextConstantSidedBlockRenderer {

    public ErrorBlockRenderer(Block block) {
        super(block);
    }

    @Override
    protected TextureQuad xmiTexture(Vector3i blockPos, BlockState blockState) {
        return AtlasPosition.block(new Identifier("minecraft:error")).layer().quad();
    }

    @Override
    protected TextureQuad xplTexture(Vector3i blockPos, BlockState blockState) {
        return AtlasPosition.block(new Identifier("minecraft:error")).layer().quad();
    }

    @Override
    protected TextureQuad ymiTexture(Vector3i blockPos, BlockState blockState) {
        return AtlasPosition.block(new Identifier("minecraft:error")).layer().quad();
    }

    @Override
    protected TextureQuad yplTexture(Vector3i blockPos, BlockState blockState) {
        return AtlasPosition.block(new Identifier("minecraft:error")).layer().quad();
    }

    @Override
    protected TextureQuad zmiTexture(Vector3i blockPos, BlockState blockState) {
        return AtlasPosition.block(new Identifier("minecraft:error")).layer().quad();
    }

    @Override
    protected TextureQuad zplTexture(Vector3i blockPos, BlockState blockState) {
        return AtlasPosition.block(new Identifier("minecraft:error")).layer().quad();
    }
}
