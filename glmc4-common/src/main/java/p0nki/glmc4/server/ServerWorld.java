package p0nki.glmc4.server;

import org.joml.Vector2i;
import org.joml.Vector3i;
import p0nki.glmc4.network.packet.clientbound.PacketS2CChunkUpdate;
import p0nki.glmc4.utils.data.Pair;
import p0nki.glmc4.utils.math.MathUtils;
import p0nki.glmc4.world.Chunk;
import p0nki.glmc4.world.ChunkGenerationStatus;
import p0nki.glmc4.world.ChunkNotLoadedException;
import p0nki.glmc4.world.World;
import p0nki.glmc4.world.gen.BulkUpdate;
import p0nki.glmc4.world.gen.ChunkGenerator;
import p0nki.glmc4.world.gen.DebugChunkGenerator;
import p0nki.glmc4.world.gen.SunlightChunkGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ServerWorld extends World {

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

    public void update(BulkUpdate update) {
        if (update.getSunlights().size() > 0) {
            throw new IllegalStateException("BulkUpdate should not have manually modified sunlight");
        }
        List<Pair<Vector3i, Byte>> list = new ArrayList<>();
        update.getBlocks().keySet().stream().map(MathUtils::getChunkCoordinate).distinct().forEach(v -> {
            for (int x = 0; x < 16; x++) {
                for (int y = 0; y < 256; y++) {
                    for (int z = 0; z < 16; z++) {
                        update.setSunlight(v.x * 16 + x, y, v.y * 16 + z, (byte) 0);
                    }
                }
            }
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    list.add(Pair.of(new Vector3i(v.x * 16 + x, 255, v.y * 16 + z), (byte) 15));
                }
            }
            Consumer<Vector3i> consumer = p -> {
                p.add(v.x * 16, 0, v.y * 16);
                byte sunlight = update.getSunlight(p);
                if (sunlight > 0) list.add(Pair.of(p, sunlight));
            };
            for (int y = 0; y < 256; y++) {
                consumer.accept(new Vector3i(-1, y, 0));
                consumer.accept(new Vector3i(16, y, 0));
                consumer.accept(new Vector3i(0, y, -1));
                consumer.accept(new Vector3i(0, y, 16));
            }
        });
        while (!list.isEmpty()) {
            Pair<Vector3i, Byte> pair = list.remove(0);
            byte light = pair.getSecond();
            light -= update.getBlock(pair.getFirst()).getBlockedSunlight();
            byte curLight = update.getSunlight(pair.getFirst());
            if (light > curLight) {
                update.setSunlight(pair.getFirst(), light);
                list.add(Pair.of(new Vector3i(pair.getFirst()).add(0, -1, 0), light));
                list.add(Pair.of(new Vector3i(pair.getFirst()).add(-1, 0, 0), (byte) (light - 1)));
                list.add(Pair.of(new Vector3i(pair.getFirst()).add(1, 0, 0), (byte) (light - 1)));
                list.add(Pair.of(new Vector3i(pair.getFirst()).add(0, 0, -1), (byte) (light - 1)));
                list.add(Pair.of(new Vector3i(pair.getFirst()).add(0, 0, 1), (byte) (light - 1)));
            }
        }
        applyUpdate(update);
        MinecraftServer.INSTANCE.writeAll(new PacketS2CChunkUpdate(update));
    }

    @Override
    public Chunk getChunk(Vector2i chunkCoordinate) {
        if (!isChunkLoaded(chunkCoordinate)) throw new ChunkNotLoadedException(chunkCoordinate);
        return chunks.get(chunkCoordinate);
    }

    public void queueLoad(Vector2i v) {
        chunkGenerator.requestLoadChunk(v);
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
                        if (Chunk.HeightMapType.FULL_NOT_WATER.test(value.getBlock(x, y, z))) {
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
