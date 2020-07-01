package p0nki.glmc4.network.packet.clientbound;

import p0nki.glmc4.network.ClientConnection;
import p0nki.glmc4.network.packet.PacketListener;
import p0nki.glmc4.network.packet.serverbound.PacketC2SPingResponse;

public class ClientPacketListener implements PacketListener<ClientPacketListener> {

    private final ClientConnection<ClientPacketListener> connection;

    public ClientPacketListener(ClientConnection<ClientPacketListener> connection) {
        this.connection = connection;
    }

    @Override
    public void onConnected() {
        System.out.println("CLIENT CONNECTED TO SERVER");
    }

    public void onPingRequest(PacketS2CPingRequest packet) {
        connection.write(new PacketC2SPingResponse());
    }

    public void onPlayerLeave(PacketS2CPlayerLeave packet) {
        System.out.println("ON PLAYER LEAVE. "+packet.getId());
    }

    public void onHello(PacketS2CHello packet) {
        connection.setPlayer(packet.getYourPlayer());
        System.out.println("YOUR PLAYER IS " + packet.getYourPlayer().getId() + ":" + packet.getYourPlayer().getName());
        packet.getAllPlayers().forEach(player -> System.out.println("Current players: " + player));
    }

    public void onPlayerJoin(PacketS2CPlayerJoin packet) {
        System.out.println("ON PLAYER JOINED. " + packet.getPlayer().toString());
    }

    @Override
    public void onDisconnected(String reason) {
        System.out.println("Disconnected " + reason);
    }

    public void onChatMessage(PacketS2CChatMessage chatMessage) {
        System.out.println(String.format("CHAT MESSAGE <%s> %s", chatMessage.getSource(), chatMessage.getMessage()));
    }

    @Override
    public ClientConnection<ClientPacketListener> getConnection() {
        return connection;
    }
}
