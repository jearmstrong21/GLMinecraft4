package p0nki.glmc4.client.render;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import p0nki.glmc4.client.assets.AtlasPosition;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MeshData {

    private final List<List<Float>> data;
    private final List<Integer> sizes;
    private final List<Integer> tri;
    private int maxReferredVertex = 0;

    public MeshData() {
        data = new ArrayList<>();
        sizes = new ArrayList<>();
        tri = new ArrayList<>();
    }

    public static MeshData chunk() {
        return new MeshData().addBuffer(3).addBuffer(2);
    }

    public int size() {
        return sizes.size();
    }

    public List<List<Float>> getData() {
        return data;
    }

    public List<Integer> getSizes() {
        return sizes;
    }

    public List<Integer> getTri() {
        return tri;
    }

    public MeshData addBuffer(int size) {
        data.add(new ArrayList<>());
        sizes.add(size);
        return this;
    }

    public MeshData mult4(int buffer, Matrix4f matrix) {
        if (sizes.get(buffer) != 3) throw new AssertionError("Invalid buffer size");
        for (int i = 0; i < data.get(buffer).size(); i += 3) {
            Vector4f original = new Vector4f(data.get(buffer).get(i), data.get(buffer).get(i + 1), data.get(buffer).get(i + 2), 1);
            original.mul(matrix);
            data.get(buffer).set(i, original.x);
            data.get(buffer).set(i + 1, original.y);
            data.get(buffer).set(i + 2, original.z);
        }
        return this;
    }

    public MeshData append(MeshData other) {
        Objects.requireNonNull(other);
        if (other.data.size() != data.size()) throw new AssertionError("Invalid data");
        for (int i = 0; i < other.data.size(); i++) {
            if (sizes.get(i) != (int) other.sizes.get(i)) throw new AssertionError("Invalid data");
        }
        appendTriOffset(other.tri);
        for (int i = 0; i < other.data.size(); i++) {
            data.get(i).addAll(other.data.get(i));
        }
        return this;
    }

    public MeshData appendBuffer(int buffer, List<Float> data) {
        this.data.get(buffer).addAll(data);
        return this;
    }

    public MeshData appendBuffer2f(int buffer, List<Vector2f> data) {
        for (Vector2f v : data) {
            this.data.get(buffer).add(v.x);
            this.data.get(buffer).add(v.y);
        }
        return this;
    }

    public MeshData addQuad(int buffer, Vector2f o, Vector2f a, Vector2f b) {
        appendBuffer2f(buffer, List.of(o, new Vector2f(o).add(a), new Vector2f(o).add(b), new Vector2f(o).add(a).add(b)));
        return this;
    }

    public MeshData addQuad(int buffer, AtlasPosition atlasPosition) {
        addQuad(buffer, new Vector2f(atlasPosition.x, atlasPosition.y), new Vector2f(atlasPosition.w, 0), new Vector2f(0, atlasPosition.h));
        return this;
    }

    public MeshData addXmiQuad(int posBuffer, int uvBuffer, Vector3f origin, AtlasPosition atlasPosition) {
        appendTriOffset(List.of(0, 1, 2, 1, 2, 3));
        addQuad(posBuffer, new Vector3f(0, 1, 0).add(origin), new Vector3f(0, 0, 1), new Vector3f(0, -1, 0));
        addQuad(uvBuffer, atlasPosition);
        return this;
    }

    public MeshData addXplQuad(int posBuffer, int uvBuffer, Vector3f origin, AtlasPosition atlasPosition) {
        appendTriOffset(List.of(0, 1, 2, 1, 2, 3));
        addQuad(posBuffer, new Vector3f(1, 1, 1).add(origin), new Vector3f(0, 0, -1), new Vector3f(0, -1, 0));
        addQuad(uvBuffer, atlasPosition);
        return this;
    }

    public MeshData addYmiQuad(int posBuffer, int uvBuffer, Vector3f origin, AtlasPosition atlasPosition) {
        appendTriOffset(List.of(0, 1, 2, 1, 2, 3));
        addQuad(posBuffer, new Vector3f(0, 0, 0).add(origin), new Vector3f(1, 0, 0), new Vector3f(0, 0, 1));
        addQuad(uvBuffer, atlasPosition);
        return this;
    }

    public MeshData addYplQuad(int posBuffer, int uvBuffer, Vector3f origin, AtlasPosition atlasPosition) {
        appendTriOffset(List.of(0, 1, 2, 1, 2, 3));
        addQuad(posBuffer, new Vector3f(0, 1, 0).add(origin), new Vector3f(1, 0, 0), new Vector3f(0, 0, 1));
        addQuad(uvBuffer, atlasPosition);
        return this;
    }

    public MeshData addZmiQuad(int posBuffer, int uvBuffer, Vector3f origin, AtlasPosition atlasPosition) {
        appendTriOffset(List.of(0, 1, 2, 1, 2, 3));
        addQuad(posBuffer, new Vector3f(1, 1, 0).add(origin), new Vector3f(-1, 0, 0), new Vector3f(0, -1, 0));
        addQuad(uvBuffer, atlasPosition);
        return this;
    }

    public MeshData addZplQuad(int posBuffer, int uvBuffer, Vector3f origin, AtlasPosition atlasPosition) {
        appendTriOffset(List.of(0, 1, 2, 1, 2, 3));
        addQuad(posBuffer, new Vector3f(0, 1, 1).add(origin), new Vector3f(1, 0, 0), new Vector3f(0, -1, 0));
        addQuad(uvBuffer, atlasPosition);
        return this;
    }

    public MeshData addQuad(int buffer, Vector3f o, Vector3f a, Vector3f b) {
        appendBuffer3f(buffer, List.of(o, new Vector3f(o).add(a), new Vector3f(o).add(b), new Vector3f(o).add(a).add(b)));
        return this;
    }

    public MeshData appendBuffer3f(int buffer, List<Vector3f> data) {
        for (Vector3f v : data) {
            this.data.get(buffer).add(v.x);
            this.data.get(buffer).add(v.y);
            this.data.get(buffer).add(v.z);
        }
        return this;
    }

    public MeshData appendTri(List<Integer> tri) {
        this.tri.addAll(tri);
        tri.forEach(x -> maxReferredVertex = Math.max(x + 1, maxReferredVertex));
        return this;
    }

    public MeshData appendTriOffset(List<Integer> tri) {
        appendTri(tri.stream().map(x -> x + maxReferredVertex).collect(Collectors.toList()));
        return this;
    }
}
