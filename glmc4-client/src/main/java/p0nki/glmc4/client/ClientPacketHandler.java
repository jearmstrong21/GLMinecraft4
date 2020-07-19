package p0nki.glmc4.client;

import p0nki.glmc4.network.packet.clientbound.*;

public class ClientPacketHandler extends ClientPacketListener {
    @Override
    public void onPingRequest(PacketS2CPingRequest packet) {
//        getConnection().write(new PacketC2SPingResponse());
    }

    @Override
    public void onPlayerLeave(PacketS2CPlayerLeave packet) {
        System.out.println("PLAYER LEAVE " + packet.getUuid());
    }

    @Override
    public void onChunkLoad(PacketS2CChunkLoad packet) {
        GLMC4Client.onLoadChunk(packet.getX(), packet.getZ(), packet.getChunk());
    }

    @Override
    public void onHello(PacketS2CHello packet) {
        System.out.println("Your player ID is " + packet.getYourPlayer());
        GLMC4Client.loadInitialEntities(packet.getAllEntities());
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
        GLMC4Client.updateEntity(packet.getUuid(), packet.getNewData());
    }

    @Override
    public void onEntitySpawn(PacketS2CEntitySpawn packet) {
        System.out.println("entity spawn");
        GLMC4Client.spawnEntity(packet.getEntity());
    }

    @Override
    public void onEntityDespawn(PacketS2CEntityDespawn packet) {
        System.out.println("entity despawn");
        GLMC4Client.despawnEntity(packet.getUuid());
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
        System.exit(0);
    }

    @Override
    public void onDisconnectReason(PacketS2CDisconnectReason packet) {
        System.out.println("DISCONNECT REASON " + packet.getReason());
    }
}
