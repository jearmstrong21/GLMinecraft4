package p0nki.glmc4.world.gen;

import org.joml.Vector2i;
import org.joml.Vector3i;
import p0nki.glmc4.block.Block;
import p0nki.glmc4.server.ServerWorld;
import p0nki.glmc4.utils.data.Pair;
import p0nki.glmc4.world.Chunk;
import p0nki.glmc4.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class SunlightChunkGenerator extends ChunkGenerator {

    private final static byte UNKNOWN = 0;
    private final static byte GENERATED = 1;
    private final static byte SUNLIGHT = 2;
    private final static byte LOADED = 3;
    private final ChunkGenerator child;
    private final Map<Vector2i, Pair<Chunk, Byte>> chunks;

    private final Queue<Vector2i> inGenerateQueue;
    private final Queue<Vector2i> inSunlightQueue;
    private final Queue<Vector2i> inLoadQueue;
    private final Queue<Runnable> runnableQueue;

    public SunlightChunkGenerator(Consumer<Pair<Vector2i, Chunk>> onLoad, ServerWorld serverWorld, BiFunction<Consumer<Pair<Vector2i, Chunk>>, ServerWorld, ChunkGenerator> creator) {
        super(onLoad, serverWorld);
        chunks = new ConcurrentHashMap<>();
        inGenerateQueue = new ConcurrentLinkedQueue<>();
        inSunlightQueue = new ConcurrentLinkedQueue<>();
        inLoadQueue = new ConcurrentLinkedQueue<>();
        runnableQueue = new ConcurrentLinkedQueue<>();
        this.child = creator.apply(pair -> chunks.put(pair.getFirst(), Pair.of(pair.getSecond(), GENERATED)), serverWorld);
    }

    private byte getChunkStatus(Vector2i v) {
        if (chunks.containsKey(v)) {
            return chunks.get(v).getSecond();
        }
        return UNKNOWN;
    }

    private void queueGenerate(Vector2i v) {
        if (!inGenerateQueue.contains(v)) inGenerateQueue.add(v);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean ensureGenerated(Vector2i v) {
        if (getChunkStatus(v) < GENERATED) {
            queueGenerate(v);
            return false;
        }
        return true;
    }

    private boolean generate(Vector2i v) {
        if (getChunkStatus(v) >= GENERATED) return false;
        child.requestLoadChunk(v);
        return true;
    }

    private boolean sunlight(Vector2i v) {
        if (getChunkStatus(v) >= SUNLIGHT) return false;
        if (!ensureGenerated(v)) return false;
        if (!ensureGenerated(new Vector2i(v.x - 1, v.y))) return false;
        if (!ensureGenerated(new Vector2i(v.x + 1, v.y))) return false;
        if (!ensureGenerated(new Vector2i(v.x, v.y - 1))) return false;
        if (!ensureGenerated(new Vector2i(v.x, v.y + 1))) return false;
        if (!ensureGenerated(new Vector2i(v.x - 1, v.y - 1))) return false;
        if (!ensureGenerated(new Vector2i(v.x + 1, v.y - 1))) return false;
        if (!ensureGenerated(new Vector2i(v.x - 1, v.y + 1))) return false;
        if (!ensureGenerated(new Vector2i(v.x + 1, v.y + 1))) return false;
        CompletableFuture.runAsync(() -> {
            try {
                List<Pair<Vector3i, Byte>> list = new ArrayList<>();
                for (int x = 0; x < 16; x++) {
                    for (int z = 0; z < 16; z++) {
                        list.add(Pair.of(new Vector3i(v.x * 16 + x, 255, v.y * 16 + z), (byte) 15));
                    }
                }
                while (!list.isEmpty()) {
                    Pair<Vector3i, Byte> pair = list.remove(0);
                    byte light = pair.getSecond();
                    Vector2i chunkCoordinate = World.getChunkCoordinate(new Vector2i(pair.getFirst().x, pair.getFirst().z));
                    Vector2i coordinateInChunk = World.getCoordinateInChunk(new Vector2i(pair.getFirst().x, pair.getFirst().z));
                    Block block = chunks.get(chunkCoordinate).getFirst().get(coordinateInChunk.x, pair.getFirst().y, coordinateInChunk.y).getBlock();
                    light -= block.getBlockedSunlight();
                    if (light <= 0) continue;
                    byte curLight = chunks.get(chunkCoordinate).getFirst().getSunlight()[coordinateInChunk.x][pair.getFirst().y][coordinateInChunk.y];
                    if (light > curLight) {
                        chunks.get(chunkCoordinate).getFirst().getSunlight()[coordinateInChunk.x][pair.getFirst().y][coordinateInChunk.y] = light;
                        list.add(Pair.of(new Vector3i(pair.getFirst()).add(0, -1, 0), light));
                        list.add(Pair.of(new Vector3i(pair.getFirst()).add(-1, 0, 0), (byte) (light - 1)));
                        list.add(Pair.of(new Vector3i(pair.getFirst()).add(1, 0, 0), (byte) (light - 1)));
                        list.add(Pair.of(new Vector3i(pair.getFirst()).add(0, 0, -1), (byte) (light - 1)));
                        list.add(Pair.of(new Vector3i(pair.getFirst()).add(0, 0, 1), (byte) (light - 1)));
                    }
                }
                runnableQueue.add(() -> chunks.put(v, chunks.get(v).mapSecond(previousStage -> SUNLIGHT)));
            } catch (Throwable t) {
                t.printStackTrace();
                throw new RuntimeException(t);
            }
        });
        return true;
    }

    private void queueSunlight(Vector2i v) {
        if (!inSunlightQueue.contains(v)) inSunlightQueue.add(v);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean ensureSunlight(Vector2i v) {
        if (getChunkStatus(v) < SUNLIGHT) {
            queueSunlight(v);
            return false;
        }
        return true;
    }

    private boolean load(Vector2i v) {
        if (getChunkStatus(v) >= LOADED) return false;
        if (!ensureSunlight(v)) return false;
        if (!ensureSunlight(new Vector2i(v.x - 1, v.y))) return false;
        if (!ensureSunlight(new Vector2i(v.x + 1, v.y))) return false;
        if (!ensureSunlight(new Vector2i(v.x, v.y - 1))) return false;
        if (!ensureSunlight(new Vector2i(v.x, v.y + 1))) return false;
        if (!ensureSunlight(new Vector2i(v.x - 1, v.y - 1))) return false;
        if (!ensureSunlight(new Vector2i(v.x + 1, v.y - 1))) return false;
        if (!ensureSunlight(new Vector2i(v.x - 1, v.y + 1))) return false;
        if (!ensureSunlight(new Vector2i(v.x + 1, v.y + 1))) return false;
        runnableQueue.add(() -> {
            chunks.put(v, chunks.get(v).mapSecond(previousStage -> LOADED));
            onLoad.accept(chunks.get(v).swap().mapFirst(previousStage -> v));
        });
        return true;
    }

    @Override
    public void tick() {
        child.tick();
        for (int i = 0; i < 100 && !inGenerateQueue.isEmpty(); i++) {
            if (generate(inGenerateQueue.peek())) inGenerateQueue.remove();
            else inGenerateQueue.add(inGenerateQueue.remove());
        }
        for (int i = 0; i < 100 && !inSunlightQueue.isEmpty(); i++) {
            if (sunlight(inSunlightQueue.peek())) inSunlightQueue.remove();
            else inSunlightQueue.add(inSunlightQueue.remove());
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
        if (!inLoadQueue.contains(v)) inLoadQueue.add(v);
    }
}
