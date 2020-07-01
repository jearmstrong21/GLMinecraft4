package p0nki.glmc4.server;

import p0nki.glmc4.network.ClientConnection;
import p0nki.glmc4.network.packet.clientbound.PacketS2C;
import p0nki.glmc4.network.packet.clientbound.PacketS2CChatMessage;
import p0nki.glmc4.network.packet.clientbound.PacketS2CPingRequest;
import p0nki.glmc4.network.packet.serverbound.ServerPacketListener;
import p0nki.glmc4.player.ServerPlayer;
import p0nki.glmc4.utils.Words;

import java.util.*;

public class MinecraftServer {

    public static MinecraftServer INSTANCE = null;

    private final Map<String, ClientConnection<ServerPacketListener>> connections = new HashMap<>();
    private final List<ServerPlayer> players = new ArrayList<>();

    public MinecraftServer() {
        if (INSTANCE != null) throw new UnsupportedOperationException();
        INSTANCE = this;
        TimerTask pingPlayers = new TimerTask() {
            @Override
            public void run() {
                writeAll(new PacketS2CPingRequest());
                playerIdsToRemove.forEach(id -> {
                    if (!connections.containsKey(id)) return;
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
            }
        };
        new Timer().schedule(pingPlayers, 0, 1000);
    }

    public void writeGlobalChatMessage(String source, String message) {
        System.out.println(String.format("GLOBAL CHAT MESSAGE <%s> %s", source, message));
        writeAll(new PacketS2CChatMessage(source, message));
    }

    public void joinPlayer(ClientConnection<ServerPacketListener> connection) {
        ServerPlayer player = new ServerPlayer(UUID.randomUUID().toString(), Words.generateUnique());
        connection.setPlayer(player);
        players.add(player);
        connections.put(connection.getPlayer().getId(), connection);
    }

    public List<ServerPlayer> getPlayers() {
        return players;
    }

    private final Set<String> playerIdsToRemove = new HashSet<>();

    public void removeConnection(String id) {
        playerIdsToRemove.add(id);
    }

    private void writeAll(PacketS2C packet) {
        connections.forEach((key, connection) -> connection.write(packet));
    }

}
