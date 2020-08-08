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
import p0nki.glmc4.utils.math.MathUtils;
import p0nki.glmc4.world.Chunk;
import p0nki.glmc4.world.ChunkNotLoadedException;
import p0nki.glmc4.world.World;
import p0nki.glmc4.world.gen.BulkUpdate;
import p0nki.glmc4.world.gen.biomes.Biome;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClientWorld extends World {

    private static final Logger LOGGER = LogManager.getLogger();

    private final Map<Vector2i, Chunk> chunks = new ConcurrentHashMap<>();
    private final Map<Vector2i, Map<RenderLayer, Mesh>> meshes = new HashMap<>();
    private final Shader shader;
    private final Texture texture;
    private final Queue<Runnable> runnableQueue = new ConcurrentLinkedQueue<>();
    private final Queue<Vector2i> inMeshQueue = new ConcurrentLinkedQueue<>();
    private final Set<Vector2i> currentlyMeshingChunks = new HashSet<>();
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
        if (!isChunkLoaded(MathUtils.getChunkCoordinate(position)))
            return Blocks.AIR.getDefaultState();
        return getBlock(position);
    }

    public ClientWorld() {
        shader = Shader.create("chunk");
        texture = new Texture(Path.of("run", "atlas", "block.png"));
    }

    public void acceptUpdate(BulkUpdate update) {
        applyUpdate(update);
        Stream.concat(
                update.getBlocks().keySet().stream().map(MathUtils::getChunkCoordinate),
                update.getSunlights().keySet().stream().map(MathUtils::getChunkCoordinate))
                .flatMap(MathUtils::fiveNeighbors).distinct().forEach(this::remesh);
    }

    private MeshData mesh(int cx, int cz, Chunk chunk, RenderLayer renderLayer) {
        MeshData data = MeshData.chunk();
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 256; y++) {
                for (int z = 0; z < 16; z++) {
                    BlockState state = chunk.getBlock(x, y, z);
                    int rx = x + cx * 16;
                    int rz = z + cz * 16;
                    BlockState testState = getBlock(rx, y, rz);
                    if (!state.equals(testState)) {
                        LOGGER.error("Expected {} at {},{},{}, got {}", state, x, y, z, testState);
                    }
                    if (state.getIndex() == Blocks.AIR.getIndex()) continue;
                    Identifier identifier = Blocks.REGISTRY.get(state.getIndex()).getKey();
                    BlockRenderer renderer = BlockRenderers.REGISTRY.get(identifier).getValue();
                    if (renderer.getRenderLayer() != renderLayer) continue;
                    BlockRenderContext context = new BlockRenderContext(renderLayer, new Vector3i(x + cx * 16, y, z + cz * 16), getBlock(rx - 1, y, rz), getBlock(rx + 1, y, rz), getBlock(rx, y - 1, rz), getBlock(rx, y + 1, rz), getBlock(rx, y, rz - 1), getBlock(rx, y, rz + 1), state);
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
                        total += getBlock(v).getAOContribution();
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
        runnableQueue.add(() -> {
            chunks.put(coordinate, chunk);
            remesh(coordinate);
        });
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
    public Chunk getChunk(Vector2i chunkCoordinate) {
        if (!isChunkLoaded(chunkCoordinate)) throw new ChunkNotLoadedException(chunkCoordinate);
        return chunks.get(chunkCoordinate);
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
                Vector2i chunk = MathUtils.getChunkCoordinate(MathUtils.floor(GLMC4Client.getThisEntity().getPosition()));
                GLMC4Client.debugRenderer3D.renderCube(worldRenderContext, ClientSettings.CURRENT_CHUNK_COLOR, new Vector3f(chunk.x * 16, y, chunk.y * 16), new Vector3f(16, 1, 16));
            }
        }

        if (!inMeshQueue.isEmpty()) {
            Vector2i v = inMeshQueue.remove();
            if (currentlyMeshingChunks.contains(v)) return;
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
                    currentlyMeshingChunks.add(v);
                    try {
                        computeCounter.incrementAndGet();
                        Chunk chunk = chunks.get(v);
                        Map<RenderLayer, MeshData> map = new EnumMap<>(RenderLayer.class);
                        for (RenderLayer renderLayer : RenderLayer.values()) {
                            map.put(renderLayer, mesh(v.x, v.y, chunk, renderLayer));
                        }
                        runnableQueue.add(() -> meshes.put(v, map.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> new Mesh(e.getValue())))));
//                        computeCounter.decrementAndGet();
                    } catch (Throwable t) {
                        t.printStackTrace();
                        throw new RuntimeException(t);
                    }
                    currentlyMeshingChunks.remove(v);
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
