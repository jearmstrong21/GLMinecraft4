package p0nki.glmc4.network.packet;

import p0nki.glmc4.network.packet.clientbound.*;
import p0nki.glmc4.network.packet.serverbound.PacketC2SPingResponse;
import p0nki.glmc4.network.packet.serverbound.PacketC2SPlayerMovement;
import p0nki.glmc4.registry.Registry;
import p0nki.glmc4.utils.Identifier;

public class PacketTypes {

    public static final Registry<PacketType<?>> REGISTRY = new Registry<>();

    public static final PacketType<PacketS2CHello> S2C_HELLO = new PacketType<>(PacketS2CHello::new);

    public static final PacketType<PacketS2CChatMessage> S2C_CHAT_MESSAGE = new PacketType<>(PacketS2CChatMessage::new);
    public static final PacketType<PacketS2CChunkLoad> S2C_CHUNK_LOAD = new PacketType<>(PacketS2CChunkLoad::new);
    public static final PacketType<PacketS2CPingRequest> S2C_PING_REQUEST = new PacketType<>(PacketS2CPingRequest::new);
    public static final PacketType<PacketS2CPlayerJoin> S2C_PLAYER_JOIN = new PacketType<>(PacketS2CPlayerJoin::new);
    public static final PacketType<PacketS2CPlayerLeave> S2C_PLAYER_LEAVE = new PacketType<>(PacketS2CPlayerLeave::new);
    public static final PacketType<PacketS2CEntityUpdate> S2C_ENTITY_UPDATE = new PacketType<>(PacketS2CEntityUpdate::new);
    public static final PacketType<PacketS2CEntitySpawn> S2C_ENTITY_SPAWN = new PacketType<>(PacketS2CEntitySpawn::new);
    public static final PacketType<PacketS2CEntityDespawn> S2C_ENTITY_DESPAWN = new PacketType<>(PacketS2CEntityDespawn::new);
    public static final PacketType<PacketS2CDisconnectReason> S2C_DISCONNECT_REASON = new PacketType<>(PacketS2CDisconnectReason::new);

    public static final PacketType<PacketC2SPingResponse> C2S_PING_RESPONSE = new PacketType<>(PacketC2SPingResponse::new);
    public static final PacketType<PacketC2SPlayerMovement> C2S_PLAYER_MOVEMENT = new PacketType<>(PacketC2SPlayerMovement::new);

    public static void initialize() {
        REGISTRY.register(new Identifier("minecraft:s2c_hello"), S2C_HELLO);

        REGISTRY.register(new Identifier("minecraft:s2c_chat_message"), S2C_CHAT_MESSAGE);
        REGISTRY.register(new Identifier("minecraft:s2c_chunk_load"), S2C_CHUNK_LOAD);
        REGISTRY.register(new Identifier("minecraft:s2c_ping_request"), S2C_PING_REQUEST);
        REGISTRY.register(new Identifier("minecraft:s2c_player_join"), S2C_PLAYER_JOIN);
        REGISTRY.register(new Identifier("minecraft:s2c_player_leave"), S2C_PLAYER_LEAVE);
        REGISTRY.register(new Identifier("minecraft:s2c_entity_update"), S2C_ENTITY_UPDATE);
        REGISTRY.register(new Identifier("minecraft:s2c_entity_spawn"), S2C_ENTITY_SPAWN);
        REGISTRY.register(new Identifier("minecraft:s2c_entity_despawn"), S2C_ENTITY_DESPAWN);
        REGISTRY.register(new Identifier("minecraft:s2c_disconnect_reason"), S2C_DISCONNECT_REASON);

        REGISTRY.register(new Identifier("minecraft:c2s_ping_response"), C2S_PING_RESPONSE);
        REGISTRY.register(new Identifier("minecraft:c2s_player_movement"), C2S_PLAYER_MOVEMENT);

        if (REGISTRY.get(S2C_HELLO).getIndex() != 0) {
            throw new RuntimeException("HELLO packet somehow is not at index zero");
        }
    }

}
