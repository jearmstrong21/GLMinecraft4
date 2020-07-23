package p0nki.glmc4.utils.math;

import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class AABB {

    private final Vector3f v0;
    private final Vector3f v1;

    public AABB(Vector3f position, Vector3f size) {
        this.v0 = new Vector3f(position);
        this.v1 = new Vector3f(size);
    }

    public AABB(float x0, float y0, float z0, float x1, float y1, float z1) {
        v0 = new Vector3f(x0, y0, z0);
        v1 = new Vector3f(x1, y1, z1);
    }

    public List<BlockPos> listBlockPos() {
        List<BlockPos> list = new ArrayList<>();
        for (int x = (int) v0.x; x <= (int) v1.x; x++) {
            for (int y = (int) v0.y; y <= (int) v1.y; y++) {
                for (int z = (int) v0.z; z <= (int) v1.z; z++) {
                    list.add(new BlockPos(x, y, z));
                }
            }
        }
        return list;
    }

    public Vector3f getV0() {
        return v0;
    }

    public Vector3f getV1() {
        return v1;
    }

    public Vector3f getSize() {
        return new Vector3f(v1.x - v0.x, v1.y - v0.y, v1.z - v0.z);
    }
}
