package p0nki.glmc4.network.packet.clientbound;

import p0nki.glmc4.network.packet.PacketListener;

public abstract class ClientPacketListener extends PacketListener<ClientPacketListener> {

    public abstract void onPingRequest(PacketS2CPingRequest packet);

    public abstract void onPlayerLeave(PacketS2CPlayerLeave packet);

    public abstract void onChunkLoad(PacketS2CChunkLoad packet);

    public abstract void onHello(PacketS2CHello packet);

    public abstract void onPlayerJoin(PacketS2CPlayerJoin packet);

    public abstract void onChatMessage(PacketS2CChatMessage packet);

    public abstract void onEntityUpdate(PacketS2CEntityUpdate packet);

    public abstract void onEntitySpawn(PacketS2CEntitySpawn packet);

    public abstract void onEntityDespawn(PacketS2CEntityDespawn packet);

    public abstract void onDisconnectReason(PacketS2CDisconnectReason packet);

    public abstract void onChunkUpdate(PacketS2CChunkUpdate packet);

}
