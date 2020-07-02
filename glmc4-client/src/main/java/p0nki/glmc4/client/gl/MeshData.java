package p0nki.glmc4.client.gl;

import java.util.ArrayList;
import java.util.List;

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

    public void appendTri(List<Integer> tri) {
        this.tri.addAll(tri);
    }
}
