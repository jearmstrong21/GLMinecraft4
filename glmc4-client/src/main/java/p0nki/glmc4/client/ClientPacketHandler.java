package p0nki.glmc4.client;

import org.joml.Vector2i;
import org.joml.Vector3i;
import p0nki.glmc4.network.packet.clientbound.*;
import p0nki.glmc4.network.packet.serverbound.PacketC2SPingResponse;
import p0nki.glmc4.network.packet.serverbound.PacketC2SPlayerMovement;

public class ClientPacketHandler extends ClientPacketListener {
    @Override
    public void onPingRequest(PacketS2CPingRequest packet) {
        getConnection().write(new PacketC2SPingResponse());
    }

    @Override
    public void onPlayerLeave(PacketS2CPlayerLeave packet) {

    }

    @Override
    public void onChunkLoad(PacketS2CChunkLoad packet) {
        GLMC4Client.onLoadChunk(packet.getX(), packet.getZ(), packet.getChunk());
    }

    @Override
    public void onHello(PacketS2CHello packet) {
        System.out.println("Your player ID is " + packet.getYourPlayer());
        setPlayer(packet.getYourPlayer());
        GLMC4Client.loadInitialEntities(packet.getAllEntities());
    }

    @Override
    public void onPlayerJoin(PacketS2CPlayerJoin packet) {

    }

    @Override
    public void onChatMessage(PacketS2CChatMessage packet) {
        System.out.printf("<%s> %s", packet.getSource(), packet.getMessage());
    }

    @Override
    public void onEntityUpdate(PacketS2CEntityUpdate packet) {
        GLMC4Client.updateEntity(packet.getUuid(), packet.getNewData());
    }

    @Override
    public void onEntitySpawn(PacketS2CEntitySpawn packet) {
        GLMC4Client.spawnEntity(packet.getEntity());
    }

    @Override
    public void onEntityDespawn(PacketS2CEntityDespawn packet) {
        GLMC4Client.despawnEntity(packet.getUuid());
    }

    @Override
    public void onConnected() {

    }

    @Override
    public void tick() {
        getConnection().write(new PacketC2SPlayerMovement(MCWindow.getKey('W'), MCWindow.getKey('S'), MCWindow.getKey('A'), MCWindow.getKey('D'), GLMC4Client.lookDir));
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

    @Override
    public void onChunkUpdate(PacketS2CChunkUpdate packet) {
        GLMC4Client.onChunkUpdate(new Vector3i(packet.getX(), packet.getY(), packet.getZ()), packet.getBlockState());
    }
}
