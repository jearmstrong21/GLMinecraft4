package p0nki.glmc4.client;

import p0nki.glmc4.network.packet.clientbound.*;

public class ClientPacketHandler extends ClientPacketListener {
    @Override
    public void onPingRequest(PacketS2CPingRequest packet) {
        System.out.println("pingrequest");
    }

    @Override
    public void onPlayerLeave(PacketS2CPlayerLeave packet) {
        System.out.println("playerleave");
    }

    @Override
    public void onChunkLoad(PacketS2CChunkLoad packet) {
        System.out.println("chunkload");
    }

    @Override
    public void onHello(PacketS2CHello packet) {
        System.out.println("hello");
    }

    @Override
    public void onPlayerJoin(PacketS2CPlayerJoin packet) {
        System.out.println("playerjoin");
    }

    @Override
    public void onChatMessage(PacketS2CChatMessage packet) {
        System.out.printf("<%s> %s", packet.getSource(), packet.getMessage());
    }

    @Override
    public void onEntityUpdate(PacketS2CEntityUpdate packet) {
        System.out.println("entity update");
    }

    @Override
    public void onEntitySpawn(PacketS2CEntitySpawn packet) {
        System.out.println("entity spawn");
    }

    @Override
    public void onEntityDespawn(PacketS2CEntityDespawn packet) {
        System.out.println("entity despawn");
    }

    @Override
    public void onConnected() {
        System.out.println("Connected");
    }

    @Override
    public void tick() {
        System.out.println("Ticked");
    }

    @Override
    public void onDisconnected(String reason) {
        System.out.println("Disconnected");
    }
}
