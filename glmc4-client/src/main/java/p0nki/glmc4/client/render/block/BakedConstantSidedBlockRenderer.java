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

    protected abstract AtlasPosition renderXmi();

    protected abstract AtlasPosition renderXpl();

    protected abstract AtlasPosition renderYmi();

    protected abstract AtlasPosition renderYpl();

    protected abstract AtlasPosition renderZmi();

    protected abstract AtlasPosition renderZpl();

    @Override
    protected MeshData renderXmi(BlockState blockState) {
        return MeshData.chunk().addXmiQuad(0, 1, new Vector3f(0, 0, 0), renderXmi());
    }

    @Override
    protected MeshData renderXpl(BlockState blockState) {
        return MeshData.chunk().addXplQuad(0, 1, new Vector3f(0, 0, 0), renderXpl());
    }

    @Override
    protected MeshData renderYmi(BlockState blockState) {
        return MeshData.chunk().addYmiQuad(0, 1, new Vector3f(0, 0, 0), renderYmi());
    }

    @Override
    protected MeshData renderYpl(BlockState blockState) {
        return MeshData.chunk().addYplQuad(0, 1, new Vector3f(0, 0, 0), renderYpl());
    }

    @Override
    protected MeshData renderZmi(BlockState blockState) {
        return MeshData.chunk().addZmiQuad(0, 1, new Vector3f(0, 0, 0), renderZmi());
    }

    @Override
    protected MeshData renderZpl(BlockState blockState) {
        return MeshData.chunk().addZplQuad(0, 1, new Vector3f(0, 0, 0), renderZpl());
    }
}
