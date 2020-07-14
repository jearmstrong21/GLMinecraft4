package p0nki.glmc4.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import p0nki.glmc4.Bootstrap;
import p0nki.glmc4.block.BlockState;
import p0nki.glmc4.block.Blocks;
import p0nki.glmc4.block.Chunk;
import p0nki.glmc4.client.assets.LocalLocation;
import p0nki.glmc4.client.gl.Mesh;
import p0nki.glmc4.client.gl.MeshData;
import p0nki.glmc4.client.gl.Shader;
import p0nki.glmc4.client.gl.Texture;
import p0nki.glmc4.client.render.block.BlockRenderContext;
import p0nki.glmc4.client.render.block.BlockRenderer;
import p0nki.glmc4.client.render.block.BlockRenderers;
import p0nki.glmc4.network.ClientConnection;
import p0nki.glmc4.network.packet.NetworkProtocol;
import p0nki.glmc4.network.packet.PacketType;
import p0nki.glmc4.network.packet.clientbound.ClientPacketListener;
import p0nki.glmc4.utils.Identifier;
import p0nki.glmc4.utils.MathUtils;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GLMC4Client {

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

    private static void runSocket() {
        try {
            socket = new Socket("localhost", 3333);
        } catch (IOException ioException) {
            LOGGER.fatal(SOCKET, "Connection refused", ioException);
            System.exit(1);
        }
        LOGGER.info(SOCKET, "Socket connected");
        NetworkProtocol networkProtocol = new NetworkProtocol();
        ClientConnection<ClientPacketListener> connection;
        try {
            connection = new ClientConnection<>(socket, networkProtocol, PacketType.SERVER_TO_CLIENT, PacketType.CLIENT_TO_SERVER);
        } catch (IOException ioException) {
            LOGGER.info(SOCKET, "Error creating connection", ioException);
            return;
        }
        ClientPacketListener packetListener = new ClientPacketHandler(connection);
        connection.setPacketListener(packetListener);
        connection.startLoop();
        LOGGER.info(SOCKET, "Connected to localhost:3333");
    }

    public static void onLoad(int x, int z, Chunk chunk) {
        LOGGER.trace(SOCKET, "Chunk received {}, {}", x, z);
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
                        rendered.offset3f(0, x, y, z);
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
        shader = new Shader("chunk");
        texture = new Texture(new LocalLocation("atlas/block.png"));
        LOGGER.info(RENDER, "Client initialized");
    }

    private static void frame(int frameCount) {
        shader.use();
        shader.setTexture("tex", texture, 0);
        shader.setMat4f("perspective", new Matrix4f().perspective((float) Math.toRadians(80), 1.0F, 0.001F, 300));
        float t = MCWindow.time();
        shader.setMat4f("view", new Matrix4f().lookAt(
                new Vector3f((float) (8.0f + 50.0F * Math.cos(t)), 30, (float) (8.0F - 50.0F * Math.cos(t - 4)))
                , new Vector3f(0, 0, 0), new Vector3f(0, 1, 0)));
        if (chunkLock.tryLock()) {
            for (Map.Entry<Long, Chunk> chunk : chunks.entrySet()) {
                meshes.put(chunk.getKey(), new Mesh(mesh(chunk.getValue())));
//                LOGGER.trace(RENDER, "Meshed chunk {}, {}", MathUtils.unpackFirst(chunk.getKey()), MathUtils.unpackSecond(chunk.getKey()));
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
        Bootstrap.initialize();
        runSocket();
        runClient();
    }

}
