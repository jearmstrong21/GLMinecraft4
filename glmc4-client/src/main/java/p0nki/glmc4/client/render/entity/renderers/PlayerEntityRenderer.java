package p0nki.glmc4.client.render.entity.renderers;

import org.joml.Matrix4f;
import p0nki.glmc4.client.ClientSettings;
import p0nki.glmc4.client.GLMC4Client;
import p0nki.glmc4.client.gl.Mesh;
import p0nki.glmc4.client.gl.Shader;
import p0nki.glmc4.client.gl.Texture;
import p0nki.glmc4.client.render.MatrixStack;
import p0nki.glmc4.client.render.WorldRenderContext;
import p0nki.glmc4.client.render.entity.EntityRenderer;
import p0nki.glmc4.entity.PlayerEntity;

import java.nio.file.Path;

public class PlayerEntityRenderer extends EntityRenderer<PlayerEntity> {

    private Shader shader;
    private Texture texture;
    private Mesh leftLeg;
    private Mesh rightLeg;
    private Mesh leftArm;
    private Mesh rightArm;
    private Mesh head;
    private Mesh body;

    @Override
    public void initialize() {
        shader = Shader.create("entity");
        texture = new Texture(Path.of(GLMC4Client.resourcePath("entity"), "steve.png"));
        head = new Mesh(createCube(0, 16, 8, 8, 8, 64, 64, true).multiply4f(0, new Matrix4f().scale(8.0F / 16.0F)));
        body = new Mesh(createCube(16, 32, 8, 12, 4, 64, 64, false).multiply4f(0, new Matrix4f().scale(8.0F / 16.0F, 12.0F / 16.0F, 4.0F / 16.0F)));
        leftLeg = new Mesh(createCube(0, 32, 4, 12, 4, 64, 64, false).multiply4f(0, new Matrix4f().scale(4.0F / 16.0F, 12.0F / 16.0F, 4.0F / 16.0F)));
        rightLeg = new Mesh(createCube(16, 0, 4, 12, 4, 64, 64, false).multiply4f(0, new Matrix4f().scale(4.0F / 16.0F, 12.0F / 16.0F, 4.0F / 16.0F)));
        leftArm = new Mesh(createCube(40, 32, 4, 12, 4, 64, 64, false).multiply4f(0, new Matrix4f().scale(4.0F / 16.0F, 12.0F / 16.0F, 4.0F / 16.0F)));
        rightArm = new Mesh(createCube(32, 0, 4, 12, 4, 64, 64, false).multiply4f(0, new Matrix4f().scale(4.0F / 16.0F, 12.0F / 16.0F, 4.0F / 16.0F)));
    }

    @Override
    protected void renderType(WorldRenderContext context, PlayerEntity entity) {
        shader.use();
        shader.set(context);
        shader.setTexture("tex", texture, 0);

        MatrixStack stack = new MatrixStack(model -> shader.setMat4f("model", model));
        stack.translate(entity.getPosition());
        stack.translate(entity.getSize().x / 2, 0, entity.getSize().z / 2);

        stack.push();
        stack.lookAt(entity.getFacingTowards());
        stack.push();
        stack.translate(0, 0.75F, 0);
        body.triangles();
        stack.pop();
        stack.push();
        stack.translate(2.0F / 16.0F, 0, 0);
        leftLeg.triangles();
        stack.pop();
        stack.push();
        stack.translate(-2.0F / 16.0F, 0, 0);
        rightLeg.triangles();
        stack.pop();
        stack.push();
        stack.translate(0, 0.75F, 0);
        stack.push();
        stack.translate(6.0F / 16.0F, 0, 0);
        leftArm.triangles();
        stack.pop();
        stack.push();
        stack.translate(-6.0F / 16.0F, 0, 0);
        rightArm.triangles();
        stack.pop();
        stack.pop();
        stack.pop();
        stack.push();
        stack.translate(0, 1.75F, 0);
        stack.lookAt(entity.getLookingAt());
        if (!ClientSettings.FIRST_PERSON && entity.getUuid().equals(GLMC4Client.getThisEntity().getUuid()))
            head.triangles();
        stack.pop();
    }

}
