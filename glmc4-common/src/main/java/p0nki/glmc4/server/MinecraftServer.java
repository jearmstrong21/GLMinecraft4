package p0nki.glmc4.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.joml.Vector3f;
import p0nki.glmc4.entity.Entity;
import p0nki.glmc4.entity.PlayerEntity;
import p0nki.glmc4.network.ClientConnection;
import p0nki.glmc4.network.packet.Packet;
import p0nki.glmc4.network.packet.clientbound.*;
import p0nki.glmc4.network.packet.serverbound.ServerPacketListener;
import p0nki.glmc4.utils.Words;

import java.util.*;

public class MinecraftServer {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Marker CHAT = MarkerManager.getMarker("CHAT");

    public static MinecraftServer INSTANCE = null;

    private final List<Entity> entities = new ArrayList<>();
    private final Map<String, ClientConnection<ServerPacketListener>> connections = new HashMap<>();
    private final List<ServerPlayer> players = new ArrayList<>();
    private final Set<String> playerIdsToRemove = new HashSet<>();

    private final Random random = new Random(System.currentTimeMillis());

    public MinecraftServer() {
        if (INSTANCE != null) throw new UnsupportedOperationException();
        INSTANCE = this;
        TimerTask pingPlayers = new TimerTask() {
            @Override
            public void run() {
                connections.values().forEach(connection -> {
                    if (connection.getPacketListener().isDead()) playerIdsToRemove.add(connection.getPlayer().getId());
                    else connection.getPacketListener().writePing();
                });
                playerIdsToRemove.forEach(id -> {
                    if (!connections.containsKey(id)) return;
                    writeGlobalChatMessage("SERVER LEAVE", connections.get(id).getPlayer().toString());
                    writeAll(new PacketS2CPlayerLeave(connections.get(id).getPlayer().getId()));
                    connections.get(id).getPacketListener().onDisconnected("Disconnected");
                    connections.get(id).close();
                    connections.remove(id);
                    for (int i = 0; i < players.size(); i++) {
                        if (players.get(i).getId().equals(id)) {
                            players.remove(i);
                            return;
                        }
                    }
                    throw new UnsupportedOperationException("Cannot remove player that does not exist");
                });
                playerIdsToRemove.clear();
                tick();
            }
        };
//        TestEntity testEntity = new TestEntity(new Vector3f(-0.5F, 5, -0.5F), UUID.randomUUID(), new Vector3f(1));
//        entities.add(testEntity);
//        PlayerEntity playerEntity = new PlayerEntity(new Vector3f(-0.5F, 5, -0.5F), UUID.randomUUID());
//        entities.add(playerEntity);
        new Timer().schedule(pingPlayers, 0, 100);
    }

    public void spawnEntity(Entity entity) {
        entities.add(entity);
        writeAll(new PacketS2CEntitySpawn(entity));
    }

    private void tick() {
        for (Entity entity : entities) {
            entity.tick(random);
            writeAll(new PacketS2CEntityUpdate(entity.getUuid(), entity.toTag()));
        }
    }

    public void writeGlobalChatMessage(String source, String message) {
        LOGGER.info(CHAT, "<{}> {}", source, message);
        writeAll(new PacketS2CChatMessage(source, message));
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public void joinPlayer(ClientConnection<ServerPacketListener> connection) {
        ServerPlayer player = new ServerPlayer(UUID.randomUUID().toString(), Words.generateUnique());
        connection.setPlayer(player);
        writeGlobalChatMessage("SERVER JOIN", connection.getPlayer().toString());
        writeAll(new PacketS2CPlayerJoin(player));
        players.add(player);
        connections.put(connection.getPlayer().getId(), connection);
        PlayerEntity playerEntity = new PlayerEntity(new Vector3f(10 * (float) Math.random() - 5, 15, 10 * (float) Math.random() - 5), UUID.randomUUID(), player);
        entities.add(playerEntity);
    }

    public List<ServerPlayer> getPlayers() {
        return players;
    }

    public void removeConnection(String id) {
        playerIdsToRemove.add(id);
    }

    private void writeAll(Packet<ClientPacketListener> packet) {
        connections.forEach((key, connection) -> connection.write(packet));
    }

}
