package p0nki.glmc4.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2i;
import org.joml.Vector3i;
import p0nki.glmc4.block.BlockState;
import p0nki.glmc4.block.Blocks;
import p0nki.glmc4.network.packet.clientbound.PacketS2CChunkUpdate;
import p0nki.glmc4.utils.math.MathUtils;
import p0nki.glmc4.world.Chunk;
import p0nki.glmc4.world.ChunkGenerationStatus;
import p0nki.glmc4.world.ChunkNotLoadedException;
import p0nki.glmc4.world.World;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ServerWorld implements World {

    private static final Logger LOGGER = LogManager.getLogger();

    private final Map<Vector2i, ServerChunkEntry> chunks = new HashMap<>();

    private final Queue<Vector2i> inHeightmapQueue = new ConcurrentLinkedQueue<>();
    private final Queue<Vector2i> inDecorateQueue = new ConcurrentLinkedQueue<>();
    private final Queue<Runnable> runnableQueue = new ConcurrentLinkedQueue<>();

    public ServerWorld() {
        for (int x = -5; x <= 5; x++) {
            for (int z = -5; z <= 5; z++) {
                decorate(new Vector2i(x, z));
            }
        }
    }

    private void queueHeightmap(Vector2i v) {
        if (!inHeightmapQueue.contains(v)) inHeightmapQueue.add(v);
    }

    private void queueDecorate(Vector2i v) {
        if (!inDecorateQueue.contains(v)) inDecorateQueue.add(v);
    }

    private byte getChunkStatus(Vector2i v) {
        if (chunks.containsKey(v)) {
            return chunks.get(v).getGenerationStatus();
        }
        return ChunkGenerationStatus.UNKNOWN;
    }

    private boolean heightmap(Vector2i v) {
        if (getChunkStatus(v) >= ChunkGenerationStatus.HEIGHTMAP) return false;
        CompletableFuture.runAsync(() -> {
            Chunk chunk = Chunk.generate(v.x, v.y);
            runnableQueue.add(() -> chunks.put(v, new ServerChunkEntry(chunk)));
        });
        return true;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean ensureHeightmap(Vector2i v) {
        if (getChunkStatus(v) < ChunkGenerationStatus.HEIGHTMAP) {
            Chunk chunk = Chunk.generate(v.x, v.y);
            runnableQueue.add(() -> chunks.put(v, new ServerChunkEntry(chunk)));
            return false;
        }
        return true;
    }

    private void setBlockInUndecoratedChunk(int x, int y, int z, BlockState blockState) {
        Vector2i chunkCoordinate = World.getChunkCoordinate(new Vector2i(x, z));
        if (getChunkStatus(chunkCoordinate) < ChunkGenerationStatus.HEIGHTMAP)
            throw new ChunkNotLoadedException(chunkCoordinate.x, chunkCoordinate.y);
        Vector2i coordinateInChunk = World.getCoordinateInChunk(new Vector2i(x, z));
        chunks.get(chunkCoordinate).getValue().set(coordinateInChunk.x, y, coordinateInChunk.y, blockState);
    }

    private boolean decorate(Vector2i v) {
        if (!ensureHeightmap(v)) return false;
        if (!ensureHeightmap(new Vector2i(v.x - 1, v.y))) return false;
        if (!ensureHeightmap(new Vector2i(v.x + 1, v.y))) return false;
        if (!ensureHeightmap(new Vector2i(v.x, v.y - 1))) return false;
        if (!ensureHeightmap(new Vector2i(v.x, v.y + 1))) return false;
        if (!ensureHeightmap(new Vector2i(v.x - 1, v.y - 1))) return false;
        if (!ensureHeightmap(new Vector2i(v.x + 1, v.y - 1))) return false;
        if (!ensureHeightmap(new Vector2i(v.x - 1, v.y + 1))) return false;
        if (!ensureHeightmap(new Vector2i(v.x + 1, v.y + 1))) return false;
        runnableQueue.add(() -> {
            Random random = new Random(v.hashCode());
            for (int i = 0; i < 1; i++) {
                int x = random.nextInt(16);
                int z = random.nextInt(16);
                int y = chunks.get(v).getGroundHeightMap()[x][z];
                int H = 4 + random.nextInt(3);
                for (int h = 0; h < H; h++) {
                    chunks.get(v).getValue().set(x, y + h, z, Blocks.OAK_LOG.getDefaultState());
                }
                for (int a = -5; a <= 5; a++) {
                    for (int b = -5; b <= 5; b++) {
                        setBlockInUndecoratedChunk(x + v.x * 16 + a, y + H + 1, z + v.y * 16 + b, Blocks.OAK_LEAVES.getDefaultState());
                    }
                }
            }
            chunks.get(v).setGenerationStatus(ChunkGenerationStatus.DECORATED);
        });
        return true;
    }

    public void tick() {
        for (int i = 0; i < 10 && !inHeightmapQueue.isEmpty(); i++) {
            if (heightmap(inHeightmapQueue.peek())) inHeightmapQueue.remove();
            else inHeightmapQueue.add(inHeightmapQueue.remove());
        }
        for (int i = 0; i < 10 && !inDecorateQueue.isEmpty(); i++) {
            if (decorate(inDecorateQueue.peek())) inDecorateQueue.remove();
            else inDecorateQueue.add(inDecorateQueue.remove());
        }
        while (!runnableQueue.isEmpty()) {
            runnableQueue.remove().run();
        }
    }

    @Override
    public boolean isChunkLoaded(Vector2i chunkCoordinate) {
        return chunks.containsKey(chunkCoordinate) && chunks.get(chunkCoordinate).getGenerationStatus() >= ChunkGenerationStatus.DECORATED;
    }

    @Override
    public BlockState get(Vector3i blockPos) {
        Vector2i chunkCoordinate = World.getChunkCoordinate(new Vector2i(blockPos.x, blockPos.z));
        if (!isChunkLoaded(chunkCoordinate)) throw new ChunkNotLoadedException(chunkCoordinate);
        Vector2i coordinateInChunk = World.getCoordinateInChunk(new Vector2i(blockPos.x, blockPos.z));
        return chunks.get(chunkCoordinate).getValue().get(coordinateInChunk.x, blockPos.y, coordinateInChunk.y);
    }

    @Override
    public boolean isClient() {
        return false;
    }

    @Override
    public Chunk getChunk(Vector2i chunkCoordinate) {
        if (!isChunkLoaded(chunkCoordinate)) throw new ChunkNotLoadedException(chunkCoordinate);
        return chunks.get(chunkCoordinate).getValue();
    }

    public void loadChunk(Vector2i v) {
        MathUtils.adjacentWithCenter(v).forEach(this::queueDecorate);
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

    public static class ServerChunkEntry {

        private final int[][] groundHeightMap;
        private final Chunk value;
        private byte generationStatus;

        public ServerChunkEntry(Chunk value) {
            generationStatus = ChunkGenerationStatus.HEIGHTMAP;
            groundHeightMap = new int[16][16];
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    groundHeightMap[x][z] = value.getHeight(x, z);
                }
            }
            this.value = value;
        }

        public byte getGenerationStatus() {
            return generationStatus;
        }

        public void setGenerationStatus(byte generationStatus) {
            this.generationStatus = generationStatus;
        }

        public int[][] getGroundHeightMap() {
            return groundHeightMap;
        }

        public Chunk getValue() {
            return value;
        }
    }

    @Override
    public List<Vector2i> getLoadedChunks() {
        return List.copyOf(chunks.keySet());
    }
}
