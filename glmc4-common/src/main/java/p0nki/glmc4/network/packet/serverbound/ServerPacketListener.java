package p0nki.glmc4.network.packet.serverbound;

import p0nki.glmc4.network.ClientConnection;
import p0nki.glmc4.network.packet.PacketListener;
import p0nki.glmc4.network.packet.clientbound.PacketS2CHello;
import p0nki.glmc4.server.MinecraftServer;

public class ServerPacketListener implements PacketListener<ServerPacketListener> {

    private final ClientConnection<ServerPacketListener> connection;

    public ServerPacketListener(ClientConnection<ServerPacketListener> connection) {
        this.connection = connection;
    }

    public void onPingResponse(PacketC2SPingResponse packet) {

    }

    @Override
    public void onConnected() {
        connection.write(new PacketS2CHello(connection.getPlayer(), MinecraftServer.INSTANCE.getPlayers()));
        MinecraftServer.INSTANCE.writeGlobalChatMessage("SERVER JOIN", connection.getPlayer().toString());
    }

    @Override
    public void onDisconnected(String reason) {
//        System.out.println("SERVER disconnected " + reason);
        MinecraftServer.INSTANCE.writeGlobalChatMessage("SERVER LEAVE", connection.getPlayer().toString());
    }

    @Override
    public ClientConnection<ServerPacketListener> getConnection() {
        return connection;
    }
}
