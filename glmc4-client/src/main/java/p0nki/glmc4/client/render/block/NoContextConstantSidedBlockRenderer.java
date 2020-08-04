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

    protected abstract TextureQuad xmiTexture(Vector3i blockPos, BlockState blockState);

    protected abstract TextureQuad xplTexture(Vector3i blockPos, BlockState blockState);

    protected abstract TextureQuad ymiTexture(Vector3i blockPos, BlockState blockState);

    protected abstract TextureQuad yplTexture(Vector3i blockPos, BlockState blockState);

    protected abstract TextureQuad zmiTexture(Vector3i blockPos, BlockState blockState);

    protected abstract TextureQuad zplTexture(Vector3i blockPos, BlockState blockState);

    @Override
    protected MeshData renderXmi(Vector3i blockPos, BlockState blockState) {
        return MeshData.chunk().addXmiQuad(blockPos, new Vector3f(0, 0, 0), xmiTexture(blockPos, blockState), 1);
    }

    @Override
    protected MeshData renderXpl(Vector3i blockPos, BlockState blockState) {
        return MeshData.chunk().addXplQuad(blockPos, new Vector3f(0, 0, 0), xplTexture(blockPos, blockState), 1);
    }

    @Override
    protected MeshData renderYmi(Vector3i blockPos, BlockState blockState) {
        return MeshData.chunk().addYmiQuad(blockPos, new Vector3f(0, 0, 0), ymiTexture(blockPos, blockState));
    }

    @Override
    protected MeshData renderYpl(Vector3i blockPos, BlockState blockState) {
        return MeshData.chunk().addYplQuad(blockPos, new Vector3f(0, 0, 0), yplTexture(blockPos, blockState));
    }

    @Override
    protected MeshData renderZmi(Vector3i blockPos, BlockState blockState) {
        return MeshData.chunk().addZmiQuad(blockPos, new Vector3f(0, 0, 0), zmiTexture(blockPos, blockState), 1);
    }

    @Override
    protected MeshData renderZpl(Vector3i blockPos, BlockState blockState) {
        return MeshData.chunk().addZplQuad(blockPos, new Vector3f(0, 0, 0), zplTexture(blockPos, blockState), 1);
    }
}
