package p0nki.glmc4.client.gl;

import org.joml.Vector2d;
import org.joml.Vector3d;
import p0nki.glmc4.client.assets.AtlasPosition;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

public class MeshData {

    private List<List<Double>> data;
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

    public List<List<Double>> getData() {
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

    public void appendBuffer(int buffer, List<Double> data) {
        this.data.get(buffer).addAll(data);
    }

    public void appendBuffer2f(int buffer, List<Vector2d> data) {
        data.stream().flatMapToDouble(vector2f -> DoubleStream.of(vector2f.x, vector2f.y)).forEachOrdered(this.data.get(buffer)::add);
    }

    public void addQuad(int buffer, Vector2d o, Vector2d a, Vector2d b) {
        appendBuffer2f(buffer, List.of(o, new Vector2d(o).add(a), new Vector2d(o).add(b), new Vector2d(o).add(a).add(b)));
    }

    public void addQuad(int buffer, AtlasPosition atlasPosition) {
        addQuad(buffer, new Vector2d(atlasPosition.x, atlasPosition.y), new Vector2d(atlasPosition.w, 0), new Vector2d(0, atlasPosition.h));
    }

    public void addXmiQuad(int posBuffer, int uvBuffer, Vector3d origin, AtlasPosition atlasPosition) {
        appendTriOffset(List.of(0, 1, 2, 1, 2, 3));
        addQuad(posBuffer, new Vector3d(0, 1, 0).add(origin), new Vector3d(0, 0, 1), new Vector3d(0, -1, 0));
        addQuad(uvBuffer, atlasPosition);
    }

    public void addXplQuad(int posBuffer, int uvBuffer, Vector3d origin, AtlasPosition atlasPosition) {
        appendTriOffset(List.of(0, 1, 2, 1, 2, 3));
        addQuad(posBuffer, new Vector3d(1, 1, 1).add(origin), new Vector3d(0, 0, -1), new Vector3d(0, -1, 0));
        addQuad(uvBuffer, atlasPosition);
    }

    public void addYmiQuad(int posBuffer, int uvBuffer, Vector3d origin, AtlasPosition atlasPosition) {
        appendTriOffset(List.of(0, 1, 2, 1, 2, 3));
        addQuad(posBuffer, new Vector3d(0, 0, 0).add(origin), new Vector3d(1, 0, 0), new Vector3d(0, 0, 1));
        addQuad(uvBuffer, atlasPosition);
    }

    public void addYplQuad(int posBuffer, int uvBuffer, Vector3d origin, AtlasPosition atlasPosition) {
        appendTriOffset(List.of(0, 1, 2, 1, 2, 3));
        addQuad(posBuffer, new Vector3d(0, 1, 0).add(origin), new Vector3d(1, 0, 0), new Vector3d(0, 0, 1));
        addQuad(uvBuffer, atlasPosition);
    }

    public void addZmiQuad(int posBuffer, int uvBuffer, Vector3d origin, AtlasPosition atlasPosition) {
        appendTriOffset(List.of(0, 1, 2, 1, 2, 3));
        addQuad(posBuffer, new Vector3d(1, 1, 0).add(origin), new Vector3d(-1, 0, 0), new Vector3d(0, -1, 0));
        addQuad(uvBuffer, atlasPosition);
    }

    public void addZplQuad(int posBuffer, int uvBuffer, Vector3d origin, AtlasPosition atlasPosition) {
        appendTriOffset(List.of(0, 1, 2, 1, 2, 3));
        addQuad(posBuffer, new Vector3d(0, 1, 1).add(origin), new Vector3d(1, 0, 0), new Vector3d(0, -1, 0));
        addQuad(uvBuffer, atlasPosition);
    }

    public void addQuad(int buffer, Vector3d o, Vector3d a, Vector3d b) {
        appendBuffer3f(buffer, List.of(o, new Vector3d(o).add(a), new Vector3d(o).add(b), new Vector3d(o).add(a).add(b)));
    }

    public void appendBuffer3f(int buffer, List<Vector3d> data) {
        data.stream().flatMapToDouble(vector3f -> DoubleStream.of(vector3f.x, vector3f.y, vector3f.z)).forEachOrdered(this.data.get(buffer)::add);
    }

    public void appendTri(List<Integer> tri) {
        this.tri.addAll(tri);
        tri.forEach(x -> maxReferredVertex = Math.max(x + 1, maxReferredVertex));
    }

    public void appendTriOffset(List<Integer> tri) {
        appendTri(tri.stream().map(x -> x + maxReferredVertex).collect(Collectors.toList()));
    }
}
