package p0nki.glmc4.server;

import org.joml.Vector2i;
import org.joml.Vector3i;
import p0nki.glmc4.block.BlockState;
import p0nki.glmc4.block.Chunk;
import p0nki.glmc4.block.ChunkNotLoadedException;
import p0nki.glmc4.block.World;
import p0nki.glmc4.network.packet.clientbound.PacketS2CChunkUpdate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerWorld implements World {

    private final Map<Vector2i, Chunk> chunks;

    public ServerWorld() {
        chunks = new HashMap<>();
        for (int x = -4; x <= 4; x++) {
            for (int z = -4; z <= 4; z++) {
                chunks.put(new Vector2i(x, z), Chunk.generate(x, z));
            }
        }
    }

    @Override
    public boolean isChunkLoaded(Vector2i chunkCoordinate) {
        return chunks.containsKey(chunkCoordinate);
    }

    @Override
    public boolean isClient() {
        return false;
    }

    @Override
    public BlockState get(Vector3i blockPos) {
        Vector2i chunkCoordinate = World.getChunkCoordinate(new Vector2i(blockPos.x, blockPos.z));
        if (!isChunkLoaded(chunkCoordinate)) throw new ChunkNotLoadedException(chunkCoordinate);
        Vector2i coordinateInChunk = World.getCoordinateInChunk(new Vector2i(blockPos.x, blockPos.z));
        return chunks.get(chunkCoordinate).get(coordinateInChunk.x, blockPos.y, coordinateInChunk.y);
    }

    @Override
    public Chunk getChunk(Vector2i chunkCoordinate) {
        if (!isChunkLoaded(chunkCoordinate)) throw new ChunkNotLoadedException(chunkCoordinate);
        return chunks.get(chunkCoordinate);
    }

    @Override
    public void update(Vector3i blockPos, BlockState blockState) {
        if (get(blockPos).toLong() == blockState.toLong()) return;
        Vector2i chunkCoordinate = World.getChunkCoordinate(new Vector2i(blockPos.x, blockPos.z));
        if (!isChunkLoaded(chunkCoordinate)) throw new ChunkNotLoadedException(chunkCoordinate.x, chunkCoordinate.y);
        Vector2i coordinateInChunk = World.getCoordinateInChunk(new Vector2i(blockPos.x, blockPos.z));
        getChunk(chunkCoordinate).set(coordinateInChunk.x, blockPos.y, coordinateInChunk.y, blockState);
        MinecraftServer.INSTANCE.writeAll(new PacketS2CChunkUpdate(blockPos.x, blockPos.y, blockPos.z, blockState));
    }

    @Override
    public List<Vector2i> getLoadedChunks() {
        return List.copyOf(chunks.keySet());
    }
}
