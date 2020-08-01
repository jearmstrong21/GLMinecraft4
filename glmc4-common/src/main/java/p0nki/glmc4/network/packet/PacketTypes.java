package p0nki.glmc4.network.packet;

import p0nki.glmc4.network.packet.clientbound.*;
import p0nki.glmc4.network.packet.serverbound.PacketC2SPingResponse;
import p0nki.glmc4.network.packet.serverbound.PacketC2SPlayerMovement;
import p0nki.glmc4.registry.Registry;
import p0nki.glmc4.utils.Identifier;

public class PacketTypes {

    public static Registry<PacketType<?>> REGISTRY;

    public static PacketType<PacketS2CHello> S2C_HELLO;

    public static PacketType<PacketS2CChatMessage> S2C_CHAT_MESSAGE;
    public static PacketType<PacketS2CChunkLoad> S2C_CHUNK_LOAD;
    public static PacketType<PacketS2CPingRequest> S2C_PING_REQUEST;
    public static PacketType<PacketS2CPlayerJoin> S2C_PLAYER_JOIN;
    public static PacketType<PacketS2CPlayerLeave> S2C_PLAYER_LEAVE;
    public static PacketType<PacketS2CEntityUpdate> S2C_ENTITY_UPDATE;
    public static PacketType<PacketS2CEntitySpawn> S2C_ENTITY_SPAWN;
    public static PacketType<PacketS2CEntityDespawn> S2C_ENTITY_DESPAWN;
    public static PacketType<PacketS2CDisconnectReason> S2C_DISCONNECT_REASON;
    public static PacketType<PacketS2CChunkUpdate> S2C_CHUNK_UPDATE;

    public static PacketType<PacketC2SPingResponse> C2S_PING_RESPONSE;
    public static PacketType<PacketC2SPlayerMovement> C2S_PLAYER_MOVEMENT;

    private static void register(String name, PacketType<?> packetType) {
        REGISTRY.register(new Identifier("minecraft", name), packetType);
    }

    public static void initialize() {
        REGISTRY = new Registry<>();
        register("s2c_hello", S2C_HELLO = new PacketType<>(PacketS2CHello::new));

        register("s2c_chat_message", S2C_CHAT_MESSAGE = new PacketType<>(PacketS2CChatMessage::new));
        register("s2c_chunk_load", S2C_CHUNK_LOAD = new PacketType<>(PacketS2CChunkLoad::new));
        register("s2c_ping_request", S2C_PING_REQUEST = new PacketType<>(PacketS2CPingRequest::new));
        register("s2c_player_join", S2C_PLAYER_JOIN = new PacketType<>(PacketS2CPlayerJoin::new));
        register("s2c_player_leave", S2C_PLAYER_LEAVE = new PacketType<>(PacketS2CPlayerLeave::new));
        register("s2c_entity_update", S2C_ENTITY_UPDATE = new PacketType<>(PacketS2CEntityUpdate::new));
        register("s2c_entity_spawn", S2C_ENTITY_SPAWN = new PacketType<>(PacketS2CEntitySpawn::new));
        register("s2c_entity_despawn", S2C_ENTITY_DESPAWN = new PacketType<>(PacketS2CEntityDespawn::new));
        register("s2c_disconnect_reason", S2C_DISCONNECT_REASON = new PacketType<>(PacketS2CDisconnectReason::new));
        register("s2c_chunk_update", S2C_CHUNK_UPDATE = new PacketType<>(PacketS2CChunkUpdate::new));

        register("c2s_ping_response", C2S_PING_RESPONSE = new PacketType<>(PacketC2SPingResponse::new));
        register("c2s_player_movement", C2S_PLAYER_MOVEMENT = new PacketType<>(PacketC2SPlayerMovement::new));

        if (REGISTRY.get(S2C_HELLO).getIndex() != 0) {
            throw new RuntimeException("HELLO packet somehow is not at index zero");
        }
    }

}
