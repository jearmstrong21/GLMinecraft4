package p0nki.glmc4.client.gl;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import p0nki.glmc4.client.GLMC4Client;
import p0nki.glmc4.client.render.WorldRenderContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL33.*;

public final class Shader {

    private final int shader;

    public Shader(String name) throws IOException {
        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader, Files.readString(Paths.get(GLMC4Client.resourcePath("shader"), name + ".vert")));
        glCompileShader(vertexShader);
        if (glGetShaderi(vertexShader, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Error compiling vertex shader\n" + glGetShaderInfoLog(vertexShader));
            throw new UnsupportedOperationException();
        }

        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader, Files.readString(Paths.get(GLMC4Client.resourcePath("shader"), name + ".frag")));
        glCompileShader(fragmentShader);
        if (glGetShaderi(fragmentShader, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Error compiling fragment shader\n" + glGetShaderInfoLog(fragmentShader));
            throw new UnsupportedOperationException();
        }

        shader = glCreateProgram();
        glAttachShader(shader, vertexShader);
        glAttachShader(shader, fragmentShader);
        glLinkProgram(shader);
        if (glGetProgrami(shader, GL_LINK_STATUS) == GL_FALSE) {
            System.err.println("Error linking shader\n" + glGetProgramInfoLog(shader));
            throw new UnsupportedOperationException();
        }
    }

    public static Shader create(String name) {
        try {
            return new Shader(name);
        } catch (IOException exception) {
            throw new AssertionError(exception);
        }
    }

    public void use() {
        glUseProgram(shader);
    }

    public void set(WorldRenderContext context) {
        setMat4f("perspective", context.getPerspective());
        setMat4f("view", context.getView());
    }

    public void setInt(String name, int value) {
        glUniform1i(glGetUniformLocation(shader, name), value);
    }

    public void setTexture(String name, Texture value, int unit) {
        setInt(name, unit);
        value.use(unit);
    }

    public void set3f(String name, Vector3f value) {
        glUniform3f(glGetUniformLocation(shader, name), value.x, value.y, value.z);
    }

    public void setMat4f(String name, Matrix4f value) {
        glUniformMatrix4fv(glGetUniformLocation(shader, name), false, BufferUtils.createFloatBuffer(16).put(value.get(new float[16])).flip());
    }

    public void setFloat(String name, float value) {
        glUniform1f(glGetUniformLocation(shader, name), value);
    }

}
