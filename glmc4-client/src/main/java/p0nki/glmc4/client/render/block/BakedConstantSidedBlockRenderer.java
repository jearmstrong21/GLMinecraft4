package p0nki.glmc4.client.render.block;

import org.joml.Vector3f;
import p0nki.glmc4.block.Block;
import p0nki.glmc4.block.BlockState;
import p0nki.glmc4.client.assets.TextureQuad;
import p0nki.glmc4.client.render.MeshData;

public abstract class BakedConstantSidedBlockRenderer extends BakedSidedBlockRenderer {

    public BakedConstantSidedBlockRenderer(Block block) {
        super(block);
    }

    protected abstract TextureQuad xmiTexture(BlockState blockState);

    protected abstract TextureQuad xplTexture(BlockState blockState);

    protected abstract TextureQuad ymiTexture(BlockState blockState);

    protected abstract TextureQuad yplTexture(BlockState blockState);

    protected abstract TextureQuad zmiTexture(BlockState blockState);

    protected abstract TextureQuad zplTexture(BlockState blockState);

    @Override
    protected MeshData renderXmi(BlockState blockState) {
        return MeshData.chunk().addXmiQuad(0, 1, 2, new Vector3f(0, 0, 0), xmiTexture(blockState));
    }

    @Override
    protected MeshData renderXpl(BlockState blockState) {
        return MeshData.chunk().addXplQuad(0, 1, 2, new Vector3f(0, 0, 0), xplTexture(blockState));
    }

    @Override
    protected MeshData renderYmi(BlockState blockState) {
        return MeshData.chunk().addYmiQuad(0, 1, 2, new Vector3f(0, 0, 0), ymiTexture(blockState));
    }

    @Override
    protected MeshData renderYpl(BlockState blockState) {
        return MeshData.chunk().addYplQuad(0, 1, 2, new Vector3f(0, 0, 0), yplTexture(blockState));
    }

    @Override
    protected MeshData renderZmi(BlockState blockState) {
        return MeshData.chunk().addZmiQuad(0, 1, 2, new Vector3f(0, 0, 0), zmiTexture(blockState));
    }

    @Override
    protected MeshData renderZpl(BlockState blockState) {
        return MeshData.chunk().addZplQuad(0, 1, 2, new Vector3f(0, 0, 0), zplTexture(blockState));
    }
}
