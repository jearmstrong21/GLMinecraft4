package p0nki.glmc4.block;

import org.joml.Vector2i;
import org.joml.Vector3i;

import java.util.List;

public interface World {

    static Vector2i getCoordinateInChunk(Vector2i worldPos) {
        int x = worldPos.x;
        int z = worldPos.y;
        while (x < 0) x += 16;
        while (z < 0) z += 16;
        while (x >= 16) x -= 16;
        while (z >= 16) z -= 16;
        return new Vector2i(x, z);
    }

    static Vector2i getChunkCoordinate(Vector2i worldPos) {
        int x = worldPos.x;
        int z = worldPos.y;
        while (x < 0) x += 16;
        while (z < 0) z += 16;
        while (x >= 16) x -= 16;
        while (z >= 16) z -= 16;
        return new Vector2i((worldPos.x - x) / 16, (worldPos.y - z) / 16);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    boolean isChunkLoaded(Vector2i chunkCoordinate);

    boolean isClient();

    BlockState get(Vector3i blockPos);

    Chunk getChunk(Vector2i chunkCoordinate);

    void update(Vector3i blockPos, BlockState blockState);

    List<Vector2i> getLoadedChunks();

}
