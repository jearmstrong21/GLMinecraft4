package p0nki.glmc4.network.packet.serverbound;

import p0nki.glmc4.network.ClientConnection;
import p0nki.glmc4.network.packet.PacketListener;
import p0nki.glmc4.network.packet.clientbound.PacketS2CHello;
import p0nki.glmc4.network.packet.clientbound.PacketS2CPingRequest;
import p0nki.glmc4.server.MinecraftServer;

public class ServerPacketListener implements PacketListener<ServerPacketListener> {

    private final ClientConnection<ServerPacketListener> connection;

    public ServerPacketListener(ClientConnection<ServerPacketListener> connection) {
        this.connection = connection;
    }

    public void onPingResponse(PacketC2SPingResponse packet) {
        lastPingResponse = System.currentTimeMillis();
    }

    private long lastPingResponse = -1;
    private boolean hasSentPing = false;

    public void writePing() {
        if (!hasSentPing) lastPingResponse = System.currentTimeMillis();
        hasSentPing = true;
        connection.write(new PacketS2CPingRequest());
    }

    public boolean isDead() {
        return hasSentPing && System.currentTimeMillis() - lastPingResponse > 3000;
    }

    @Override
    public void onConnected() {
        connection.write(new PacketS2CHello(connection.getPlayer(), MinecraftServer.INSTANCE.getPlayers()));
    }

    @Override
    public void onDisconnected(String reason) {

    }

    @Override
    public ClientConnection<ServerPacketListener> getConnection() {
        return connection;
    }
}
