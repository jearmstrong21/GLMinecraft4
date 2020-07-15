package p0nki.glmc4.client.render.entity.renderers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import p0nki.glmc4.client.assets.AtlasPosition;
import p0nki.glmc4.client.assets.ResourceLocation;
import p0nki.glmc4.client.gl.*;
import p0nki.glmc4.client.render.entity.EntityRenderer;
import p0nki.glmc4.entity.TestEntity;

public class TestEntityRenderer extends EntityRenderer<TestEntity> {

    private static final Logger LOGGER = LogManager.getLogger();

    private Texture texture;
    private Shader shader;
    private Mesh mesh;

    @Override
    public void initialize() {
        texture = new Texture(new ResourceLocation("entity/steve.png"));
        shader = new Shader("test");
        MeshData data = new MeshData();
        data.addBuffer(3);
        data.addBuffer(2);

        AtlasPosition front = new AtlasPosition(8, 8, 8, 8, 64, 64);
        AtlasPosition back = new AtlasPosition(24, 8, 8, 8, 64, 64);
        AtlasPosition top = new AtlasPosition(8, 0, 8, 8, 64, 64);
        AtlasPosition bottom = new AtlasPosition(16, 0, 8, 8, 64, 64);
        AtlasPosition left = new AtlasPosition(16, 8, 8, 8, 64, 64);
        AtlasPosition right = new AtlasPosition(0, 8, 8, 8, 64, 64);

        data.addXmiQuad(0, 1, new Vector3f(0), left);
        data.addXplQuad(0, 1, new Vector3f(0), right);

        data.addYmiQuad(0, 1, new Vector3f(0), bottom);
        data.addYplQuad(0, 1, new Vector3f(0), top);

        data.addZmiQuad(0, 1, new Vector3f(0), front);
        data.addZplQuad(0, 1, new Vector3f(0), back);

        mesh = new Mesh(data);
    }

    @Override
    protected void renderType(WorldRenderContext context, TestEntity entity) {
        shader.use();
        shader.set(context);
        shader.setMat4f("model", new Matrix4f().translate(entity.getPosition()));
        shader.set3f("color", entity.getColor());
        shader.setTexture("tex", texture, 0);
        mesh.render();
    }

}
