package p0nki.glmc4.client.gl;

import org.joml.Vector2i;
import p0nki.glmc4.client.assets.Location;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.stb.STBImage.stbi_load;
import static org.lwjgl.stb.STBImage.stbi_set_flip_vertically_on_load;

public class Texture {

    private final int id;
    private final int width;
    private final int height;

    public Texture(Location location) {
        id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, id);
        stbi_set_flip_vertically_on_load(true);
        int[] w = new int[1];
        int[] h = new int[1];
        int[] comps = new int[1];
        ByteBuffer data = stbi_load(location.asFile().getAbsolutePath(), w, h, comps, 0);
        if (data == null)
            throw new RuntimeException("Failed to load image " + location.asFile().getAbsolutePath());
        int format = GL_RGB;
        if (comps[0] == 4) format = GL_RGBA;
        width = w[0];
        height = h[0];
        glTexImage2D(GL_TEXTURE_2D, 0, format, width, height, 0, format, GL_UNSIGNED_BYTE, data);
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void use(int unit) {
        glActiveTexture(GL_TEXTURE0 + unit);
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Vector2i getSize() {
        return new Vector2i(width, height);
    }

}

