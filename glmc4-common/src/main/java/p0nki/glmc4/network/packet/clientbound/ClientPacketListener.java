package p0nki.glmc4.network.packet.clientbound;

import p0nki.glmc4.network.packet.PacketListener;

public interface ClientPacketListener extends PacketListener<ClientPacketListener> {

    void onPingRequest(PacketS2CPingRequest packet);

    void onPlayerLeave(PacketS2CPlayerLeave packet);

    void onChunkLoad(PacketS2CChunkLoad packet);

    void onHello(PacketS2CHello packet);

    void onPlayerJoin(PacketS2CPlayerJoin packet);

    void onChatMessage(PacketS2CChatMessage packet);

    void onEntityUpdate(PacketS2CEntityUpdate packet);

    void onEntitySpawn(PacketS2CEntitySpawn packet);

}
