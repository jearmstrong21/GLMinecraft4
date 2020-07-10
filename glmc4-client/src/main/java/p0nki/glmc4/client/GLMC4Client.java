package p0nki.glmc4.client;

import org.joml.Matrix4f;
import org.joml.Vector3d;
import org.joml.Vector3f;
import p0nki.glmc4.client.assets.AtlasPosition;
import p0nki.glmc4.client.assets.LocalLocation;
import p0nki.glmc4.client.assets.TextureAssembler;
import p0nki.glmc4.client.gl.Mesh;
import p0nki.glmc4.client.gl.MeshData;
import p0nki.glmc4.client.gl.Shader;
import p0nki.glmc4.client.gl.Texture;
import p0nki.glmc4.network.ClientConnection;
import p0nki.glmc4.network.packet.NetworkProtocol;
import p0nki.glmc4.network.packet.PacketType;
import p0nki.glmc4.network.packet.clientbound.ClientPacketListener;
import p0nki.glmc4.utils.Identifier;

import java.io.IOException;
import java.net.Socket;

public class GLMC4Client {

    private static void runSocket() {
        Socket socket;
        try {
            socket = new Socket("localhost", 3333);
        } catch (IOException ioException) {
            System.out.println("Connection refused");
            return;
        }
        System.out.println("Socket connected");
        NetworkProtocol networkProtocol = new NetworkProtocol();
        ClientConnection<ClientPacketListener> connection;
        try {
            connection = new ClientConnection<>(socket, networkProtocol, PacketType.CLIENTBOUND, PacketType.SERVERBOUND);
        } catch (IOException ioException) {
            System.out.println("Error created connection object");
            return;
        }
        ClientPacketListener packetListener = new ClientPacketListener(connection);
        connection.setPacketListener(packetListener);
        connection.startLoop();
        System.out.println("Listening on localhost:3333");
    }

    private static Mesh mesh;
    private static Shader shader;
    private static Texture texture;

    private static void initialize() {
        TextureAssembler BLOCK = TextureAssembler.get(new Identifier("minecraft:block"), "block");
        shader = new Shader("chunk");
        MeshData data = new MeshData();

        data.addBuffer(3);
        data.addBuffer(2);

        boolean[][][] terrain = new boolean[16][16][16];
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int h = x + z;
                for (int y = 0; y < 16; y++) {
                    terrain[x][y][z] = y < h;
                }
            }
        }
        boolean d = true;
        AtlasPosition SIDE = BLOCK.getTexture(new Identifier("minecraft:grass_side"));
        AtlasPosition TOP = BLOCK.getTexture(new Identifier("minecraft:grass_top"));
        AtlasPosition BOTTOM = BLOCK.getTexture(new Identifier("minecraft:dirt"));
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 16; y++) {
                for (int z = 0; z < 16; z++) {
                    if (!terrain[x][y][z]) continue;
                    boolean xmi = d;
                    boolean xpl = d;
                    boolean ymi = d;
                    boolean ypl = d;
                    boolean zmi = d;
                    boolean zpl = d;
                    if (x > 0) xmi = !terrain[x - 1][y][z];
                    if (x < 15) xpl = !terrain[x + 1][y][z];
                    if (y > 0) ymi = !terrain[x][y - 1][z];
                    if (y < 15) ypl = !terrain[x][y + 1][z];
                    if (z > 0) zmi = !terrain[x][y][z - 1];
                    if (z < 15) zpl = !terrain[x][y][z + 1];
                    Vector3d o = new Vector3d(x, y, z);
                    if (xmi) data.addXmiQuad(0, 1, o, SIDE);
                    if (xpl) data.addXplQuad(0, 1, o, SIDE);
                    if (ymi) data.addYmiQuad(0, 1, o, BOTTOM);
                    if (ypl) data.addYplQuad(0, 1, o, TOP);
                    if (zmi) data.addZmiQuad(0, 1, o, SIDE);
                    if (zpl) data.addZplQuad(0, 1, o, SIDE);
                }
            }
        }

//        data.addXmiQuad(0, 1, new Vector3d(0), BLOCK.getTexture(new Identifier("minecraft:grass_side")));
//        data.addXplQuad(0, 1, new Vector3d(0), BLOCK.getTexture(new Identifier("minecraft:grass_side")));
//        data.addYmiQuad(0, 1, new Vector3d(0), BLOCK.getTexture(new Identifier("minecraft:dirt")));
//        data.addYplQuad(0, 1, new Vector3d(0), BLOCK.getTexture(new Identifier("minecraft:grass_top")));
//        data.addZmiQuad(0, 1, new Vector3d(0), BLOCK.getTexture(new Identifier("minecraft:grass_side")));
//        data.addZplQuad(0, 1, new Vector3d(0), BLOCK.getTexture(new Identifier("minecraft:grass_side")));

        mesh = new Mesh(data);
        texture = new Texture(new LocalLocation("atlas/block.png"));
    }

    private static void frame(int frameCount) {
        shader.use();
        shader.setTexture("tex", texture, 0);
        shader.setMat4f("perspective", new Matrix4f().perspective((float) Math.toRadians(80), 1.0F, 0.001F, 100));
        float t = MCWindow.time();
        shader.setMat4f("view", new Matrix4f().lookAt(
                new Vector3f((float) (8.0f + 5.0F * Math.cos(t)), 10, (float) (8.0F - 5.0F * Math.cos(t - 4)))
                , new Vector3f(8, 0, 8), new Vector3f(0, 1, 0)));
        mesh.render();
        System.out.println(MCWindow.getFps());
    }

    private static void end() {

    }

    public static void main(String[] args) {
//        runSocket();
        MCWindow.setInitializeCallback(GLMC4Client::initialize);
        MCWindow.setFrameCallback(GLMC4Client::frame);
        MCWindow.setEndCallback(GLMC4Client::end);
        MCWindow.start();
    }

}
