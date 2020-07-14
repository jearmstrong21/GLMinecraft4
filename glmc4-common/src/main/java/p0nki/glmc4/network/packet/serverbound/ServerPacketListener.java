package p0nki.glmc4.network.packet.serverbound;

import p0nki.glmc4.network.packet.PacketListener;

public interface ServerPacketListener extends PacketListener<ServerPacketListener> {

    boolean isDead();

    void writePing();

    void onPingResponse(PacketC2SPingResponse packet);

}
