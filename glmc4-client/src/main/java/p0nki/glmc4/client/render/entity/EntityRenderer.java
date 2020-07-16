package p0nki.glmc4.client.render.entity;

import org.joml.Vector3f;
import p0nki.glmc4.client.assets.AtlasPosition;
import p0nki.glmc4.client.gl.MeshData;
import p0nki.glmc4.client.gl.WorldRenderContext;
import p0nki.glmc4.entity.Entity;

public abstract class EntityRenderer<E extends Entity> {

    public static MeshData createCube(int x, int y, int width, int height, int depth, int texWidth, int texHeight) {
        MeshData data = new MeshData();
        data.addBuffer(3);
        data.addBuffer(2);

        AtlasPosition left = new AtlasPosition(x + depth + width, y - height, depth, height, texWidth, texHeight);
        AtlasPosition right = new AtlasPosition(x, y - height, depth, height, texWidth, texHeight);
        AtlasPosition bottom = new AtlasPosition(x + depth + width, y - height - depth, depth, width, texWidth, texHeight);
        AtlasPosition top = new AtlasPosition(x + depth, y - height - depth, depth, width, texWidth, texHeight);
        AtlasPosition front = new AtlasPosition(x + depth, y - height, width, height, texWidth, texHeight);
        AtlasPosition back = new AtlasPosition(x + depth + width + depth, y - height, width, height, texWidth, texHeight);

        data.addXmiQuad(0, 1, new Vector3f(0), left);
        data.addXplQuad(0, 1, new Vector3f(0), right);

        data.addYmiQuad(0, 1, new Vector3f(0), bottom);
        data.addYplQuad(0, 1, new Vector3f(0), top);

        data.addZmiQuad(0, 1, new Vector3f(0), front);
        data.addZplQuad(0, 1, new Vector3f(0), back);

        return data;
    }

    public abstract void initialize();

    protected abstract void renderType(WorldRenderContext context, E entity);

    @SuppressWarnings("unchecked") // :sunglasses:
    public final void render(WorldRenderContext context, Entity entity) {
        renderType(context, (E) entity);
    }

}
