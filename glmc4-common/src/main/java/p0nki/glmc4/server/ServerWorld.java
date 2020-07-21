package p0nki.glmc4.server;

import org.joml.Vector2i;
import p0nki.glmc4.block.BlockState;
import p0nki.glmc4.block.Chunk;
import p0nki.glmc4.block.ChunkNotLoadedException;
import p0nki.glmc4.block.World;
import p0nki.glmc4.utils.math.BlockPos;
import p0nki.glmc4.utils.math.MathUtils;

import java.util.HashMap;
import java.util.Map;

public class ServerWorld implements World {

    private final Map<Long, Chunk> chunks;

    public ServerWorld() {
        chunks = new HashMap<>();
        for (int x = -4; x <= 4; x++) {
            for (int z = -4; z <= 4; z++) {
                chunks.put(MathUtils.pack(x, z), Chunk.generate(x, z));
            }
        }
    }

    @Override
    public boolean isChunkLoaded(Vector2i chunkCoordinate) {
        return chunks.containsKey(MathUtils.pack(chunkCoordinate));
    }

    @Override
    public boolean isClient() {
        return false;
    }

    @Override
    public BlockState get(BlockPos blockPos) {
        Vector2i chunkCoordinate = World.getChunkCoordinate(blockPos);
        if (!isChunkLoaded(chunkCoordinate)) throw new ChunkNotLoadedException(chunkCoordinate);
        BlockPos coordinateInChunk = World.getCoordinateInChunk(blockPos);
        return chunks.get(MathUtils.pack(chunkCoordinate)).get(coordinateInChunk);
    }

    @Override
    public Chunk getChunk(Vector2i chunkCoordinate) {
        if (!isChunkLoaded(chunkCoordinate)) throw new ChunkNotLoadedException(chunkCoordinate);
        return chunks.get(MathUtils.pack(chunkCoordinate));
    }
}
