package p0nki.glmc4.utils.math;

import org.joml.Vector3f;

import java.util.stream.Stream;

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

    public Stream<BlockPos> streamBlockPos() {
        Stream.Builder<BlockPos> builder = Stream.builder();
        for (int x = (int) position.x; x < position.x + size.x; x++) {
            for (int y = (int) position.y; y < position.y + size.y; y++) {
                for (int z = (int) position.z; z < position.z + size.z; z++) {
                    builder.accept(new BlockPos(x, y, z));
                }
            }
        }
        return builder.build();
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getSize() {
        return size;
    }
}
