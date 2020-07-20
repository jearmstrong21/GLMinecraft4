package p0nki.glmc4.client.render;

import p0nki.glmc4.client.GLMC4Client;
import p0nki.glmc4.client.gl.Mesh;
import p0nki.glmc4.client.gl.Shader;
import p0nki.glmc4.client.gl.Texture;
import p0nki.glmc4.utils.ListUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TextRenderer {

    private final Shader shader;
    private final Texture texture;
    private final Mesh quad;
    private final Map<Character, Integer> charStart = new HashMap<>();
    private final Map<Character, Integer> charSize = new HashMap<>();

    public TextRenderer() {
        shader = Shader.create("text");
        texture = new Texture(Paths.get(GLMC4Client.resourcePath("font.png")));
        quad = new Mesh(new MeshData()
                .addBuffer(2)
                .appendBuffer(0, ListUtils.floatList(0, 0, 1, 0, 0, 1, 1, 1))
                .appendTri(List.of(0, 1, 2, 1, 2, 3)));

        try {
            BufferedImage image = ImageIO.read(Paths.get(GLMC4Client.resourcePath("font.png")).toFile());
            if (image.getWidth() != 160) throw new AssertionError(image.getWidth());
            if (image.getHeight() != 160) throw new AssertionError(image.getHeight());
            for (int x = 0; x < 16; x++) {
                for (int y = 0; y < 16; y++) {
                    int minX = 16;
                    int maxX = 0;
                    for (int i = 0; i < 10; i++) {
                        for (int j = 0; j < 10; j++) {
                            Color color = new Color(image.getRGB(i + x * 10, j + y * 10));
                            if (color.getRed() > 255 / 2) {
                                minX = Math.min(minX, i);
                                maxX = Math.max(maxX, i);
                            }
                        }
                    }
                    charStart.put((char) (y * 16 + x), minX);
                    charSize.put((char) (y * 16 + x), maxX - minX + 1);
                }
            }
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
    }

    public void renderString(float x, float y, float size, String text) {
        shader.use();
        shader.setTexture("tex", texture, 0);
        float x0 = x;
        for (char c : text.toCharArray()) {
            if (c == '\n') {
                y -= size;
                x = x0;
            } else if (c == ' ') {
                x += size * 0.25F;
            } else {
                shader.setFloat("x", x);
                shader.setFloat("y", y);
                shader.setFloat("w", charSize.get(c) / 10.0F * size);
                shader.setFloat("h", size);
                int uvx = c % 16;
                int uvy = c / 16;
                shader.setFloat("uvx", uvx / 16.0F + charStart.get(c) / 160.0F);
                shader.setFloat("uvy", uvy / 16.0F);
                shader.setFloat("uvw", charSize.get(c) / 160.F);
                shader.setFloat("uvh", 1.0F / 16.0F);
                quad.triangles();
                x += size * charSize.get(c) / 10.0F;
            }
        }
    }

}
