package p0nki.glmc4.utils;

import org.joml.Vector3f;

public class AABB {

    private final Vector3f position;
    private final Vector3f size;

    public AABB(Vector3f position, Vector3f size) {
        this.position = new Vector3f(position);
        this.size = new Vector3f(size);
    }

    public AABB(float x, float y, float z, float w, float h, float d) {
        this(new Vector3f(x, y, z), new Vector3f(w, h, d));
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getSize() {
        return size;
    }
}
