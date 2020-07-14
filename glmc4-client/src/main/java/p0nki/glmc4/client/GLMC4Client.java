package p0nki.glmc4.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import p0nki.glmc4.block.BlockState;
import p0nki.glmc4.block.Blocks;
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

import java.io.IOException;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class GLMC4Client {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Marker SOCKET = MarkerManager.getMarker("SOCKET");

    private static void runSocket() {
        Socket socket;
        try {
            socket = new Socket("localhost", 3333);
        } catch (IOException ioException) {
            LOGGER.fatal(SOCKET, "Connection refused", ioException);
            return;
        }
        LOGGER.info(SOCKET, "Socket connected");
        NetworkProtocol networkProtocol = new NetworkProtocol();
        ClientConnection<ClientPacketListener> connection;
        try {
            connection = new ClientConnection<>(socket, networkProtocol, PacketType.CLIENTBOUND, PacketType.SERVERBOUND);
        } catch (IOException ioException) {
            LOGGER.info(SOCKET, "Error creating connection", ioException);
            return;
        }
        ClientPacketListener packetListener = new ClientPacketListener(connection);
        connection.setPacketListener(packetListener);
        connection.startLoop();
        LOGGER.info(SOCKET, "Connected to localhost:3333");
    }

    private static Mesh mesh;
    private static Shader shader;
    private static Texture texture;

    private static void initialize() {
        BlockState[][][] terrain = new BlockState[16][256][16];
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int h = (x + z) / 4;
                for (int y = 0; y < 256; y++) {
                    if (y < h - 4) terrain[x][y][z] = Blocks.STONE.getDefaultState();
                    else if (y < h) terrain[x][y][z] = Blocks.DIRT.getDefaultState();
                    else if (y == h) terrain[x][y][z] = Blocks.GRASS.getDefaultState();
                    else terrain[x][y][z] = Blocks.AIR.getDefaultState();
                }
            }
        }
        LOGGER.info("16x256x16 chunk data created");
        shader = new Shader("chunk");
        MeshData data = MeshData.chunk();

        Set<Identifier> warnedIdentifiers = new HashSet<>();
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 256; y++) {
                for (int z = 0; z < 16; z++) {
                    if (terrain[x][y][z].getBlock() == Blocks.AIR) continue;
                    BlockState state = terrain[x][y][z];
                    BlockState xmi = Blocks.AIR.getDefaultState();
                    BlockState xpl = Blocks.AIR.getDefaultState();
                    BlockState ymi = Blocks.AIR.getDefaultState();
                    BlockState ypl = Blocks.AIR.getDefaultState();
                    BlockState zmi = Blocks.AIR.getDefaultState();
                    BlockState zpl = Blocks.AIR.getDefaultState();
                    if (x > 0) xmi = terrain[x - 1][y][z];
                    if (x < 15) xpl = terrain[x + 1][y][z];
                    if (y > 0) ymi = terrain[x][y - 1][z];
                    if (y < 255) ypl = terrain[x][y + 1][z];
                    if (z > 0) zmi = terrain[x][y][z - 1];
                    if (z < 15) zpl = terrain[x][y][z + 1];
                    Identifier identifier = Blocks.REGISTRY.get(state.getBlock()).getKey();
                    if (BlockRenderers.REGISTRY.hasKey(identifier)) {
                        BlockRenderer renderer = BlockRenderers.REGISTRY.get(identifier).getValue();
                        BlockRenderContext context = new BlockRenderContext(xmi, xpl, ymi, ypl, zmi, zpl, state);
                        MeshData rendered = renderer.render(context);
                        rendered.offset3f(0, x, y, z);
                        data.append(rendered);
                    } else if (!warnedIdentifiers.contains(identifier)) {
                        System.err.printf("[WARNING] No renderer found for %s. Will not warn again.%n", identifier);
                        warnedIdentifiers.add(identifier);
                    }
                }
            }
        }
        LOGGER.info("Mesh created");

        mesh = new Mesh(data);
        texture = new Texture(new LocalLocation("atlas/block.png"));
        LOGGER.info("Client initialization ended");
    }

    private static void frame(int frameCount) {
        shader.use();
        shader.setTexture("tex", texture, 0);
        shader.setMat4f("perspective", new Matrix4f().perspective((float) Math.toRadians(80), 1.0F, 0.001F, 100));
        float t = MCWindow.time();
        shader.setMat4f("view", new Matrix4f().lookAt(
                new Vector3f((float) (8.0f + 25.0F * Math.cos(t)), 10, (float) (8.0F - 25.0F * Math.cos(t - 4)))
                , new Vector3f(8, 0, 8), new Vector3f(0, 1, 0)));
        for (int x = -2; x < 3; x++) {
            for (int z = -2; z < 3; z++) {
                shader.setFloat("x", x * 16);
                shader.setFloat("z", z * 16);
                mesh.render();
            }
        }
    }

    private static void end() {

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
        runSocket();
//        runClient();
    }

}
