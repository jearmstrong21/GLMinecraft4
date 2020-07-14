package p0nki.glmc4.network.packet.serverbound;

import p0nki.glmc4.network.packet.Packet;
import p0nki.glmc4.network.packet.PacketType;

public abstract class PacketC2S implements Packet<ServerPacketListener> {

    @Override
    public final PacketType getType() {
        return PacketType.CLIENT_TO_SERVER;
    }
}
