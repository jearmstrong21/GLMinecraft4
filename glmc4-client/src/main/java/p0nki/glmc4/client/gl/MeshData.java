package p0nki.glmc4.client.gl;

import org.joml.Vector2f;
import org.joml.Vector3f;
import p0nki.glmc4.client.assets.AtlasPosition;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MeshData {

    private List<List<Float>> data;
    private List<Integer> sizes;
    private List<Integer> tri;
    private int maxReferredVertex = 0;

    public MeshData() {
        data = new ArrayList<>();
        sizes = new ArrayList<>();
        tri = new ArrayList<>();
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

    public void addBuffer(int size) {
        data.add(new ArrayList<>());
        sizes.add(size);
    }

    public void appendBuffer(int buffer, List<Float> data) {
        this.data.get(buffer).addAll(data);
    }

    public void appendBuffer2f(int buffer, List<Vector2f> data) {
        for (Vector2f v : data) {
            this.data.get(buffer).add(v.x);
            this.data.get(buffer).add(v.y);
        }
    }

    public void addQuad(int buffer, Vector2f o, Vector2f a, Vector2f b) {
        appendBuffer2f(buffer, List.of(o, new Vector2f(o).add(a), new Vector2f(o).add(b), new Vector2f(o).add(a).add(b)));
    }

    public void addQuad(int buffer, AtlasPosition atlasPosition) {
        addQuad(buffer, new Vector2f(atlasPosition.x, atlasPosition.y), new Vector2f(atlasPosition.w, 0), new Vector2f(0, atlasPosition.h));
    }

    public void addXmiQuad(int posBuffer, int uvBuffer, Vector3f origin, AtlasPosition atlasPosition) {
        appendTriOffset(List.of(0, 1, 2, 1, 2, 3));
        addQuad(posBuffer, new Vector3f(0, 1, 0).add(origin), new Vector3f(0, 0, 1), new Vector3f(0, -1, 0));
        addQuad(uvBuffer, atlasPosition);
    }

    public void addXplQuad(int posBuffer, int uvBuffer, Vector3f origin, AtlasPosition atlasPosition) {
        appendTriOffset(List.of(0, 1, 2, 1, 2, 3));
        addQuad(posBuffer, new Vector3f(1, 1, 1).add(origin), new Vector3f(0, 0, -1), new Vector3f(0, -1, 0));
        addQuad(uvBuffer, atlasPosition);
    }

    public void addYmiQuad(int posBuffer, int uvBuffer, Vector3f origin, AtlasPosition atlasPosition) {
        appendTriOffset(List.of(0, 1, 2, 1, 2, 3));
        addQuad(posBuffer, new Vector3f(0, 0, 0).add(origin), new Vector3f(1, 0, 0), new Vector3f(0, 0, 1));
        addQuad(uvBuffer, atlasPosition);
    }

    public void addYplQuad(int posBuffer, int uvBuffer, Vector3f origin, AtlasPosition atlasPosition) {
        appendTriOffset(List.of(0, 1, 2, 1, 2, 3));
        addQuad(posBuffer, new Vector3f(0, 1, 0).add(origin), new Vector3f(1, 0, 0), new Vector3f(0, 0, 1));
        addQuad(uvBuffer, atlasPosition);
    }

    public void addZmiQuad(int posBuffer, int uvBuffer, Vector3f origin, AtlasPosition atlasPosition) {
        appendTriOffset(List.of(0, 1, 2, 1, 2, 3));
        addQuad(posBuffer, new Vector3f(1, 1, 0).add(origin), new Vector3f(-1, 0, 0), new Vector3f(0, -1, 0));
        addQuad(uvBuffer, atlasPosition);
    }

    public void addZplQuad(int posBuffer, int uvBuffer, Vector3f origin, AtlasPosition atlasPosition) {
        appendTriOffset(List.of(0, 1, 2, 1, 2, 3));
        addQuad(posBuffer, new Vector3f(0, 1, 1).add(origin), new Vector3f(1, 0, 0), new Vector3f(0, -1, 0));
        addQuad(uvBuffer, atlasPosition);
    }

    public void addQuad(int buffer, Vector3f o, Vector3f a, Vector3f b) {
        appendBuffer3f(buffer, List.of(o, new Vector3f(o).add(a), new Vector3f(o).add(b), new Vector3f(o).add(a).add(b)));
    }

    public void appendBuffer3f(int buffer, List<Vector3f> data) {
        for (Vector3f v : data) {
            this.data.get(buffer).add(v.x);
            this.data.get(buffer).add(v.y);
            this.data.get(buffer).add(v.z);
        }
    }

    public void appendTri(List<Integer> tri) {
        this.tri.addAll(tri);
        tri.forEach(x -> maxReferredVertex = Math.max(x + 1, maxReferredVertex));
    }

    public void appendTriOffset(List<Integer> tri) {
        appendTri(tri.stream().map(x -> x + maxReferredVertex).collect(Collectors.toList()));
    }
}
