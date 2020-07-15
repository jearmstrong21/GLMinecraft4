package p0nki.glmc4.client.render.entity.renderers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import p0nki.glmc4.client.gl.Mesh;
import p0nki.glmc4.client.gl.MeshData;
import p0nki.glmc4.client.gl.Shader;
import p0nki.glmc4.client.gl.WorldRenderContext;
import p0nki.glmc4.client.render.entity.EntityRenderer;
import p0nki.glmc4.entity.TestEntity;

import java.util.List;

public class TestEntityRenderer extends EntityRenderer<TestEntity> {

    private static final Logger LOGGER = LogManager.getLogger();

    private Shader shader;
    private Mesh mesh;

    @Override
    public void initialize() {
        shader = new Shader("test");
        MeshData data = new MeshData();
        data.addBuffer(3);
        data.appendTriOffset(List.of(0, 1, 2, 1, 2, 3));
        data.addQuad(0, new Vector3f(0, 0, 0), new Vector3f(1, 0, 0), new Vector3f(0, 0, 1));
        mesh = new Mesh(data);
    }

    @Override
    protected void renderType(WorldRenderContext context, TestEntity entity) {
        shader.use();
        shader.set(context);
        shader.setMat4f("model", new Matrix4f().translate(entity.getPosition()));
        shader.set3f("color", entity.getColor());
        mesh.render();
    }

}
