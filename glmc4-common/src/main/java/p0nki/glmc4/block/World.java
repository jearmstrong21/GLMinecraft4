package p0nki.glmc4.block;

import org.joml.Vector2i;
import org.joml.Vector3i;

public interface World {

    static Vector2i getCoordinateInChunk(Vector2i worldPos) {
        int x = worldPos.x % 16;
        int y = worldPos.y % 16;
        if (x < 0) x += 16;
        if (y < 0) y += 16;
        return new Vector2i(x, y);
    }

    static Vector2i getChunkCoordinate(Vector2i worldPos) {
        int cx = worldPos.x / 16;
        int cz = worldPos.y / 16;
        if (worldPos.x < 0) cx--;
        if (worldPos.y < 0) cz--;
        return new Vector2i(cx, cz);
    }

//
//    static int getCoordinateInChunk(int worldCoordinate) {
//        worldCoordinate %= 16;
//        return worldCoordinate < 0 ? worldCoordinate + 16 : worldCoordinate;
//    }
//
//    static Vector3i getCoordinateInChunk(Vector3i chunkCoordinate) {
//        return new Vector3i(getCoordinateInChunk(chunkCoordinate.x), chunkCoordinate.y, getCoordinateInChunk(chunkCoordinate.z));
//    }
//
//    static int getChunkCoordinate(int worldCoordinate) {
//        return (worldCoordinate - getCoordinateInChunk(worldCoordinate)) / 16;
//    }
//
//    static Vector2i getChunkCoordinate(Vector3i blockPos) {
//        return new Vector2i(getChunkCoordinate(blockPos.x), getChunkCoordinate(blockPos.z));
//    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    boolean isChunkLoaded(Vector2i chunkCoordinate);

    boolean isClient();

    BlockState get(Vector3i blockPos);

    Chunk getChunk(Vector2i chunkCoordinate);

}
