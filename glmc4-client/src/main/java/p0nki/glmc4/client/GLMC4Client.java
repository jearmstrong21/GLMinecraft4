package p0nki.glmc4.client;

import org.joml.Matrix4f;
import org.joml.Vector3d;
import org.joml.Vector3f;
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
import java.util.List;

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

        data.addQuad(0, new Vector3d(0, 1, 0), new Vector3d(0, 0, 1), new Vector3d(0, -1, 0));
        data.addQuad(1, BLOCK.getTexture(new Identifier("minecraft:faces_xmi")));
        data.appendTri(List.of(0, 1, 2, 1, 2, 3));

        data.addQuad(0, new Vector3d(1, 1, 1), new Vector3d(0, 0, -1), new Vector3d(0, -1, 0));
        data.addQuad(1, BLOCK.getTexture(new Identifier("minecraft:faces_xpl")));
        data.appendTri(List.of(4, 5, 6, 5, 6, 7));

        data.addQuad(0, new Vector3d(0, 0, 0), new Vector3d(1, 0, 0), new Vector3d(0, 0, 1));
        data.addQuad(1, BLOCK.getTexture(new Identifier("minecraft:faces_ymi")));
        data.appendTri(List.of(8, 9, 10, 9, 10, 11));

        data.addQuad(0, new Vector3d(0, 1, 0), new Vector3d(1, 0, 0), new Vector3d(0, 0, 1));
        data.addQuad(1, BLOCK.getTexture(new Identifier("minecraft:faces_ypl")));
        data.appendTri(List.of(12, 13, 14, 13, 14, 15));

        data.addQuad(0, new Vector3d(1, 1, 0), new Vector3d(-1, 0, 0), new Vector3d(0, -1, 0));
        data.addQuad(1, BLOCK.getTexture(new Identifier("minecraft:faces_zmi")));
        data.appendTri(List.of(16, 17, 18, 17, 18, 19));

        data.addQuad(0, new Vector3d(0, 1, 1), new Vector3d(1, 0, 0), new Vector3d(0, -1, 0));
        data.addQuad(1, BLOCK.getTexture(new Identifier("minecraft:faces_zpl")));
        data.appendTri(List.of(20, 21, 22, 21, 22, 23));

        mesh = new Mesh(data);
        texture = new Texture(new LocalLocation("atlas/block.png"));
    }

    private static void frame(int frameCount) {
        shader.use();
        shader.setTexture("tex", texture, 0);
        shader.setMat4f("perspective", new Matrix4f().perspective((float) Math.toRadians(80), 1.0F, 0.001F, 100));
        float t = MCWindow.time();
        shader.setMat4f("view", new Matrix4f().lookAt(
                new Vector3f((float) (0.5F + 2.0F * Math.cos(t)), (float) (0.5F - 1.5F * Math.cos(t - 2)), (float) (0.5F - 3.0F * Math.cos(t - 4)))
                , new Vector3f(0), new Vector3f(0, 1, 0)));
        mesh.render();
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
