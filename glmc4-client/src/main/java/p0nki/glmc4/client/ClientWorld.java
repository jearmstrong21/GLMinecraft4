package p0nki.glmc4.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.lwjgl.opengl.GL41;
import p0nki.glmc4.block.BlockState;
import p0nki.glmc4.block.Blocks;
import p0nki.glmc4.client.gl.Mesh;
import p0nki.glmc4.client.gl.Shader;
import p0nki.glmc4.client.gl.Texture;
import p0nki.glmc4.client.render.MeshData;
import p0nki.glmc4.client.render.WorldRenderContext;
import p0nki.glmc4.client.render.block.BlockRenderContext;
import p0nki.glmc4.client.render.block.BlockRenderer;
import p0nki.glmc4.client.render.block.BlockRenderers;
import p0nki.glmc4.client.render.block.RenderLayer;
import p0nki.glmc4.utils.Identifier;
import p0nki.glmc4.world.Chunk;
import p0nki.glmc4.world.ChunkNotLoadedException;
import p0nki.glmc4.world.World;
import p0nki.glmc4.world.gen.biomes.Biome;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ClientWorld implements World {

    private static final Logger LOGGER = LogManager.getLogger();

    private final Map<Vector2i, Chunk> chunks = new ConcurrentHashMap<>();
    private final Map<Vector2i, Map<RenderLayer, Mesh>> meshes = new HashMap<>();
    private final Shader shader;
    private final Texture texture;
    private final Queue<Runnable> runnableQueue = new ConcurrentLinkedQueue<>();
    private final Queue<Vector2i> inMeshQueue = new ConcurrentLinkedQueue<>();
    private final AtomicInteger computeCounter = new AtomicInteger(0);

    private void remesh(Vector2i v) {
        if (!inMeshQueue.contains(v)) inMeshQueue.add(v);
    }

    public String worldStats() {
        return String.format(
                "\n\tin meshs %s" +
                        "\n\trunnables %s" +
                        "\n\tchunks %s" +
                        "\n\tmeshes %s" +
                        "\n\tcomputes %s",
                inMeshQueue.size(),
                runnableQueue.size(),
                chunks.size(),
                meshes.size(),
                computeCounter.get());
    }

    public BlockState getOrAir(Vector3i position) {
        if (!isChunkLoaded(World.getChunkCoordinate(new Vector2i(position.x, position.z))))
            return Blocks.AIR.getDefaultState();
        return get(position);
    }

    public ClientWorld() {
        shader = Shader.create("chunk");
        texture = new Texture(Path.of("run", "atlas", "block.png"));
    }

    private MeshData mesh(int cx, int cz, Chunk chunk, RenderLayer renderLayer) {
        MeshData data = MeshData.chunk();
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 256; y++) {
                for (int z = 0; z < 16; z++) {
                    BlockState state = chunk.get(x, y, z);
                    int rx = x + cx * 16;
                    int rz = z + cz * 16;
                    BlockState testState = get(rx, y, rz);
                    if (!state.equals(testState)) {
                        LOGGER.error("Expected {} at {},{},{}, got {}", state, x, y, z, testState);
                    }
                    if (state.getBlock() == Blocks.AIR) continue;
                    Identifier identifier = Blocks.REGISTRY.get(state.getBlock()).getKey();
                    BlockRenderer renderer = BlockRenderers.REGISTRY.get(identifier).getValue();
                    if (renderer.getRenderLayer() != renderLayer) continue;
                    BlockRenderContext context = new BlockRenderContext(renderLayer, new Vector3i(x + cx * 16, y, z + cz * 16), get(rx - 1, y, rz), get(rx + 1, y, rz), get(rx, y - 1, rz), get(rx, y + 1, rz), get(rx, y, rz - 1), get(rx, y, rz + 1), state);
                    MeshData rendered = renderer.render(context);
                    rendered.multiply4f(0, new Matrix4f().translate(x, y, z));
                    data.append(rendered);
                }
            }
        }
        return data;
    }

    public float calculateAO(Vector3i position) {
        if (ClientSettings.SMOOTH_LIGHTING) {
            float total = 0;
            for (int i = -1; i <= 0; i++) {
                for (int j = -1; j <= 0; j++) {
                    for (int k = -1; k <= 0; k++) {
                        Vector3i v = new Vector3i(position.x + i, position.y + j, position.z + k);
                        total += get(v).getBlock().getAOContribution();
                    }
                }
            }
            return total / 8.0F;
        } else {
            return 1;
        }
    }

    public void loadChunk(Vector2i coordinate, Chunk chunk) {
        if (chunks.containsKey(coordinate)) throw new AssertionError(coordinate);
        Objects.requireNonNull(chunk);
        runnableQueue.add(() -> chunks.put(coordinate, chunk));
    }

    @Override
    public boolean isChunkLoaded(Vector2i chunkCoordinate) {
        if (chunks.containsKey(chunkCoordinate)) {
            Objects.requireNonNull(chunks.get(chunkCoordinate));
            return true;
        }
        return false;
    }

    @Override
    public boolean isClient() {
        return true;
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
    public Chunk getChunk(Vector2i chunkCoordinate) {
        if (!isChunkLoaded(chunkCoordinate)) throw new ChunkNotLoadedException(chunkCoordinate);
        return chunks.get(chunkCoordinate);
    }

    @Override
    public void update(Vector3i blockPos, BlockState blockState) {
        runnableQueue.add(() -> {
            if (get(blockPos).toLong() == blockState.toLong()) return;
            Vector2i chunkCoordinate = World.getChunkCoordinate(new Vector2i(blockPos.x, blockPos.z));
            if (!isChunkLoaded(chunkCoordinate))
                throw new ChunkNotLoadedException(chunkCoordinate.x, chunkCoordinate.y);
            Vector2i coordinateInChunk = World.getCoordinateInChunk(new Vector2i(blockPos.x, blockPos.z));
            getChunk(chunkCoordinate).set(coordinateInChunk.x, blockPos.y, coordinateInChunk.y, blockState);
            if (coordinateInChunk.x == 0 && coordinateInChunk.y == 0)
                remesh(new Vector2i(chunkCoordinate.x - 1, chunkCoordinate.y - 1));
            if (coordinateInChunk.x == 0 && coordinateInChunk.y == 15)
                remesh(new Vector2i(chunkCoordinate.x - 1, chunkCoordinate.y + 1));
            if (coordinateInChunk.x == 15 && coordinateInChunk.y == 0)
                remesh(new Vector2i(chunkCoordinate.x + 1, chunkCoordinate.y - 1));
            if (coordinateInChunk.x == 15 && coordinateInChunk.y == 15)
                remesh(new Vector2i(chunkCoordinate.x + 1, chunkCoordinate.y + 1));
            if (coordinateInChunk.x == 0) remesh(new Vector2i(chunkCoordinate.x - 1, chunkCoordinate.y));
            if (coordinateInChunk.x == 15) remesh(new Vector2i(chunkCoordinate.x + 1, chunkCoordinate.y));
            if (coordinateInChunk.y == 0) remesh(new Vector2i(chunkCoordinate.x, chunkCoordinate.y - 1));
            if (coordinateInChunk.y == 15) remesh(new Vector2i(chunkCoordinate.x, chunkCoordinate.y + 1));
            remesh(chunkCoordinate);
        });
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
        return chunks.get(chunkCoordinate).getBiome(coordinateInChunk.x, coordinateInChunk.y);
    }

    public void render(WorldRenderContext worldRenderContext) {
        shader.use();
        shader.set(worldRenderContext);
        shader.setTexture("tex", texture, 0);
        Consumer<RenderLayer> applyLayer = renderLayer -> meshes.forEach((pos, map) -> {
            shader.setFloat("x", pos.x * 16);
            shader.setFloat("z", pos.y * 16);
            map.get(renderLayer).triangles();
        });
        applyLayer.accept(RenderLayer.MAIN);

        GL41.glEnable(GL41.GL_BLEND);
        GL41.glBlendFunc(GL41.GL_SRC_ALPHA, GL41.GL_ONE_MINUS_SRC_ALPHA);
        applyLayer.accept(RenderLayer.TRANSPARENT);
        GL41.glDisable(GL41.GL_BLEND);

        if (ClientSettings.CHUNK_BORDERS) {
            chunks.keySet().forEach(key -> GLMC4Client.debugRenderer3D.renderCube(worldRenderContext, ClientSettings.CHUNK_COLOR, new Vector3f(key.x * 16, 0, key.y * 16), new Vector3f(16, 256, 16)));
            for (int y = 0; y < 256; y++) {
                Vector2i chunk = World.getChunkCoordinate(new Vector2i((int) GLMC4Client.getThisEntity().getPosition().x, (int) GLMC4Client.getThisEntity().getPosition().z));
                GLMC4Client.debugRenderer3D.renderCube(worldRenderContext, ClientSettings.CURRENT_CHUNK_COLOR, new Vector3f(chunk.x * 16, y, chunk.y * 16), new Vector3f(16, 1, 16));
            }
        }
        for (Vector2i v : chunks.keySet()) {
            if (!meshes.containsKey(v)) {
                remesh(v);
            }
        }

        if (!inMeshQueue.isEmpty()) {
            Vector2i v = inMeshQueue.remove();
            if (!chunks.containsKey(v) ||
                    !chunks.containsKey(new Vector2i(v.x - 1, v.y)) ||
                    !chunks.containsKey(new Vector2i(v.x + 1, v.y)) ||
                    !chunks.containsKey(new Vector2i(v.x, v.y - 1)) ||
                    !chunks.containsKey(new Vector2i(v.x, v.y + 1)) ||
                    !chunks.containsKey(new Vector2i(v.x - 1, v.y - 1)) ||
                    !chunks.containsKey(new Vector2i(v.x + 1, v.y - 1)) ||
                    !chunks.containsKey(new Vector2i(v.x - 1, v.y + 1)) ||
                    !chunks.containsKey(new Vector2i(v.x + 1, v.y + 1))) {
                inMeshQueue.add(v);
            } else {
                CompletableFuture.runAsync(() -> {
                    try {
                        computeCounter.incrementAndGet();
                        Chunk chunk = chunks.get(v);
                        Map<RenderLayer, MeshData> map = new EnumMap<>(RenderLayer.class);
                        for (RenderLayer renderLayer : RenderLayer.values()) {
                            map.put(renderLayer, mesh(v.x, v.y, chunk, renderLayer));
                        }
                        runnableQueue.add(() -> meshes.put(v, map.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> new Mesh(e.getValue())))));
                        computeCounter.decrementAndGet();
                    } catch (Throwable t) {
                        t.printStackTrace();
                        throw new RuntimeException(t);
                    }
                });
            }
        }

        while (!runnableQueue.isEmpty()) {
            runnableQueue.remove().run();
        }

    }

    public Optional<Biome> getOptionalBiome(Vector2i position) {
        try {
            return Optional.of(getBiome(position));
        } catch (ChunkNotLoadedException e) {
            return Optional.empty();
        }
    }
}
