package p0nki.glmc4.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import p0nki.glmc4.Bootstrap;
import p0nki.glmc4.network.ClientConnection;
import p0nki.glmc4.network.packet.PacketDirection;
import p0nki.glmc4.network.packet.serverbound.ServerPacketListener;

import java.io.IOException;
import java.net.ServerSocket;

public class GLMC4Server {

    private static final Logger LOGGER = LogManager.getLogger();

    public static void main(String[] args) throws IOException {
        Bootstrap.initialize();
        new MinecraftServer();
        ServerSocket serverSocket = new ServerSocket(3333);
        LOGGER.info("Listening on port 3333");
        //noinspection InfiniteLoopStatement
        while (true) {
            ClientConnection<ServerPacketListener> connection = new ClientConnection<>(serverSocket.accept(), PacketDirection.CLIENT_TO_SERVER, PacketDirection.SERVER_TO_CLIENT);
            ServerPacketListener packetListener = new ServerPacketHandler(connection);
            connection.setPacketListener(packetListener);
            MinecraftServer.INSTANCE.joinPlayer(connection);
            connection.startLoop();
        }
    }

}
