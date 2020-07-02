package p0nki.glmc4.client;

import p0nki.glmc4.client.assets.TextureAssembler;
import p0nki.glmc4.client.gl.Mesh;
import p0nki.glmc4.client.gl.MeshData;
import p0nki.glmc4.client.gl.Shader;
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

    private static void initialize() {
        shader = new Shader("chunk");
        MeshData data = new MeshData();
        data.addBuffer(2);
        data.addBuffer(3);
        data.appendBuffer(0, List.of(-.5, -.5, -.5, .5, .5, -.5, .5, .5));
        data.appendBuffer(1, List.of(0., 0., 0., 1., 0., 0., 0., 1., 0., 1., 2., 0.));
        data.appendTri(List.of(0, 1, 2, 1, 2, 3));
        mesh = new Mesh(data);
    }

    private static void frame(int frameCount) {
        shader.use();
        mesh.render();
    }

    private static void end() {

    }

    public static void main(String[] args) throws IOException {
//        runSocket();
        TextureAssembler.assemble(new Identifier("minecraft", "block"), "block");
        MCWindow.setInitializeCallback(GLMC4Client::initialize);
        MCWindow.setFrameCallback(GLMC4Client::frame);
        MCWindow.setEndCallback(GLMC4Client::end);
        MCWindow.start();
    }

}
