package p0nki.glmc4.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import p0nki.glmc4.network.ClientConnection;
import p0nki.glmc4.network.packet.clientbound.*;
import p0nki.glmc4.network.packet.serverbound.PacketC2SPingResponse;
import p0nki.glmc4.player.ServerPlayer;

import java.util.stream.Collectors;

public class ClientPacketHandler implements ClientPacketListener {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Marker CHAT = MarkerManager.getMarker("CHAT");
    private static final Marker PLAYERLIST = MarkerManager.getMarker("PLAYERLIST");

    private final ClientConnection<ClientPacketListener> connection;

    public ClientPacketHandler(ClientConnection<ClientPacketListener> connection) {
        this.connection = connection;
    }

    @Override
    public void onConnected() {
        LOGGER.info("Connected to server");
    }

    public void onPingRequest(PacketS2CPingRequest packet) {
        connection.write(new PacketC2SPingResponse());
    }

    public void onPlayerLeave(PacketS2CPlayerLeave packet) {
        LOGGER.info(PLAYERLIST, "Player left: {}", packet.getId());
    }

    public void onChunkLoad(PacketS2CChunkLoad packet) {
        LOGGER.info("Chunk load {}, {}", packet.getX(), packet.getZ());
        GLMC4Client.onLoad(packet.getX(), packet.getZ(), packet.getChunk());
    }

    public void onHello(PacketS2CHello packet) {
        connection.setPlayer(packet.getYourPlayer());
        LOGGER.info(PLAYERLIST, "Your player is {}", packet.getYourPlayer().toString());
        LOGGER.info(PLAYERLIST, "Currently logged in players: {}", packet.getAllPlayers().stream().map(ServerPlayer::toString).collect(Collectors.joining(", ")));
    }

    public void onPlayerJoin(PacketS2CPlayerJoin packet) {
        LOGGER.info(PLAYERLIST, "Player joined: {}", packet.getPlayer());
    }

    @Override
    public void onDisconnected(String reason) {
        LOGGER.fatal("Disconnected: {}", reason);
    }

    public void onChatMessage(PacketS2CChatMessage chatMessage) {
        LOGGER.info(CHAT, "<{}> {}", chatMessage.getSource(), chatMessage.getMessage());
    }

    @Override
    public ClientConnection<ClientPacketListener> getConnection() {
        return connection;
    }
}
