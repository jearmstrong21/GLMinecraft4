package p0nki.glmc4.client.render.entity;

import org.joml.Vector3f;
import p0nki.glmc4.client.ClientSettings;
import p0nki.glmc4.client.GLMC4Client;
import p0nki.glmc4.client.assets.AtlasPosition;
import p0nki.glmc4.client.render.MeshData;
import p0nki.glmc4.client.render.WorldRenderContext;
import p0nki.glmc4.entity.Entity;
import p0nki.glmc4.registry.Registrable;
import p0nki.glmc4.registry.Registry;

public abstract class EntityRenderer<E extends Entity> extends Registrable<EntityRenderer<?>> {

    @Override
    public Registry<EntityRenderer<?>> getRegistry() {
        return EntityRenderers.REGISTRY;
    }

    @Override
    public EntityRenderer<?> getValue() {
        return this;
    }

    public static MeshData createCube(int x, int y, int width, int height, int depth, int texWidth, int texHeight, boolean centerY) {
        MeshData data = new MeshData();
        data.addBuffer(3);
        data.addBuffer(2);

        AtlasPosition left = new AtlasPosition(x + depth + width, y - height, depth, height, texWidth, texHeight);
        AtlasPosition right = new AtlasPosition(x, y - height, depth, height, texWidth, texHeight);
        AtlasPosition bottom = new AtlasPosition(x + depth + width, y - height - depth, width, depth, texWidth, texHeight);
        AtlasPosition top = new AtlasPosition(x + depth, y - height - depth, width, depth, texWidth, texHeight);
        AtlasPosition front = new AtlasPosition(x + depth, y - height, width, height, texWidth, texHeight);
        AtlasPosition back = new AtlasPosition(x + depth + width + depth, y - height, width, height, texWidth, texHeight);

        data.addXmiQuad(0, 1, new Vector3f(0), right);
        data.addXplQuad(0, 1, new Vector3f(0), left);

        data.addYmiQuad(0, 1, new Vector3f(0), bottom);
        data.addYplQuad(0, 1, new Vector3f(0), top);

        data.addZmiQuad(0, 1, new Vector3f(0), back);
        data.addZplQuad(0, 1, new Vector3f(0), front);

        for (int i = 0; i < data.getBuffer(0).size(); i += 3) {
            data.getBuffer(0).set(i, data.getBuffer(0).get(i) - 0.5F);
            if (centerY) data.getBuffer(0).set(i + 1, data.getBuffer(0).get(i + 1) - 0.5F);
            data.getBuffer(0).set(i + 2, data.getBuffer(0).get(i + 2) - 0.5F);
        }

        return data;
    }

    public abstract void initialize();

    protected abstract void renderType(WorldRenderContext context, E entity);

    @SuppressWarnings("unchecked") // :sunglasses:
    public final void render(WorldRenderContext context, Entity entity) {
        renderType(context, (E) entity);
        if (ClientSettings.RENDER_HITBOXES) {
            GLMC4Client.debugRenderer3D.renderCube(context, ClientSettings.HITBOX_COLOR,
                    entity.getPosition(),
                    entity.getSize()
            );
//            entity.listBlockPos().forEach(blockPos -> GLMC4Client.debugRenderer3D.renderCube(context,
//                    new Vector3f(0, 0, 1), // color
//                    new Vector3f(blockPos), // pos
//                    new Vector3f(1)) // size
//            );
        }
    }

}
