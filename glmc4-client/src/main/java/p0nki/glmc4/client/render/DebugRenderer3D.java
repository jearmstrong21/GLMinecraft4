package p0nki.glmc4.client.render;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import p0nki.glmc4.client.gl.Mesh;
import p0nki.glmc4.client.gl.Shader;
import p0nki.glmc4.utils.ListUtils;

import java.util.List;

public class DebugRenderer3D {

    private Shader shader;
    private Mesh cube;

    public DebugRenderer3D() {
        shader = Shader.create("debug3d");
        cube = new Mesh(new MeshData()
                .addBuffer(3)
                .appendBuffer(0, ListUtils.floatList(
                        0, 0, 0, 1, 0, 0,
                        0, 0, 0, 0, 1, 0,
                        0, 0, 0, 0, 0, 1,

                        1, 0, 0, 1, 0, 1,
                        1, 0, 0, 1, 1, 0,

                        0, 1, 0, 0, 1, 1,
                        0, 1, 0, 1, 1, 0,

                        0, 0, 1, 0, 1, 1,
                        0, 0, 1, 1, 0, 1,

                        0, 1, 1, 1, 1, 1,
                        1, 0, 1, 1, 1, 1,
                        1, 1, 0, 1, 1, 1
                ))
                .appendTri(List.of(
                        0, 1,
                        2, 3,
                        4, 5,
                        6, 7,
                        8, 9,
                        10, 11,
                        12, 13,
                        14, 15,
                        16, 17,
                        18, 19,
                        20, 21,
                        22, 23
                )));
    }

    public void renderCube(WorldRenderContext context, Vector3f color, Vector3f position, Vector3f size) {
        shader.use();
        shader.set(context);
        shader.set3f("col", color);
        shader.setMat4f("model", new Matrix4f().translate(position).scale(size));
        cube.lines();
    }
}
