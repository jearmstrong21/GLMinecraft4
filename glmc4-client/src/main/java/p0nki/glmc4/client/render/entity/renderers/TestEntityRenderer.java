package p0nki.glmc4.client.render.entity.renderers;

import org.joml.Matrix4f;
import p0nki.glmc4.client.GLMC4Client;
import p0nki.glmc4.client.gl.Mesh;
import p0nki.glmc4.client.gl.Shader;
import p0nki.glmc4.client.gl.Texture;
import p0nki.glmc4.client.render.WorldRenderContext;
import p0nki.glmc4.client.render.entity.EntityRenderer;
import p0nki.glmc4.entity.TestEntity;

import java.nio.file.Path;

public class TestEntityRenderer extends EntityRenderer<TestEntity> {

    private Texture texture;
    private Shader shader;
    private Mesh body;

    @Override
    public void initialize() {
        texture = new Texture(Path.of(GLMC4Client.resourcePath("entity"), "cow.png"));
        shader = Shader.create("entity");

        body = new Mesh(createCube(18, 32, 12, 18, 10, 64, 32, false).multiply4f(0, new Matrix4f().scale(12, 18, 10).scale(3.0F / 18.0F)));
    }

    @Override
    protected void renderType(WorldRenderContext context, TestEntity entity) {
        shader.use();
        shader.set(context);
        shader.setMat4f("model", new Matrix4f().translate(entity.getPosition()));
        shader.set3f("color", entity.getColor());
        shader.setTexture("tex", texture, 0);
        body.triangles();
    }

}
