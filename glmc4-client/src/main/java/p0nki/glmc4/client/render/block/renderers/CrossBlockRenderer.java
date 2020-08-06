package p0nki.glmc4.client.render.block.renderers;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import p0nki.glmc4.block.Block;
import p0nki.glmc4.block.BlockState;
import p0nki.glmc4.client.assets.TextureQuad;
import p0nki.glmc4.client.render.MeshData;
import p0nki.glmc4.client.render.block.BlockRenderContext;
import p0nki.glmc4.client.render.block.BlockRenderer;
import p0nki.glmc4.utils.math.MathUtils;

public abstract class CrossBlockRenderer extends BlockRenderer {

    protected CrossBlockRenderer(Block block) {
        super(block);
    }

    protected abstract TextureQuad getQuad(BlockState blockState);

    @Override
    public MeshData render(BlockRenderContext context) {
        if (context.showXmi() || context.showXpl() || context.showYmi() || context.showYpl() || context.showZmi() || context.showZpl()) {
            TextureQuad quad = getQuad(context.getCur());
            MeshData data = MeshData.chunk();
            data.addXmiQuad(new Vector3f(0, 0, 0), quad, 1, 1, 1, 1, 1, 1);
            data.multiply4f(0, new Matrix4f().rotateY(MathUtils.PI / 4).scale((float) Math.sqrt(2)));
            MeshData newData = MeshData.chunk();
            newData.addXplQuad(new Vector3f(0, 0, 0), quad, 1, 1, 1, 1, 1, 1);
            newData.multiply4f(0, new Matrix4f().rotateY(-MathUtils.PI / 4).scale((float) Math.sqrt(2)));
            newData.multiply4f(0, new Matrix4f().translate(0, 0, -1));
            return data.append(newData);
        }
        return MeshData.chunk();
    }
}
