package p0nki.glmc4.client.render.entity.renderers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import p0nki.glmc4.client.GLMC4Client;
import p0nki.glmc4.client.gl.Mesh;
import p0nki.glmc4.client.gl.Shader;
import p0nki.glmc4.client.gl.Texture;
import p0nki.glmc4.client.render.WorldRenderContext;
import p0nki.glmc4.client.render.entity.EntityRenderer;
import p0nki.glmc4.entity.PlayerEntity;

import java.nio.file.Path;

public class PlayerEntityRenderer extends EntityRenderer<PlayerEntity> {

    private static final Logger LOGGER = LogManager.getLogger();

    private Shader shader;
    private Texture texture;
    private Mesh head;
    private Mesh body;

    @Override
    public void initialize() {
        shader = Shader.create("entity");
        texture = new Texture(Path.of(GLMC4Client.resourcePath("entity"), "steve.png"));
        head = new Mesh(createCube(0, 16, 8, 8, 8, 64, 64, true).mult4(0, new Matrix4f().scale(8.0F / 16.0F)));
        body = new Mesh(createCube(16, 32, 8, 12, 4, 64, 64, false).mult4(0, new Matrix4f().scale(8.0F / 16.0F, 12.0F / 16.0F, 4.0F / 16.0F)));
    }

    @Override
    protected void renderType(WorldRenderContext context, PlayerEntity entity, Vector3f extrapolatedPosition) {
        shader.use();
        shader.set(context);
        shader.setTexture("tex", texture, 0);
        Matrix4f baseMatrix = new Matrix4f().translate(extrapolatedPosition);
        Matrix4f bodyMatrix = new Matrix4f(baseMatrix).mul(new Matrix4f().lookAlong(entity.getFacingTowards(), new Vector3f(0, 1, 0)));
        Matrix4f headMatrix = new Matrix4f(baseMatrix).mul(new Matrix4f().lookAlong(entity.getLookingAt(), new Vector3f(0, 1, 0)));
        shader.setMat4f("model", new Matrix4f(headMatrix).translate(0, 1.0F, 0));
        head.triangles();
        shader.setMat4f("model", new Matrix4f(bodyMatrix).translate(0, 0, 0));
        body.triangles();
    }

}
