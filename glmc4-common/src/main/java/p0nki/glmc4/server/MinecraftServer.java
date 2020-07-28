package p0nki.glmc4.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.joml.Vector3f;
import p0nki.glmc4.entity.Entity;
import p0nki.glmc4.entity.PlayerEntity;
import p0nki.glmc4.network.packet.Packet;
import p0nki.glmc4.network.packet.clientbound.*;
import p0nki.glmc4.network.packet.serverbound.ServerPacketListener;
import p0nki.glmc4.utils.Words;

import java.util.*;

public class MinecraftServer {

    public static MinecraftServer INSTANCE;

    private static Logger LOGGER = LogManager.getLogger();
    private static Marker GAMELOOP = MarkerManager.getMarker("GAMELOOP");


    private final Map<UUID, ServerPlayer> players = new HashMap<>();
    private final Map<UUID, ServerPacketListener> listeners = new HashMap<>();
    private final Map<UUID, Entity> entities = new HashMap<>();
    private final ServerWorld serverWorld;

    public MinecraftServer() {
        serverWorld = new ServerWorld();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                tick();
            }
        }, 0, 50);
    }

    public ServerWorld getServerWorld() {
        return serverWorld;
    }

    public void writeAll(Packet<ClientPacketListener> packet) {
        if (packet instanceof PacketS2CChatMessage) {
            LOGGER.info(GAMELOOP, "<{}> {}", ((PacketS2CChatMessage) packet).getSource(), ((PacketS2CChatMessage) packet).getMessage());
        } else if (packet instanceof PacketS2CPlayerJoin) {
            LOGGER.info(GAMELOOP, "Player {} with name {} joined!", ((PacketS2CPlayerJoin) packet).getPlayer().getUuid(), ((PacketS2CPlayerJoin) packet).getPlayer().getName());
        } else if (packet instanceof PacketS2CPlayerLeave) {
            LOGGER.info(GAMELOOP, "Player {} with name {} left!", players.get(((PacketS2CPlayerLeave) packet).getUuid()).getUuid(), players.get(((PacketS2CPlayerLeave) packet).getUuid()).getName());
        }
        listeners.values().forEach(listener -> listener.getConnection().write(packet));
    }

    public void onJoin(ServerPacketListener listener) {
        if (listeners.containsValue(listener)) throw new IllegalArgumentException("Cannot join listener twice");
        ServerPlayer player = new ServerPlayer(UUID.randomUUID(), Words.generateUnique());
        PlayerEntity playerEntity = new PlayerEntity(new Vector3f(0.5F, 15, 0.3F), player);
        listener.setPlayer(player);
        listener.setPlayerEntity(playerEntity);
        listener.getConnection().write(new PacketS2CHello(player, new ArrayList<>(players.values()), new ArrayList<>(entities.values())));
        players.put(player.getUuid(), player);
        listeners.put(player.getUuid(), listener);
        writeAll(new PacketS2CPlayerJoin(player));
        spawnEntity(playerEntity);
    }

    public void onLeave(UUID uuid) {
        if (!players.containsKey(uuid)) throw new IllegalArgumentException("Cannot disconnect offline player");
        writeAll(new PacketS2CPlayerLeave(uuid));
        despawnEntity(uuid);
        players.remove(uuid);
        listeners.remove(uuid);
    }

    public void spawnEntity(Entity entity) {
        if (entities.containsKey(entity.getUuid()))
            throw new IllegalArgumentException("Cannot spawn already existing entity");
        entities.put(entity.getUuid(), entity);
        writeAll(new PacketS2CEntitySpawn(entity));
    }

    public void despawnEntity(UUID uuid) {
        if (!entities.containsKey(uuid)) throw new IllegalArgumentException("Cannot despawn nonexistent entity");
        entities.remove(uuid);
        writeAll(new PacketS2CEntityDespawn(uuid));
    }

    private void tick() {
        listeners.values().forEach(ServerPacketListener::tick);
        Random random = new Random(System.currentTimeMillis());
        entities.values().forEach(entity -> {
            entity.tick(random);
            writeAll(new PacketS2CEntityUpdate(entity));
        });
    }

}
