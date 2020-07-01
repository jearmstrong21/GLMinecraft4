package p0nki.glmc4.client;

import p0nki.glmc4.network.ClientConnection;
import p0nki.glmc4.network.packet.PacketHandler;
import p0nki.glmc4.network.packet.PacketType;
import p0nki.glmc4.network.packet.clientbound.ClientPacketListener;

import java.io.IOException;
import java.net.Socket;

public class GLMC4Client {

    public static void main(String[] args) {
        Socket socket = null;
        try {
            socket = new Socket("localhost", 3333);
        } catch (IOException ioException) {
            System.out.println("Connection refused");
            return;
        }
        System.out.println("Socket connected");
        PacketHandler packetHandler = new PacketHandler();
        ClientConnection<ClientPacketListener> connection;
        try {
            connection = new ClientConnection<>(socket, packetHandler, PacketType.CLIENTBOUND, PacketType.SERVERBOUND);
        } catch (IOException ioException) {
            System.out.println("Error created connection object");
            return;
        }
        ClientPacketListener packetListener = new ClientPacketListener(connection);
        connection.setPacketListener(packetListener);
        connection.startLoop();
        System.out.println("Listening on localhost:3333");
    }

}
