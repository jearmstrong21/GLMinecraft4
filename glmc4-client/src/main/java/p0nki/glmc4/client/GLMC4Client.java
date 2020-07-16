package p0nki.glmc4.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import p0nki.glmc4.block.BlockState;
import p0nki.glmc4.block.Blocks;
import p0nki.glmc4.block.Chunk;
import p0nki.glmc4.client.assets.LocalLocation;
import p0nki.glmc4.client.gl.*;
import p0nki.glmc4.client.render.block.BlockRenderContext;
import p0nki.glmc4.client.render.block.BlockRenderer;
import p0nki.glmc4.client.render.block.BlockRenderers;
import p0nki.glmc4.client.render.entity.EntityRenderer;
import p0nki.glmc4.client.render.entity.EntityRenderers;
import p0nki.glmc4.entity.Entity;
import p0nki.glmc4.entity.EntityType;
import p0nki.glmc4.entity.EntityTypes;
import p0nki.glmc4.network.ClientConnection;
import p0nki.glmc4.network.packet.PacketDirection;
import p0nki.glmc4.network.packet.clientbound.ClientPacketListener;
import p0nki.glmc4.tag.CompoundTag;
import p0nki.glmc4.utils.Identifier;
import p0nki.glmc4.utils.MathUtils;

import java.io.IOException;
import java.net.Socket;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GLMC4Client {

    public static Path RESOURCES;
    public static Path SHADERS;
//    (String... join) {
//        return Path.of(ClassLoader.getSystemClassLoader().getResource("./))
//    }

//    static {
//        try {
//            SHADERS = Path.of(ClassLoader.getSystemClassLoader().getResource(SHADERS).toURI());
//        } catch (URISyntaxException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public static final Path RUN = Path.of("run");

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Marker SOCKET = MarkerManager.getMarker("SOCKET");
    private static final Marker RENDER = MarkerManager.getMarker("RENDER");
    private static final Map<Long, Chunk> chunks = new HashMap<>();
    private static final Map<Long, Mesh> meshes = new HashMap<>();
    private final static Set<Identifier> warnedIdentifiers = new HashSet<>();
    private final static Lock chunkLock = new ReentrantLock();
    private static Socket socket;

    private static Shader shader;
    private static Texture texture;

    private static List<Entity> entities = new ArrayList<>();

    public static void loadInitialEntities(List<Entity> entities) {
        GLMC4Client.entities = entities;
    }

    public static void updateEntity(UUID uuid, CompoundTag newData) {
        for (Entity e : entities) {
            if (e.getUuid().equals(uuid)) {
                e.fromTag(newData);
                return;
            }
        }
    }

    private static void runSocket() {
        try {
            socket = new Socket("localhost", 3333);
        } catch (IOException ioException) {
            LOGGER.fatal(SOCKET, "Connection refused", ioException);
            System.exit(1);
        }
        LOGGER.info(SOCKET, "Socket connected");
        ClientConnection<ClientPacketListener> connection;
        try {
            connection = new ClientConnection<>(socket, PacketDirection.SERVER_TO_CLIENT, PacketDirection.CLIENT_TO_SERVER);
        } catch (IOException ioException) {
            LOGGER.info(SOCKET, "Error creating connection", ioException);
            return;
        }
        LOGGER.debug(SOCKET, "Connection created");
        ClientPacketListener packetListener = new ClientPacketHandler(connection);
        connection.setPacketListener(packetListener);
        connection.startLoop();
        LOGGER.info(SOCKET, "Connected to localhost:3333");
    }

    public static void onLoadChunk(int x, int z, Chunk chunk) {
        chunkLock.lock();
        chunks.put(MathUtils.pack(x, z), chunk);
        chunkLock.unlock();
    }

    private static MeshData mesh(Chunk chunk) {
        MeshData data = MeshData.chunk();
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 256; y++) {
                for (int z = 0; z < 16; z++) {
                    BlockState state = chunk.get(x, y, z);
                    if (state.getBlock() == Blocks.AIR) continue;
                    Identifier identifier = Blocks.REGISTRY.get(state.getBlock()).getKey();
                    if (BlockRenderers.REGISTRY.hasKey(identifier)) {
                        BlockRenderer renderer = BlockRenderers.REGISTRY.get(identifier).getValue();
                        BlockRenderContext context = new BlockRenderContext(chunk.getOrAir(x - 1, y, z), chunk.getOrAir(x + 1, y, z), chunk.getOrAir(x, y - 1, z), chunk.getOrAir(x, y + 1, z), chunk.getOrAir(x, y, z - 1), chunk.getOrAir(x, y, z + 1), state);
                        MeshData rendered = renderer.render(context);
                        rendered.mult4(0, new Matrix4f().translate(x, y, z));
                        data.append(rendered);
                    } else if (!warnedIdentifiers.contains(identifier)) {
                        LOGGER.warn(RENDER, "No renderer found for {}", state);
                        warnedIdentifiers.add(identifier);
                    }
                }
            }
        }
        return data;
    }

    private static void initialize() {
        shader = Shader.create("chunk");
        texture = new Texture(new LocalLocation("atlas/block.png"));
        LOGGER.info(RENDER, "Client initialized");
        EntityRenderers.REGISTRY.getEntries().forEach(entry -> entry.getValue().initialize());
    }

    private static void frame(int frameCount) {
        float t = MCWindow.time();
        Matrix4f perspective = new Matrix4f().perspective((float) Math.toRadians(80), 1.0F, 0.001F, 300);
        float camHeight = 30;
        float camRadius = 15;
        Matrix4f view = new Matrix4f().lookAt(
                new Vector3f((float) (camRadius * Math.cos(t)), camHeight, (float) (camRadius * Math.sin(t)))
                , new Vector3f(0, 0, 0), new Vector3f(0, 1, 0));
        WorldRenderContext context = new WorldRenderContext(perspective, view);
        shader.use();
        shader.setTexture("tex", texture, 0);
        shader.set(context);
        if (chunkLock.tryLock()) {
            for (Map.Entry<Long, Chunk> chunk : chunks.entrySet()) {
                meshes.put(chunk.getKey(), new Mesh(mesh(chunk.getValue())));
            }
            chunks.clear();
            chunkLock.unlock();
        } else {
            LOGGER.trace(RENDER, "Unable to obtain chunk lock. Will attempt next frame");
        }
        for (Map.Entry<Long, Mesh> chunk : meshes.entrySet()) {
            shader.setFloat("x", 16 * MathUtils.unpackFirst(chunk.getKey()));
            shader.setFloat("z", 16 * MathUtils.unpackSecond(chunk.getKey()));
            chunk.getValue().render();
        }

        for (Entity entity : entities) {
            EntityType<?> type = entity.getType();
            Identifier identifier = EntityTypes.REGISTRY.get(type).getKey();
            EntityRenderer<?> renderer = EntityRenderers.REGISTRY.get(identifier).getValue();
            renderer.render(context, entity);
        }
    }

    private static void end() {
        LOGGER.info(RENDER, "Rendering thread ended. A socket crash after this point is perfectly normal and expected.");
        try {
            socket.close();
        } catch (IOException ioException) {
            LOGGER.fatal(SOCKET, "Somehow closing the socket threw an exception", ioException);
        }
        LOGGER.info(SOCKET, "Socket closed");
    }

    private static void runClient() {
        try {
            MCWindow.setInitializeCallback(GLMC4Client::initialize);
            MCWindow.setFrameCallback(GLMC4Client::frame);
            MCWindow.setEndCallback(GLMC4Client::end);
            MCWindow.start();
        } catch (Error | RuntimeException error) {
            throw new RuntimeException("Crashes, fix or remove", error);
        }
    }

    public static void main(String[] args) {
        ClientBootstrap.initialize();
        runSocket();
        runClient();
    }

}
