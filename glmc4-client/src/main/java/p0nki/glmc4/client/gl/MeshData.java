package p0nki.glmc4.client.gl;

import org.joml.Vector2d;
import org.joml.Vector3d;
import p0nki.glmc4.client.assets.AtlasPosition;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.DoubleStream;

public class MeshData {

    private List<List<Double>> data;
    private List<Integer> sizes;
    private List<Integer> tri;

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

    public void addQuad(int buffer, Vector3d o, Vector3d a, Vector3d b) {
        appendBuffer3f(buffer, List.of(o, new Vector3d(o).add(a), new Vector3d(o).add(b), new Vector3d(o).add(a).add(b)));
    }

    public void appendBuffer3f(int buffer, List<Vector3d> data) {
        data.stream().flatMapToDouble(vector3f -> DoubleStream.of(vector3f.x, vector3f.y, vector3f.z)).forEachOrdered(this.data.get(buffer)::add);
    }

    public void appendTri(List<Integer> tri) {
        this.tri.addAll(tri);
    }
}
