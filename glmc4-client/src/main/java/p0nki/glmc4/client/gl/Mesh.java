package p0nki.glmc4.client.gl;

import org.apache.commons.lang3.ArrayUtils;

import static org.lwjgl.opengl.GL33.*;

public class Mesh {

    private final int vao;
    private final int ebo;
    private final int size;

    public Mesh(MeshData data) {
        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        ebo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, ArrayUtils.toPrimitive(data.getTri().toArray(Integer[]::new)), GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

        size = data.getTri().size();

        for (int i = 0; i < data.size(); i++) {
            int vbo = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            glBufferData(GL_ARRAY_BUFFER, ArrayUtils.toPrimitive(data.getData().get(i).toArray(Double[]::new)), GL_STATIC_DRAW);
            glVertexAttribPointer(i, data.getSizes().get(i), GL_DOUBLE, false, 0, 0);
            glEnableVertexAttribArray(i);
        }
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glBindVertexArray(0);
    }

    public void render() {
        glBindVertexArray(vao);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glDrawElements(GL_TRIANGLES, size, GL_UNSIGNED_INT, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

}
