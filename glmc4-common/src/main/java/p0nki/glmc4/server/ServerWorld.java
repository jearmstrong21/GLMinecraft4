package p0nki.glmc4.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2i;
import org.joml.Vector3i;
import p0nki.glmc4.block.BlockState;
import p0nki.glmc4.network.packet.clientbound.PacketS2CChunkUpdate;
import p0nki.glmc4.utils.data.Pair;
import p0nki.glmc4.world.Chunk;
import p0nki.glmc4.world.ChunkGenerationStatus;
import p0nki.glmc4.world.ChunkNotLoadedException;
import p0nki.glmc4.world.World;
import p0nki.glmc4.world.gen.ChunkGenerator;
import p0nki.glmc4.world.gen.DebugChunkGenerator;
import p0nki.glmc4.world.gen.SunlightChunkGenerator;
import p0nki.glmc4.world.gen.biomes.Biome;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ServerWorld extends World {

    private static final Logger LOGGER = LogManager.getLogger();

    private final Map<Vector2i, Chunk> chunks = new HashMap<>();

    private final ChunkGenerator chunkGenerator;

    public ServerWorld() {
        Consumer<Pair<Vector2i, Chunk>> pairConsumer = pair -> chunks.put(pair.getFirst(), pair.getSecond());
        chunkGenerator = new SunlightChunkGenerator(pairConsumer, this, DebugChunkGenerator::new);
        for (int x = -5; x <= 5; x++) {
            for (int z = -5; z <= 5; z++) {
                chunkGenerator.requestLoadChunk(new Vector2i(x, z));
            }
        }
    }

    public void tick() {
        chunkGenerator.tick();
    }

    @Override
    public boolean isChunkLoaded(Vector2i chunkCoordinate) {
        return chunks.containsKey(chunkCoordinate);
    }

    @Override
    public BlockState get(Vector3i blockPos) {
        Vector2i chunkCoordinate = World.getChunkCoordinate(new Vector2i(blockPos.x, blockPos.z));
        if (!isChunkLoaded(chunkCoordinate)) throw new ChunkNotLoadedException(chunkCoordinate);
        Vector2i coordinateInChunk = World.getCoordinateInChunk(new Vector2i(blockPos.x, blockPos.z));
        return chunks.get(chunkCoordinate).get(coordinateInChunk.x, blockPos.y, coordinateInChunk.y);
    }

    @Override
    public byte getSunlight(Vector3i blockPos) {
        Vector2i chunkCoordinate = World.getChunkCoordinate(new Vector2i(blockPos.x, blockPos.z));
        if (!isChunkLoaded(chunkCoordinate)) throw new ChunkNotLoadedException(chunkCoordinate);
        Vector2i coordinateInChunk = World.getCoordinateInChunk(new Vector2i(blockPos.x, blockPos.z));
        return chunks.get(chunkCoordinate).getSunlight(coordinateInChunk.x, blockPos.y, coordinateInChunk.y);
    }

    @Override
    public boolean isClient() {
        return false;
    }

    @Override
    public Chunk getChunk(Vector2i chunkCoordinate) {
        if (!isChunkLoaded(chunkCoordinate)) throw new ChunkNotLoadedException(chunkCoordinate);
        return chunks.get(chunkCoordinate);
    }

    public void queueLoad(Vector2i v) {
        chunkGenerator.requestLoadChunk(v);
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
    public Biome getBiome(Vector2i position) {
        Vector2i chunkCoordinate = World.getChunkCoordinate(position);
        if (!isChunkLoaded(chunkCoordinate)) throw new ChunkNotLoadedException(chunkCoordinate);
        Vector2i coordinateInChunk = World.getCoordinateInChunk(position);
        return chunks.get(chunkCoordinate).getBiome(coordinateInChunk.x, coordinateInChunk.y);
    }

    @Override
    public List<Vector2i> getLoadedChunks() {
        return List.copyOf(chunks.keySet());
    }

    public static class ServerChunkEntry {

        private final Chunk value;
        private byte generationStatus;
        private final int[][] worldGenHeightMap;

        public ServerChunkEntry(Chunk value) {
            generationStatus = ChunkGenerationStatus.HEIGHTMAP;
            this.value = value;
            worldGenHeightMap = new int[16][16];
            calculateWorldGenHeightMap();
        }

        public void calculateWorldGenHeightMap() {
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    for (int y = 255; y >= 0; y--) {
                        if (Chunk.HeightMapType.FULL_NOT_WATER.test(value.get(x, y, z))) {
                            worldGenHeightMap[x][z] = y;
                            break;
                        }
                    }
                }
            }
        }

        public int[][] getWorldGenHeightMap() {
            return worldGenHeightMap;
        }

        public byte getGenerationStatus() {
            return generationStatus;
        }

        public void setGenerationStatus(byte generationStatus) {
            this.generationStatus = generationStatus;
        }

        public Chunk getValue() {
            return value;
        }
    }
}
