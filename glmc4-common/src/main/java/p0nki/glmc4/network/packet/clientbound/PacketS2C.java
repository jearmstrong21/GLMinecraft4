package p0nki.glmc4.network.packet.clientbound;

import p0nki.glmc4.network.packet.Packet;
import p0nki.glmc4.network.packet.PacketType;

public abstract class PacketS2C implements Packet<ClientPacketListener> {

    @Override
    public final PacketType getType() {
        return PacketType.CLIENTBOUND;
    }

}
