package p0nki.glmc4.client.render.block;

import org.joml.Vector3f;
import p0nki.glmc4.block.Block;
import p0nki.glmc4.block.BlockState;
import p0nki.glmc4.client.assets.AtlasPosition;
import p0nki.glmc4.client.gl.MeshData;

public abstract class BakedConstantSidedBlockRenderer extends BakedSidedBlockRenderer {

    public BakedConstantSidedBlockRenderer(Block block) {
        super(block);
    }

    protected abstract AtlasPosition xmiTexture(BlockState blockState);

    protected abstract AtlasPosition xplTexture(BlockState blockState);

    protected abstract AtlasPosition ymiTexture(BlockState blockState);

    protected abstract AtlasPosition yplTexture(BlockState blockState);

    protected abstract AtlasPosition zmiTexture(BlockState blockState);

    protected abstract AtlasPosition zplTexture(BlockState blockState);

    @Override
    protected MeshData renderXmi(BlockState blockState) {
        return MeshData.chunk().addXmiQuad(0, 1, new Vector3f(0, 0, 0), xmiTexture(blockState));
    }

    @Override
    protected MeshData renderXpl(BlockState blockState) {
        return MeshData.chunk().addXplQuad(0, 1, new Vector3f(0, 0, 0), xplTexture(blockState));
    }

    @Override
    protected MeshData renderYmi(BlockState blockState) {
        return MeshData.chunk().addYmiQuad(0, 1, new Vector3f(0, 0, 0), ymiTexture(blockState));
    }

    @Override
    protected MeshData renderYpl(BlockState blockState) {
        return MeshData.chunk().addYplQuad(0, 1, new Vector3f(0, 0, 0), yplTexture(blockState));
    }

    @Override
    protected MeshData renderZmi(BlockState blockState) {
        return MeshData.chunk().addZmiQuad(0, 1, new Vector3f(0, 0, 0), zmiTexture(blockState));
    }

    @Override
    protected MeshData renderZpl(BlockState blockState) {
        return MeshData.chunk().addZplQuad(0, 1, new Vector3f(0, 0, 0), zplTexture(blockState));
    }
}
