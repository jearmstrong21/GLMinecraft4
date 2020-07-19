package p0nki.glmc4.network.packet.serverbound;

import p0nki.glmc4.entity.PlayerEntity;
import p0nki.glmc4.network.packet.PacketListener;

public abstract class ServerPacketListener extends PacketListener<ServerPacketListener> {

    private PlayerEntity playerEntity;

    public PlayerEntity getPlayerEntity() {
        return playerEntity;
    }

    public void setPlayerEntity(PlayerEntity playerEntity) {
        this.playerEntity = playerEntity;
    }

    public abstract void onPingResponse(PacketC2SPingResponse packet);

    public abstract void onPlayerMovement(PacketC2SPlayerMovement packet);

}
