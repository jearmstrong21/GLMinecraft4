package p0nki.glmc4.world.gen;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2i;
import org.joml.Vector3i;
import p0nki.glmc4.block.BlockState;
import p0nki.glmc4.block.Blocks;
import p0nki.glmc4.server.ServerWorld;
import p0nki.glmc4.utils.data.Pair;
import p0nki.glmc4.utils.math.MathUtils;
import p0nki.glmc4.utils.math.SimplexGenerator;
import p0nki.glmc4.world.Chunk;
import p0nki.glmc4.world.ChunkGenerationStatus;
import p0nki.glmc4.world.ChunkNotLoadedException;
import p0nki.glmc4.world.World;
import p0nki.glmc4.world.gen.biomes.Biome;
import p0nki.glmc4.world.gen.biomes.BiomeGenerator;
import p0nki.glmc4.world.gen.ctx.ReadWriteWorldContext;

import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

public class OverworldChunkGenerator extends ChunkGenerator {

    private static final Logger LOGGER = LogManager.getLogger();

    private final Map<Vector2i, ServerWorld.ServerChunkEntry> chunks;

    private final Queue<Vector2i> inHeightmapQueue;
    private final Queue<Vector2i> inDecorate1Queue;
    private final Queue<Vector2i> inSurfaceBuildQueue;
    private final Queue<Vector2i> inDecorate2Queue;
    private final Queue<Vector2i> inLoadQueue;
    private final Queue<Runnable> runnableQueue;
    private final AtomicInteger loadedChunks;

    private final SimplexGenerator heightMapGenerator;
    private final BiomeGenerator biomeGenerator;
    private final SimplexGenerator surfaceDepthGenerator;
    private final long featureSeed;

    public OverworldChunkGenerator(Consumer<Pair<Vector2i, Chunk>> onLoad, ServerWorld serverWorld) {
        super(onLoad, serverWorld);
        final AtomicLong seed = new AtomicLong(System.currentTimeMillis());
        heightMapGenerator = new SimplexGenerator(seed);
        biomeGenerator = new BiomeGenerator(seed);
        surfaceDepthGenerator = new SimplexGenerator(seed);
        featureSeed = seed.incrementAndGet();
        chunks = new ConcurrentHashMap<>();
        inHeightmapQueue = new ConcurrentLinkedQueue<>();
        inDecorate1Queue = new ConcurrentLinkedQueue<>();
        inSurfaceBuildQueue = new ConcurrentLinkedQueue<>();
        inDecorate2Queue = new ConcurrentLinkedQueue<>();
        inLoadQueue = new ConcurrentLinkedQueue<>();
        runnableQueue = new ConcurrentLinkedQueue<>();
        loadedChunks = new AtomicInteger(0);
    }


    private ReadWriteWorldContext getContext(Vector2i v, byte readStatus) {
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
                Vector2i chunkCoordinate = World.getChunkCoordinate(new Vector2i(position.x, position.z));
                if (getChunkStatus(chunkCoordinate) < readStatus)
                    throw new ChunkNotLoadedException(chunkCoordinate.x, chunkCoordinate.y);
                Vector2i coordinateInChunk = World.getCoordinateInChunk(new Vector2i(position.x, position.z));
                return chunks.get(chunkCoordinate).getValue().get(coordinateInChunk.x, position.y, coordinateInChunk.y);
            }

            @Override
            public void set(Vector3i position, BlockState blockState) {
                Vector2i chunkCoordinate = World.getChunkCoordinate(new Vector2i(position.x, position.z));
                if (!canTouch(new Vector2i(position.x, position.z)))
                    throw new ChunkNotLoadedException(chunkCoordinate);
                if (getChunkStatus(chunkCoordinate) < ChunkGenerationStatus.HEIGHTMAP)
                    throw new ChunkNotLoadedException(chunkCoordinate.x, chunkCoordinate.y);
                Vector2i coordinateInChunk = World.getCoordinateInChunk(new Vector2i(position.x, position.z));
                chunks.get(chunkCoordinate).getValue().set(coordinateInChunk.x, position.y, coordinateInChunk.y, blockState);
            }

            @Override
            public int getHeight(Vector2i position) {
                if (!canTouch(new Vector2i(position.x, position.y)))
                    throw new ChunkNotLoadedException(World.getChunkCoordinate(new Vector2i(position.x, position.y)));
                Vector2i chunkCoordinate = World.getChunkCoordinate(position);
                Vector2i coordinateInChunk = World.getCoordinateInChunk(position);
                return chunks.get(chunkCoordinate).getWorldGenHeightMap()[coordinateInChunk.x][coordinateInChunk.y];
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


    private void queueHeightmap(Vector2i v) {
        if (!inHeightmapQueue.contains(v)) inHeightmapQueue.add(v);
    }

    private void queueDecorate1(Vector2i v) {
        if (!inDecorate1Queue.contains(v)) inDecorate1Queue.add(v);
    }

    private void queueSurfaceBuild(Vector2i v) {
        if (!inSurfaceBuildQueue.contains(v)) inSurfaceBuildQueue.add(v);
    }

    private void queueDecorate2(Vector2i v) {
        if (!inDecorate2Queue.contains(v)) inDecorate2Queue.add(v);
    }

    public void queueLoad(Vector2i v) {
        if (!inLoadQueue.contains(v)) inLoadQueue.add(v);
    }


    private byte getChunkStatus(Vector2i v) {
        if (chunks.containsKey(v)) {
            return chunks.get(v).getGenerationStatus();
        }
        return ChunkGenerationStatus.UNKNOWN;
    }


    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean ensureHeightmap(Vector2i v) {
        if (getChunkStatus(v) < ChunkGenerationStatus.HEIGHTMAP) {
            queueHeightmap(v);
            return false;
        }
        return true;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean ensureDecorate1(Vector2i v) {
        if (getChunkStatus(v) < ChunkGenerationStatus.DECORATED1) {
            queueDecorate1(v);
            return false;
        }
        return true;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean ensureSurfaceBuild(Vector2i v) {
        if (getChunkStatus(v) < ChunkGenerationStatus.SURFACE_BUILD) {
            queueSurfaceBuild(v);
            return false;
        }
        return true;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean ensureDecorate2(Vector2i v) {
        if (getChunkStatus(v) < ChunkGenerationStatus.DECORATED2) {
            queueDecorate2(v);
            return false;
        }
        return true;
    }

    private float sampleHeightMap(Biome biome, float x, float z) {
        x *= 0.01F;
        z *= 0.01F;
        float value = heightMapGenerator.simplex(x, z) + 0.5F * heightMapGenerator.simplex(x * 2, z * 2) + 0.25F * heightMapGenerator.simplex(x * 4, z * 4);
        value /= (1 + 0.5 + 0.25);
        value = MathUtils.map(value, -1, 1, biome.getNoiseRange().getFirst(), biome.getNoiseRange().getSecond());
        return value;
    }

    private boolean heightmap(Vector2i v) {
        if (getChunkStatus(v) >= ChunkGenerationStatus.HEIGHTMAP) return false;
        CompletableFuture.runAsync(() -> {
            Chunk chunk = new Chunk();

            Biome[][] biomes = biomeGenerator.generate(v.x, v.y);
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    chunk.setBiome(x, z, biomes[x][z]);
                }
            }

            int cellSize = 4;
            float[][] samples = new float[16 / cellSize + 1][16 / cellSize + 1];
            for (int i = 0; i < 16 / cellSize + 1; i++) {
                for (int j = 0; j < 16 / cellSize + 1; j++) {
                    samples[i][j] = sampleHeightMap(biomes[i * cellSize][j * cellSize], v.x * 16 + i * cellSize, v.y * 16 + j * cellSize);
                }
            }
            int[][] heightmap = new int[16][16];
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    int i = x / cellSize;
                    int j = z / cellSize;
                    float di = (x % cellSize) / (float) cellSize;
                    float dj = (z % cellSize) / (float) cellSize;
                    float minx = MathUtils.lerp(di, samples[i][j], samples[i + 1][j]);
                    float maxx = MathUtils.lerp(di, samples[i][j + 1], samples[i + 1][j + 1]);
                    float value = MathUtils.lerp(dj, minx, maxx);
                    heightmap[x][z] = (int) value;
                }
            }

            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    for (int y = 0; y <= heightmap[x][z]; y++) {
                        chunk.set(x, y, z, Blocks.STONE.getDefaultState());
                    }
                    if (heightmap[x][z] < 64) {
                        for (int y = heightmap[x][z] + 1; y <= 64; y++) {
                            chunk.set(x, y, z, Blocks.WATER.getDefaultState());
                        }
                    }
                }
            }

//            for (int x = 0; x < 16; x++) {
//                for (int z = 0; z < 16; z++) {
//                    for (int y = 64; y < 75; y++) {
//                        chunk.set(x, y, z, Blocks.AIR.getDefaultState());
//                    }
//                }
//            }
            ServerWorld.ServerChunkEntry entry = new ServerWorld.ServerChunkEntry(chunk);
            runnableQueue.add(() -> chunks.put(v, entry));
        });
        return true;
    }

    private boolean decorate1(Vector2i v) {
        if (getChunkStatus(v) >= ChunkGenerationStatus.DECORATED1) return false;
        if (!ensureHeightmap(v)) return false;
        if (!ensureHeightmap(new Vector2i(v.x - 1, v.y))) return false;
        if (!ensureHeightmap(new Vector2i(v.x + 1, v.y))) return false;
        if (!ensureHeightmap(new Vector2i(v.x, v.y - 1))) return false;
        if (!ensureHeightmap(new Vector2i(v.x, v.y + 1))) return false;
        if (!ensureHeightmap(new Vector2i(v.x - 1, v.y - 1))) return false;
        if (!ensureHeightmap(new Vector2i(v.x + 1, v.y - 1))) return false;
        if (!ensureHeightmap(new Vector2i(v.x - 1, v.y + 1))) return false;
        if (!ensureHeightmap(new Vector2i(v.x + 1, v.y + 1))) return false;
        CompletableFuture.runAsync(() -> {
            Random random = new Random(featureSeed + v.hashCode() * 2 + 1);
            random.nextBytes(new byte[10]);
            ReadWriteWorldContext context = getContext(v, ChunkGenerationStatus.HEIGHTMAP);
            MathUtils.streamCoordinatesInChunk()
                    .map(p -> chunks.get(v).getValue().getBiome(p.x, p.y))
                    .distinct()
                    .forEach(biome -> biome.getPreSurfaceFeatures()
                            .forEach(pair -> {
                                pair.getFirst().generate(context, new Vector3i(v.x * 16, 0, v.y * 16), random).forEach(position -> pair.getSecond().generate(context, position, random));
                            }));
            runnableQueue.add(() -> chunks.get(v).setGenerationStatus(ChunkGenerationStatus.DECORATED1));
        });
        return true;
    }

    private boolean surfaceBuild(Vector2i v) {
        if (getChunkStatus(v) >= ChunkGenerationStatus.SURFACE_BUILD) return false;
        if (!ensureDecorate1(v)) return false;
        if (!ensureDecorate1(new Vector2i(v.x - 1, v.y))) return false;
        if (!ensureDecorate1(new Vector2i(v.x + 1, v.y))) return false;
        if (!ensureDecorate1(new Vector2i(v.x, v.y - 1))) return false;
        if (!ensureDecorate1(new Vector2i(v.x, v.y + 1))) return false;
        if (!ensureDecorate1(new Vector2i(v.x - 1, v.y - 1))) return false;
        if (!ensureDecorate1(new Vector2i(v.x + 1, v.y - 1))) return false;
        if (!ensureDecorate1(new Vector2i(v.x - 1, v.y + 1))) return false;
        if (!ensureDecorate1(new Vector2i(v.x + 1, v.y + 1))) return false;
        CompletableFuture.runAsync(() -> {
            chunks.get(v).calculateWorldGenHeightMap();
            Random random = new Random(featureSeed + v.hashCode() * 2);
            random.nextBytes(new byte[10]);
            MathUtils.streamCoordinatesInChunk().forEach(c -> {
                Biome biome = chunks.get(v).getValue().getBiome(c.x, c.y);
                int rx = v.x * 16 + c.x;
                int rz = v.y * 16 + c.y;
                float zoom = 0.01F;
                biome.getSurfaceBuilder().generate(random,
                        chunks.get(v).getValue(),
                        biome, c.x, c.y,
                        (int) MathUtils.map(surfaceDepthGenerator.simplex(rx * zoom, rz * zoom),
                                -1, 1, 3, 10));
            });
            runnableQueue.add(() -> chunks.get(v).setGenerationStatus(ChunkGenerationStatus.SURFACE_BUILD));
        });
        return true;
    }

    private boolean decorate2(Vector2i v) {
        if (getChunkStatus(v) >= ChunkGenerationStatus.DECORATED2) return false;
        if (!ensureSurfaceBuild(v)) return false;
        if (!ensureSurfaceBuild(new Vector2i(v.x - 1, v.y))) return false;
        if (!ensureSurfaceBuild(new Vector2i(v.x + 1, v.y))) return false;
        if (!ensureSurfaceBuild(new Vector2i(v.x, v.y - 1))) return false;
        if (!ensureSurfaceBuild(new Vector2i(v.x, v.y + 1))) return false;
        if (!ensureSurfaceBuild(new Vector2i(v.x - 1, v.y - 1))) return false;
        if (!ensureSurfaceBuild(new Vector2i(v.x + 1, v.y - 1))) return false;
        if (!ensureSurfaceBuild(new Vector2i(v.x - 1, v.y + 1))) return false;
        if (!ensureSurfaceBuild(new Vector2i(v.x + 1, v.y + 1))) return false;
        CompletableFuture.runAsync(() -> {
            Random random = new Random(featureSeed + v.hashCode() * 2 + 2);
            random.nextBytes(new byte[10]);
            ReadWriteWorldContext context = getContext(v, ChunkGenerationStatus.DECORATED1);
            MathUtils.streamCoordinatesInChunk()
                    .map(p -> chunks.get(v).getValue().getBiome(p.x, p.y))
                    .distinct()
                    .forEach(biome -> biome.getPostSurfaceFeatures()
                            .forEach(pair -> {
                                pair.getFirst().generate(context, new Vector3i(v.x * 16, 0, v.y * 16), random).forEach(position -> pair.getSecond().generate(context, position, random));
                            }));
            runnableQueue.add(() -> chunks.get(v).setGenerationStatus(ChunkGenerationStatus.DECORATED2));
        });
        return true;
    }

    private boolean load(Vector2i v) {
        if (getChunkStatus(v) >= ChunkGenerationStatus.LOADED) return false;
        if (!ensureDecorate2(v)) return false;
        if (!ensureDecorate2(new Vector2i(v.x - 1, v.y))) return false;
        if (!ensureDecorate2(new Vector2i(v.x + 1, v.y))) return false;
        if (!ensureDecorate2(new Vector2i(v.x, v.y - 1))) return false;
        if (!ensureDecorate2(new Vector2i(v.x, v.y + 1))) return false;
        if (!ensureDecorate2(new Vector2i(v.x - 1, v.y - 1))) return false;
        if (!ensureDecorate2(new Vector2i(v.x + 1, v.y - 1))) return false;
        if (!ensureDecorate2(new Vector2i(v.x - 1, v.y + 1))) return false;
        if (!ensureDecorate2(new Vector2i(v.x + 1, v.y + 1))) return false;
        runnableQueue.add(() -> {
            chunks.get(v).setGenerationStatus(ChunkGenerationStatus.LOADED);
            onLoad.accept(Pair.of(v, chunks.get(v).getValue()));
            LOGGER.info("Loaded {} chunks", loadedChunks.incrementAndGet());
        });
        return true;
    }

    @Override
    public void tick() {
        for (int i = 0; i < 100 && !inHeightmapQueue.isEmpty(); i++) {
            if (heightmap(inHeightmapQueue.peek())) inHeightmapQueue.remove();
            else inHeightmapQueue.add(inHeightmapQueue.remove());
        }
        for (int i = 0; i < 100 && !inDecorate1Queue.isEmpty(); i++) {
            if (decorate1(inDecorate1Queue.peek())) inDecorate1Queue.remove();
            else inDecorate1Queue.add(inDecorate1Queue.remove());
        }
        for (int i = 0; i < 100 && !inSurfaceBuildQueue.isEmpty(); i++) {
            if (surfaceBuild(inSurfaceBuildQueue.peek())) inSurfaceBuildQueue.remove();
            else inSurfaceBuildQueue.add(inSurfaceBuildQueue.remove());
        }
        for (int i = 0; i < 100 && !inDecorate2Queue.isEmpty(); i++) {
            if (decorate2(inDecorate2Queue.peek())) inDecorate2Queue.remove();
            else inDecorate2Queue.add(inDecorate2Queue.remove());
        }
        for (int i = 0; i < 100 && !inLoadQueue.isEmpty(); i++) {
            if (load(inLoadQueue.peek())) inLoadQueue.remove();
            else inLoadQueue.add(inLoadQueue.remove());
        }
        while (!runnableQueue.isEmpty()) {
            runnableQueue.remove().run();
        }
    }

    @Override
    public void requestLoadChunk(Vector2i v) {
        queueLoad(v);
    }
}
