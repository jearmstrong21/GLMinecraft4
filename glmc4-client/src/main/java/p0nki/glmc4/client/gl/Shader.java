package p0nki.glmc4.client.gl;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import p0nki.glmc4.client.assets.ResourceLocation;

import static org.lwjgl.opengl.GL33.*;

public class Shader {

    private final int shader;

    public Shader(String name) {
        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader, new ResourceLocation("shader/" + name + ".vert").loadText());
        glCompileShader(vertexShader);
        if (glGetShaderi(vertexShader, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Error compiling vertex shader\n" + glGetShaderInfoLog(vertexShader));
            throw new UnsupportedOperationException();
        }

        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader, new ResourceLocation("shader/" + name + ".frag").loadText());
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

    public void use() {
        glUseProgram(shader);
    }

    public void setInt(String name, int value) {
        glUniform1i(glGetUniformLocation(shader, name), value);
    }

    public void setTexture(String name, Texture value, int unit) {
        setInt(name, unit);
        value.use(unit);
    }

    public void setMat4f(String name, Matrix4f value) {
        glUniformMatrix4fv(glGetUniformLocation(shader, name), false, BufferUtils.createFloatBuffer(16).put(value.get(new float[16])).flip());
    }

    public void setFloat(String name, float value) {
        glUniform1f(glGetUniformLocation(shader, name), value);
    }

}
