package p0nki.glmc4.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import p0nki.glmc4.entity.Entity;
import p0nki.glmc4.network.ClientConnection;
import p0nki.glmc4.network.packet.clientbound.*;
import p0nki.glmc4.network.packet.serverbound.PacketC2SPingResponse;
import p0nki.glmc4.server.ServerPlayer;

import java.util.UUID;
import java.util.stream.Collectors;

public class ClientPacketHandler implements ClientPacketListener {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Marker CHAT = MarkerManager.getMarker("CHAT");
    private static final Marker PLAYERS = MarkerManager.getMarker("PLAYERS");
    private static final Marker ENTITIES = MarkerManager.getMarker("ENTITIES");

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
        LOGGER.info(PLAYERS, "Player left: {}", packet.getUuid());
    }

    public void onChunkLoad(PacketS2CChunkLoad packet) {
        GLMC4Client.onLoadChunk(packet.getX(), packet.getZ(), packet.getChunk());
    }

    public void onHello(PacketS2CHello packet) {
        connection.setPlayer(packet.getYourPlayer());
        LOGGER.info(PLAYERS, "Your player is {}", packet.getYourPlayer().toString());
        LOGGER.info(PLAYERS, "Currently logged in players: {}", packet.getAllPlayers().stream().map(ServerPlayer::toString).collect(Collectors.joining(", ")));
        GLMC4Client.loadInitialEntities(packet.getAllEntities());
        LOGGER.info(ENTITIES, "Loaded initial {} entities: {}", packet.getAllEntities().size(), packet.getAllEntities().stream().map(Entity::getUuid).map(UUID::toString).collect(Collectors.joining(", ")));
    }

    public void onPlayerJoin(PacketS2CPlayerJoin packet) {
        LOGGER.info(PLAYERS, "Player joined: {}", packet.getPlayer());
    }

    @Override
    public void onDisconnected(String reason) {
        LOGGER.fatal("Disconnected: {}, exiting", reason);
        System.exit(1);
    }

    public void onChatMessage(PacketS2CChatMessage chatMessage) {
        LOGGER.info(CHAT, "<{}> {}", chatMessage.getSource(), chatMessage.getMessage());
    }

    @Override
    public void onEntityUpdate(PacketS2CEntityUpdate packet) {
        GLMC4Client.updateEntity(packet.getUuid(), packet.getNewData());
//        LOGGER.info(ENTITIES, "Entity {} updated", packet.getUuid());
    }

    @Override
    public void onEntitySpawn(PacketS2CEntitySpawn packet) {
        GLMC4Client.spawnEntity(packet.getEntity());
        LOGGER.info(ENTITIES, "Entity {} spawned", packet.getEntity().getUuid());
    }

    @Override
    public void onEntityDespawn(PacketS2CEntityDespawn packet) {
        GLMC4Client.despawnEntity(packet.getUuid());
        LOGGER.info(ENTITIES, "Entity {} despawned", packet.getUuid());
    }

    @Override
    public ClientConnection<ClientPacketListener> getConnection() {
        return connection;
    }
}
