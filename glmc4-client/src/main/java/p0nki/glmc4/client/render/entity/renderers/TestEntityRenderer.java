package p0nki.glmc4.client.render.entity.renderers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Matrix4f;
import p0nki.glmc4.client.assets.ResourceLocation;
import p0nki.glmc4.client.gl.Mesh;
import p0nki.glmc4.client.gl.Shader;
import p0nki.glmc4.client.gl.Texture;
import p0nki.glmc4.client.gl.WorldRenderContext;
import p0nki.glmc4.client.render.entity.EntityRenderer;
import p0nki.glmc4.entity.TestEntity;

public class TestEntityRenderer extends EntityRenderer<TestEntity> {

    private static final Logger LOGGER = LogManager.getLogger();

    private Texture texture;
    private Shader shader;
    private Mesh head;
    private Mesh body;

    @Override
    public void initialize() {
        texture = new Texture(new ResourceLocation("entity/cow.png"));
        shader = Shader.create("test");

        head = new Mesh(createCube(0, 14, 8, 8, 6, 64, 32).mult4(0, new Matrix4f().scale(8, 8, 6).scale(2.0F / 8.0F)));
        body = new Mesh(createCube(18, 32, 12, 18, 10, 64, 32).mult4(0, new Matrix4f().scale(12, 18, 10).scale(3.0F / 18.0F)));
    }

    @Override
    protected void renderType(WorldRenderContext context, TestEntity entity) {
//        LOGGER.trace("Rendering test entity at {} {} {}", entity.getPosition().x, entity.getPosition().y, entity.getPosition().z);
        shader.use();
        shader.set(context);
        shader.setMat4f("model", new Matrix4f().translate(entity.getPosition()));
        shader.set3f("color", entity.getColor());
        shader.setTexture("tex", texture, 0);
        body.render();
    }

}
