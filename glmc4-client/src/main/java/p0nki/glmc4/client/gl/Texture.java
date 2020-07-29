package p0nki.glmc4.client.gl;

import java.nio.ByteBuffer;
import java.nio.file.Path;

import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.stb.STBImage.stbi_load;
import static org.lwjgl.stb.STBImage.stbi_set_flip_vertically_on_load;

public class Texture {

    private final int id;

    public Texture(Path path) {
        id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, id);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        stbi_set_flip_vertically_on_load(false);
        int[] w = new int[1];
        int[] h = new int[1];
        int[] comps = new int[1];
        ByteBuffer data = stbi_load(path.toString(), w, h, comps, 0);
        if (data == null) {
            throw new AssertionError("Failed to load image: " + path.toString());
        }
        int format = GL_RGB;
        if (comps[0] == 4) format = GL_RGBA;
        glTexImage2D(GL_TEXTURE_2D, 0, format, w[0], h[0], 0, format, GL_UNSIGNED_BYTE, data);
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void use(int unit) {
        glActiveTexture(GL_TEXTURE0 + unit);
        glBindTexture(GL_TEXTURE_2D, id);
    }

}