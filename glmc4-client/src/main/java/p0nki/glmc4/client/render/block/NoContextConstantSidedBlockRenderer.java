package p0nki.glmc4.client.render.block;

import org.joml.Vector3f;
import org.joml.Vector3i;
import p0nki.glmc4.block.Block;
import p0nki.glmc4.block.BlockState;
import p0nki.glmc4.client.assets.TextureQuad;
import p0nki.glmc4.client.render.MeshData;

public abstract class NoContextConstantSidedBlockRenderer extends NoContextSidedBlockRenderer {

    public NoContextConstantSidedBlockRenderer(Block block) {
        super(block);
    }

    protected abstract TextureQuad xmiTexture(BlockState blockState);

    protected abstract TextureQuad xplTexture(BlockState blockState);

    protected abstract TextureQuad ymiTexture(BlockState blockState);

    protected abstract TextureQuad yplTexture(BlockState blockState);

    protected abstract TextureQuad zmiTexture(BlockState blockState);

    protected abstract TextureQuad zplTexture(BlockState blockState);

    @Override
    protected MeshData renderXmi(Vector3i blockPos, BlockState blockState) {
        return MeshData.chunk().addXmiQuad(blockPos, new Vector3f(0, 0, 0), xmiTexture(blockState));
    }

    @Override
    protected MeshData renderXpl(Vector3i blockPos, BlockState blockState) {
        return MeshData.chunk().addXplQuad(blockPos, new Vector3f(0, 0, 0), xplTexture(blockState));
    }

    @Override
    protected MeshData renderYmi(Vector3i blockPos, BlockState blockState) {
        return MeshData.chunk().addYmiQuad(blockPos, new Vector3f(0, 0, 0), ymiTexture(blockState));
    }

    @Override
    protected MeshData renderYpl(Vector3i blockPos, BlockState blockState) {
        return MeshData.chunk().addYplQuad(blockPos, new Vector3f(0, 0, 0), yplTexture(blockState));
    }

    @Override
    protected MeshData renderZmi(Vector3i blockPos, BlockState blockState) {
        return MeshData.chunk().addZmiQuad(blockPos, new Vector3f(0, 0, 0), zmiTexture(blockState));
    }

    @Override
    protected MeshData renderZpl(Vector3i blockPos, BlockState blockState) {
        return MeshData.chunk().addZplQuad(blockPos, new Vector3f(0, 0, 0), zplTexture(blockState));
    }
}
