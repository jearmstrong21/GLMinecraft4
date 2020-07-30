package p0nki.glmc4.client.render;

import org.joml.*;
import p0nki.glmc4.client.GLMC4Client;
import p0nki.glmc4.client.assets.TextureLayer;
import p0nki.glmc4.client.assets.TextureQuad;

import java.lang.Math;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MeshData {

    private final static int POS_BUFFER_INDEX = 0;
    private final static int UV_BUFFER_INDEX = 1;
    private final static int COLOR_BUFFER_INDEX = 2;
    private final static int AO_BUFFER_INDEX = 3;
    private final static int FAKE_LIGHT_BUFFER_INDEX = 4;

    private final static float FAKE_LIGHT_SIDE_TOP = 1.0F;
    private final static float FAKE_LIGHT_SIDE_1 = 0.8F;
    private final static float FAKE_LIGHT_SIDE_2 = 0.6F;
    private final static float FAKE_LIGHT_SIDE_BOTTOM = 0.4F;

    private final List<List<Float>> data;
    private final List<Integer> sizes;
    private final List<Integer> tri;
    private int maxReferredVertex = 0;

    public MeshData() {
        data = new ArrayList<>();
        sizes = new ArrayList<>();
        tri = new ArrayList<>();
    }

    public List<Float> getBuffer(int index) {
        return data.get(index);
    }

    public static MeshData chunk() {
        return new MeshData().addBuffer(3).addBuffer(2).addBuffer(3).addBuffer(1).addBuffer(1);
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

    public MeshData multiply4f(int buffer, Matrix4f matrix) {
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

    @SuppressWarnings("UnusedReturnValue")
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

    @SuppressWarnings("UnusedReturnValue")
    public MeshData appendBuffer2f(int buffer, List<Vector2f> data) {
        for (Vector2f v : data) {
            this.data.get(buffer).add(v.x);
            this.data.get(buffer).add(v.y);
        }
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public MeshData addQuad(int buffer, Vector2f o, Vector2f a, Vector2f b) {
        appendBuffer2f(buffer, List.of(o, new Vector2f(o).add(a), new Vector2f(o).add(b), new Vector2f(o).add(a).add(b)));
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public MeshData addQuad(TextureLayer layer, float ao0, float ao1, float ao2, float ao3, float fakeLight) {
        addQuad(UV_BUFFER_INDEX, new Vector2f(layer.atlasPosition.x, layer.atlasPosition.y), new Vector2f(layer.atlasPosition.w, 0), new Vector2f(0, layer.atlasPosition.h));
        appendBuffer3f(COLOR_BUFFER_INDEX, List.of(layer.color, layer.color, layer.color, layer.color));
        appendBuffer(AO_BUFFER_INDEX, List.of(ao0, ao1, ao2, ao3));
        appendBuffer(FAKE_LIGHT_BUFFER_INDEX, List.of(fakeLight, fakeLight, fakeLight, fakeLight));
        return this;
    }

    public MeshData addXmiQuad(Vector3i blockPos, Vector3f origin, TextureQuad textureQuad) {
        return addXmiQuad(origin, textureQuad,
                GLMC4Client.getClientWorld().calculateAO(new Vector3i(blockPos.x, blockPos.y + 1, blockPos.z)),
                GLMC4Client.getClientWorld().calculateAO(new Vector3i(blockPos.x, blockPos.y + 1, blockPos.z + 1)),
                GLMC4Client.getClientWorld().calculateAO(new Vector3i(blockPos.x, blockPos.y, blockPos.z)),
                GLMC4Client.getClientWorld().calculateAO(new Vector3i(blockPos.x, blockPos.y, blockPos.z + 1)));
    }

    public MeshData addXmiQuad(Vector3f origin, TextureQuad textureQuad, float ao0, float ao1, float ao2, float ao3) {
        appendTriOffset(List.of(0, 1, 2, 1, 2, 3));
        addQuad(POS_BUFFER_INDEX, new Vector3f(0, 1, 0).add(origin), new Vector3f(0, 0, 1), new Vector3f(0, -1, 0));
        addQuad(textureQuad.layer1, ao0, ao1, ao2, ao3, FAKE_LIGHT_SIDE_1);
        return this;
    }

    public MeshData addXplQuad(Vector3i blockPos, Vector3f origin, TextureQuad textureQuad) {
        return addXplQuad(origin, textureQuad,
                GLMC4Client.getClientWorld().calculateAO(new Vector3i(blockPos.x + 1, blockPos.y + 1, blockPos.z + 1)),
                GLMC4Client.getClientWorld().calculateAO(new Vector3i(blockPos.x + 1, blockPos.y + 1, blockPos.z)),
                GLMC4Client.getClientWorld().calculateAO(new Vector3i(blockPos.x + 1, blockPos.y, blockPos.z + 1)),
                GLMC4Client.getClientWorld().calculateAO(new Vector3i(blockPos.x + 1, blockPos.y, blockPos.z)));
    }

    public MeshData addXplQuad(Vector3f origin, TextureQuad textureQuad, float ao0, float ao1, float ao2, float ao3) {
        appendTriOffset(List.of(0, 1, 2, 1, 2, 3));
        addQuad(POS_BUFFER_INDEX, new Vector3f(1, 1, 1).add(origin), new Vector3f(0, 0, -1), new Vector3f(0, -1, 0));
        addQuad(textureQuad.layer1, ao0, ao1, ao2, ao3, FAKE_LIGHT_SIDE_2);
        return this;
    }

    public MeshData addYmiQuad(Vector3i blockPos, Vector3f origin, TextureQuad textureQuad) {
        return addYmiQuad(origin, textureQuad,
                GLMC4Client.getClientWorld().calculateAO(new Vector3i(blockPos.x, blockPos.y, blockPos.z)),
                GLMC4Client.getClientWorld().calculateAO(new Vector3i(blockPos.x + 1, blockPos.y, blockPos.z)),
                GLMC4Client.getClientWorld().calculateAO(new Vector3i(blockPos.x, blockPos.y, blockPos.z + 1)),
                GLMC4Client.getClientWorld().calculateAO(new Vector3i(blockPos.x + 1, blockPos.y, blockPos.z + 1)));
    }

    public MeshData addYmiQuad(Vector3f origin, TextureQuad textureQuad, float ao0, float ao1, float ao2, float ao3) {
        appendTriOffset(List.of(0, 1, 2, 1, 2, 3));
        addQuad(POS_BUFFER_INDEX, new Vector3f(0, 0, 0).add(origin), new Vector3f(1, 0, 0), new Vector3f(0, 0, 1));
        addQuad(textureQuad.layer1, ao0, ao1, ao2, ao3, FAKE_LIGHT_SIDE_BOTTOM);
        return this;
    }

    public MeshData addYplQuad(Vector3i blockPos, Vector3f origin, TextureQuad textureQuad) {
        return addYplQuad(origin, textureQuad,
                GLMC4Client.getClientWorld().calculateAO(new Vector3i(blockPos.x, blockPos.y + 1, blockPos.z)),
                GLMC4Client.getClientWorld().calculateAO(new Vector3i(blockPos.x + 1, blockPos.y + 1, blockPos.z)),
                GLMC4Client.getClientWorld().calculateAO(new Vector3i(blockPos.x, blockPos.y + 1, blockPos.z + 1)),
                GLMC4Client.getClientWorld().calculateAO(new Vector3i(blockPos.x + 1, blockPos.y + 1, blockPos.z + 1)));
    }

    public MeshData addYplQuad(Vector3f origin, TextureQuad textureQuad, float ao0, float ao1, float ao2, float ao3) {
        appendTriOffset(List.of(0, 1, 2, 1, 2, 3));
        addQuad(POS_BUFFER_INDEX, new Vector3f(0, 1, 0).add(origin), new Vector3f(1, 0, 0), new Vector3f(0, 0, 1));
        addQuad(textureQuad.layer1, ao0, ao1, ao2, ao3, FAKE_LIGHT_SIDE_TOP);
        return this;
    }

    public MeshData addZmiQuad(Vector3i blockPos, Vector3f origin, TextureQuad textureQuad) {
        return addZmiQuad(origin, textureQuad,
                GLMC4Client.getClientWorld().calculateAO(new Vector3i(blockPos.x + 1, blockPos.y + 1, blockPos.z)),
                GLMC4Client.getClientWorld().calculateAO(new Vector3i(blockPos.x, blockPos.y + 1, blockPos.z)),
                GLMC4Client.getClientWorld().calculateAO(new Vector3i(blockPos.x + 1, blockPos.y, blockPos.z)),
                GLMC4Client.getClientWorld().calculateAO(new Vector3i(blockPos.x, blockPos.y, blockPos.z)));
    }

    public MeshData addZmiQuad(Vector3f origin, TextureQuad textureQuad, float ao0, float ao1, float ao2, float ao3) {
        appendTriOffset(List.of(0, 1, 2, 1, 2, 3));
        addQuad(POS_BUFFER_INDEX, new Vector3f(1, 1, 0).add(origin), new Vector3f(-1, 0, 0), new Vector3f(0, -1, 0));
        addQuad(textureQuad.layer1, ao0, ao1, ao2, ao3, FAKE_LIGHT_SIDE_1);
        return this;
    }

    public MeshData addZplQuad(Vector3i blockPos, Vector3f origin, TextureQuad textureQuad) {
        return addZplQuad(origin, textureQuad,
                GLMC4Client.getClientWorld().calculateAO(new Vector3i(blockPos.x, blockPos.y + 1, blockPos.z + 1)),
                GLMC4Client.getClientWorld().calculateAO(new Vector3i(blockPos.x + 1, blockPos.y + 1, blockPos.z + 1)),
                GLMC4Client.getClientWorld().calculateAO(new Vector3i(blockPos.x, blockPos.y, blockPos.z + 1)),
                GLMC4Client.getClientWorld().calculateAO(new Vector3i(blockPos.x + 1, blockPos.y, blockPos.z + 1)));
    }

    public MeshData addZplQuad(Vector3f origin, TextureQuad textureQuad, float ao0, float ao1, float ao2, float ao3) {
        appendTriOffset(List.of(0, 1, 2, 1, 2, 3));
        addQuad(POS_BUFFER_INDEX, new Vector3f(0, 1, 1).add(origin), new Vector3f(1, 0, 0), new Vector3f(0, -1, 0));
        addQuad(textureQuad.layer1, ao0, ao1, ao2, ao3, FAKE_LIGHT_SIDE_2);
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public MeshData addQuad(int buffer, Vector3f o, Vector3f a, Vector3f b) {
        appendBuffer3f(buffer, List.of(o, new Vector3f(o).add(a), new Vector3f(o).add(b), new Vector3f(o).add(a).add(b)));
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
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

    @SuppressWarnings("UnusedReturnValue")
    public MeshData appendTriOffset(List<Integer> tri) {
        appendTri(tri.stream().map(x -> x + maxReferredVertex).collect(Collectors.toList()));
        return this;
    }
}
