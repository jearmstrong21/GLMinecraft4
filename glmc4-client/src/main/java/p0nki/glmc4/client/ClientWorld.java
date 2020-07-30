package p0nki.glmc4.client;

import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.joml.Vector3i;
import p0nki.glmc4.block.*;
import p0nki.glmc4.client.gl.Mesh;
import p0nki.glmc4.client.gl.Shader;
import p0nki.glmc4.client.gl.Texture;
import p0nki.glmc4.client.render.MeshData;
import p0nki.glmc4.client.render.WorldRenderContext;
import p0nki.glmc4.client.render.block.BlockRenderContext;
import p0nki.glmc4.client.render.block.BlockRenderer;
import p0nki.glmc4.client.render.block.BlockRenderers;
import p0nki.glmc4.utils.Identifier;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class ClientWorld implements World {

    private final ReentrantLock chunkLock = new ReentrantLock(true);
    private final Map<Vector2i, Chunk> chunks = new HashMap<>();
    private final Map<Vector2i, Mesh> meshes = new HashMap<>();
    private final Shader shader;
    private final Texture texture;

    public ClientWorld() {
        shader = Shader.create("chunk");
        texture = new Texture(Path.of("run", "atlas", "block.png"));
    }

    private static MeshData mesh(int cx, int cz, Chunk chunk) {
        MeshData data = MeshData.chunk();
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 256; y++) {
                for (int z = 0; z < 16; z++) {
                    BlockState state = chunk.get(x, y, z);
                    if (state.getBlock() == Blocks.AIR) continue;
                    Identifier identifier = Blocks.REGISTRY.get(state.getBlock()).getKey();
                    BlockRenderer renderer = BlockRenderers.REGISTRY.get(identifier).getValue();
                    BlockRenderContext context = new BlockRenderContext(new Vector3i(x + cx * 16, y, z + cz * 16), chunk.getOrAir(x - 1, y, z), chunk.getOrAir(x + 1, y, z), chunk.getOrAir(x, y - 1, z), chunk.getOrAir(x, y + 1, z), chunk.getOrAir(x, y, z - 1), chunk.getOrAir(x, y, z + 1), state);
                    MeshData rendered = renderer.render(context);
                    rendered.multiply4f(0, new Matrix4f().translate(x, y, z));
                    data.append(rendered);
                }
            }
        }
        return data;
    }

    public float calculateAO(Vector3i position) {
        float total = 0;
        for (int i = -1; i <= 0; i++) {
            for (int j = -1; j <= 0; j++) {
                for (int k = -1; k <= 0; k++) {
                    Vector3i v = new Vector3i(position.x + i, position.y + j, position.z + k);
                    float f = 1;
                    if (isChunkLoaded(World.getChunkCoordinate(new Vector2i(v.x, v.z)))) {
                        if (v.y >= 0 && v.y <= 255) {
                            if (get(v).getBlock() != Blocks.AIR) {
                                f = 0;
                            }
                        }
                    }
                    total += f;
                }
            }
        }
        return total / 8.0F;
    }

    public void loadChunk(Vector2i coordinate, Chunk chunk) {
        if (chunks.containsKey(coordinate)) throw new AssertionError(coordinate);
        chunkLock.lock();
        chunks.put(coordinate, chunk);
        chunkLock.unlock();
    }

    @Override
    public boolean isChunkLoaded(Vector2i chunkCoordinate) {
        return chunks.containsKey(chunkCoordinate);
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
    public Chunk getChunk(Vector2i chunkCoordinate) {
        if (!isChunkLoaded(chunkCoordinate)) throw new ChunkNotLoadedException(chunkCoordinate);
        return chunks.get(chunkCoordinate);
    }

    @Override
    public List<Vector2i> getLoadedChunks() {
        return List.copyOf(chunks.keySet());
    }

    public void render(WorldRenderContext worldRenderContext) {
        shader.use();
        shader.set(worldRenderContext);
        shader.setTexture("tex", texture, 0);
        for (Map.Entry<Vector2i, Mesh> chunk : meshes.entrySet()) {
            int x = 16 * chunk.getKey().x;
            int z = 16 * chunk.getKey().y;
            shader.setFloat("x", x);
            shader.setFloat("z", z);
            chunk.getValue().triangles();
        }
        if (chunkLock.tryLock()) {
            for (Vector2i v : chunks.keySet()) {
                if (!meshes.containsKey(v)) {
                    meshes.put(v, new Mesh(mesh(v.x, v.y, chunks.get(v))));
                }
            }
            chunkLock.unlock();
        }
    }
}
