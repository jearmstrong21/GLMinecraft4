package p0nki.glmc4.world.gen;

import org.joml.Vector2i;
import p0nki.glmc4.server.ServerWorld;
import p0nki.glmc4.utils.data.Pair;
import p0nki.glmc4.world.Chunk;

import java.util.function.Consumer;

public abstract class ChunkGenerator {

    protected final Consumer<Pair<Vector2i, Chunk>> onLoad;
    private final ServerWorld serverWorld;

    public ChunkGenerator(Consumer<Pair<Vector2i, Chunk>> onLoad, ServerWorld serverWorld) {
        this.onLoad = onLoad;
        this.serverWorld = serverWorld;
    }

    public ServerWorld getServerWorld() {
        return serverWorld;
    }

    public abstract void tick();

    public abstract void requestLoadChunk(Vector2i v);

}
