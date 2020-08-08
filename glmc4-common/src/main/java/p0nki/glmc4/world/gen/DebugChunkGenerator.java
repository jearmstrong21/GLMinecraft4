package p0nki.glmc4.world.gen;

import org.joml.Vector2i;
import p0nki.glmc4.block.BlockState;
import p0nki.glmc4.block.Blocks;
import p0nki.glmc4.block.blocks.GrassBlock;
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
                        for (int y = 0; y <= 84; y++) {
                            chunk.setBlock(x, y, z, Blocks.STONE.getDefaultState());
                        }
                        int h = 90;
                        BlockState mid = Blocks.WATER.getDefaultState();
                        BlockState top = Blocks.WATER.getDefaultState();
                        if (x < 8) {
                            mid = Blocks.DIRT.getDefaultState();
                            top = Blocks.GRASS.getDefaultState().with(GrassBlock.SNOWED, z < 8);
                            if (z < 8) h += 1;
                            if (z < 4) h += 1;
                            if (z < 2) h += 1;
                        }
                        for (int y = 85; y <= h; y++) {
                            chunk.setBlock(x, y, z, mid);
                        }
                        chunk.setBlock(x, h + 1, z, top);
//                        for (int y = 85; y <= (z < 8 ? 89 : 90); y++) {
//                            chunk.set(x, y, z, (x < 8 ? Blocks.DIRT : Blocks.WATER).getDefaultState());
//                        }
//                        if (x < 8) chunk.set(x, z < 8 ? 91 : 91, z, Blocks.GRASS.getDefaultState());
                    }
                }
                onLoad.accept(Pair.of(v, chunk));
            });
        }
    }
}
