package p0nki.glmc4.world.gen;

import org.joml.Vector2i;
import p0nki.glmc4.block.Blocks;
import p0nki.glmc4.server.ServerWorld;
import p0nki.glmc4.utils.data.Pair;
import p0nki.glmc4.world.Chunk;

import java.util.Collections;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

public class DebugChunkGenerator extends ChunkGenerator {

    private final Set<Vector2i> loadedChunks = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private final Queue<Runnable> runnableQueue = new ConcurrentLinkedQueue<>();

    public DebugChunkGenerator(Consumer<Pair<Vector2i, Chunk>> onLoad, ServerWorld serverWorld) {
        super(onLoad, serverWorld);
    }

    @Override
    public void tick() {
        while (!runnableQueue.isEmpty()) {
            runnableQueue.remove().run();
        }
    }

    @Override
    public void requestLoadChunk(Vector2i v) {
        if (!loadedChunks.contains(v)) {
            CompletableFuture.runAsync(() -> {
                loadedChunks.add(v);
                Chunk chunk = new Chunk();
                for (int x = 0; x < 16; x++) {
                    for (int z = 0; z < 16; z++) {
                        for (int y = 0; y <= 64; y++) {
                            chunk.set(x, y, z, Blocks.STONE.getDefaultState());
                        }
                        for (int y = 65; y <= 70; y++) {
                            chunk.set(x, y, z, (x < 8 ? Blocks.DIRT : Blocks.WATER).getDefaultState());
                        }
                        if (x < 8) chunk.set(x, 71, z, Blocks.GRASS.getDefaultState());
                    }
                }
                onLoad.accept(Pair.of(v, chunk));
            });
        }
    }
}
