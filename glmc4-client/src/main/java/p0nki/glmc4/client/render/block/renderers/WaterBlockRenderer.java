package p0nki.glmc4.client.render.block.renderers;

import org.joml.Vector3f;
import org.joml.Vector3i;
import p0nki.glmc4.block.BlockState;
import p0nki.glmc4.block.Blocks;
import p0nki.glmc4.block.blocks.WaterBlock;
import p0nki.glmc4.client.assets.AtlasPosition;
import p0nki.glmc4.client.assets.TextureQuad;
import p0nki.glmc4.client.render.MeshData;
import p0nki.glmc4.client.render.block.NoContextSidedBlockRenderer;
import p0nki.glmc4.client.render.block.RenderLayer;
import p0nki.glmc4.utils.Identifier;

public class WaterBlockRenderer extends NoContextSidedBlockRenderer {

    private final static Vector3f COLOR = new Vector3f(0.4F, 0.6F, 1.0F);

    public WaterBlockRenderer() {
        super(Blocks.WATER, RenderLayer.TRANSPARENT);
    }

    private static float getHeight(BlockState blockState) {
        return blockState.get(WaterBlock.HEIGHT) / 16.0F;
    }

    private static TextureQuad getTextureQuad(BlockState blockState) {
        AtlasPosition atlasPosition = AtlasPosition.block(new Identifier("minecraft:water"));
        if (blockState != null) atlasPosition.h *= getHeight(blockState);
        return atlasPosition.layer(COLOR).quad();
    }

    @Override
    protected MeshData renderXmi(Vector3i blockPos, BlockState blockState) {
        return MeshData.chunk().addXmiQuad(blockPos, new Vector3f(0), getTextureQuad(blockState), getHeight(blockState));
    }

    @Override
    protected MeshData renderXpl(Vector3i blockPos, BlockState blockState) {
        return MeshData.chunk().addXplQuad(blockPos, new Vector3f(0), getTextureQuad(blockState), getHeight(blockState));
    }

    @Override
    protected MeshData renderYmi(Vector3i blockPos, BlockState blockState) {
        return MeshData.chunk().addYmiQuad(blockPos, new Vector3f(0), getTextureQuad(null));
    }

    @Override
    protected MeshData renderYpl(Vector3i blockPos, BlockState blockState) {
        return MeshData.chunk().addYplQuad(blockPos, new Vector3f(0, getHeight(blockState) - 1, 0), getTextureQuad(null));
    }

    @Override
    protected MeshData renderZmi(Vector3i blockPos, BlockState blockState) {
        return MeshData.chunk().addZmiQuad(blockPos, new Vector3f(0), getTextureQuad(blockState), getHeight(blockState));
    }

    @Override
    protected MeshData renderZpl(Vector3i blockPos, BlockState blockState) {
        return MeshData.chunk().addZplQuad(blockPos, new Vector3f(0), getTextureQuad(blockState), getHeight(blockState));
    }
}
