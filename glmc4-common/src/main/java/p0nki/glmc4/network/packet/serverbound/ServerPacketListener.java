package p0nki.glmc4.network.packet.serverbound;

import p0nki.glmc4.network.packet.PacketListener;

public abstract class ServerPacketListener extends PacketListener<ServerPacketListener> {

    public abstract void onPingResponse(PacketC2SPingResponse packet);

    public abstract void onPlayerMovement(PacketC2SPlayerMovement packet);

}
