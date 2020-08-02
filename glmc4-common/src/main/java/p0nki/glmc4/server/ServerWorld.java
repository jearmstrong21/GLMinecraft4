package p0nki.glmc4.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2i;
import org.joml.Vector3i;
import p0nki.glmc4.block.BlockState;
import p0nki.glmc4.network.packet.clientbound.PacketS2CChunkUpdate;
import p0nki.glmc4.utils.math.MathUtils;
import p0nki.glmc4.world.Chunk;
import p0nki.glmc4.world.ChunkGenerationStatus;
import p0nki.glmc4.world.ChunkNotLoadedException;
import p0nki.glmc4.world.World;
import p0nki.glmc4.world.gen.biomes.Biome;
import p0nki.glmc4.world.gen.ctx.ReadWriteWorldContext;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.IntStream;

public class ServerWorld implements World {

    private static final Logger LOGGER = LogManager.getLogger();

    private final Map<Vector2i, ServerChunkEntry> chunks = new HashMap<>();

    private final Queue<Vector2i> inHeightmapQueue = new ConcurrentLinkedQueue<>();
    private final Queue<Vector2i> inDecorateQueue = new ConcurrentLinkedQueue<>();
    private final Queue<Runnable> runnableQueue = new ConcurrentLinkedQueue<>();

    private ReadWriteWorldContext getContext(Vector2i v) {
        return new ReadWriteWorldContext() {

            @Override
            public boolean canTouch(Vector2i position) {
                Vector2i chunkCoordinate = World.getChunkCoordinate(position);
                return Math.abs(chunkCoordinate.x - v.x) <= 1 && Math.abs(chunkCoordinate.y - v.y) <= 1;
            }

            @Override
            public BlockState get(Vector3i position) {
                if (!canTouch(new Vector2i(position.x, position.z)))
                    throw new ChunkNotLoadedException(World.getChunkCoordinate(new Vector2i(position.x, position.z)));
                return ServerWorld.this.get(position);
            }

            @Override
            public void set(Vector3i position, BlockState blockState) {
                if (!canTouch(new Vector2i(position.x, position.z)))
                    throw new ChunkNotLoadedException(World.getChunkCoordinate(new Vector2i(position.x, position.z)));
                setBlockInUndecoratedChunk(position.x, position.y, position.z, blockState);
            }

            @Override
            public int getGroundHeightMap(Vector2i position) {
                if (!canTouch(new Vector2i(position.x, position.y)))
                    throw new ChunkNotLoadedException(World.getChunkCoordinate(new Vector2i(position.x, position.y)));
                Vector2i chunkCoordinate = World.getChunkCoordinate(position);
                Vector2i coordinateInChunk = World.getCoordinateInChunk(position);
                return chunks.get(chunkCoordinate).getGroundHeightMap()[coordinateInChunk.x][coordinateInChunk.y];
            }

            @Override
            public Biome getBiome(Vector2i position) {
                if (!canTouch(new Vector2i(position.x, position.y)))
                    throw new ChunkNotLoadedException(World.getChunkCoordinate(new Vector2i(position.x, position.y)));
                Vector2i chunkCoordinate = World.getChunkCoordinate(position);
                Vector2i coordinateInChunk = World.getCoordinateInChunk(position);
                return chunks.get(chunkCoordinate).getValue().getBiome(coordinateInChunk.x, coordinateInChunk.y);
            }
        };
    }

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
            Chunk chunk = Chunk.generateHeightMap(v.x, v.y);
            runnableQueue.add(() -> chunks.put(v, new ServerChunkEntry(chunk)));
        });
        return true;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean ensureHeightmap(Vector2i v) {
        if (getChunkStatus(v) < ChunkGenerationStatus.HEIGHTMAP) {
            Chunk chunk = Chunk.generateHeightMap(v.x, v.y);
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
            ReadWriteWorldContext context = getContext(v);
            IntStream.range(0, 16)
                    .boxed()
                    .flatMap(x -> IntStream.range(0, 16)
                            .mapToObj(z -> new Vector2i(x, z)))
                    .map(p -> chunks.get(v).getValue().getBiome(p.x, p.y))
                    .forEach(biome -> biome.getDecoratorFeatures()
                            .forEach(pair -> {
                                pair.getFirst().generate(context, new Vector3i(v.x * 16, 0, v.y * 16), random).forEach(position -> pair.getSecond().generate(context, position, random));
                            }));
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

    @Override
    public Biome getBiome(Vector2i position) {
        Vector2i chunkCoordinate = World.getChunkCoordinate(position);
        if (!isChunkLoaded(chunkCoordinate)) throw new ChunkNotLoadedException(chunkCoordinate);
        Vector2i coordinateInChunk = World.getCoordinateInChunk(position);
        return chunks.get(chunkCoordinate).getValue().getBiome(coordinateInChunk.x, coordinateInChunk.y);
    }
}
