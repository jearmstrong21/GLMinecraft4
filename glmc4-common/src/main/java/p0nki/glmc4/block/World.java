package p0nki.glmc4.block;

import org.joml.Vector2i;
import p0nki.glmc4.utils.math.BlockPos;

public interface World {

    static int getCoordinateInChunk(int worldCoordinate) {
        worldCoordinate %= 16;
        return worldCoordinate < 0 ? worldCoordinate + 16 : worldCoordinate;
    }

    static BlockPos getCoordinateInChunk(BlockPos chunkCoordinate) {
        return new BlockPos(getCoordinateInChunk(chunkCoordinate.getX()), chunkCoordinate.getY(), getCoordinateInChunk(chunkCoordinate.getZ()));
    }

    static int getChunkCoordinate(int worldCoordinate) {
        return (worldCoordinate - getCoordinateInChunk(worldCoordinate)) / 16;
    }

    static Vector2i getChunkCoordinate(BlockPos blockPos) {
        return new Vector2i(getChunkCoordinate(blockPos.getX()), getChunkCoordinate(blockPos.getZ()));
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    boolean isChunkLoaded(Vector2i chunkCoordinate);

    boolean isClient();

    BlockState get(BlockPos blockPos);

    Chunk getChunk(Vector2i chunkCoordinate);

}
