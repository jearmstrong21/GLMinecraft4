package p0nki.glmc4.world;

import org.joml.Vector2i;

public class ChunkNotLoadedException extends RuntimeException {

    private final int x;
    private final int z;

    public ChunkNotLoadedException(int x, int z) {
        super(String.format("Chunk %s, %s not loaded", x, z));
        this.x = x;
        this.z = z;
    }

    public ChunkNotLoadedException(Vector2i chunkCoordinate) {
        this(chunkCoordinate.x, chunkCoordinate.y);
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }
}
