package p0nki.glmc4.client.gl;

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

    public void setTexture(String name, Texture texture, int unit) {
        setInt(name, unit);
        texture.use(unit);
    }

}
