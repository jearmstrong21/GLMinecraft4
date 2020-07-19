package p0nki.glmc4.network.packet.clientbound;

import p0nki.glmc4.network.PacketByteBuf;
import p0nki.glmc4.network.packet.Packet;
import p0nki.glmc4.network.packet.PacketTypes;

public class PacketS2CPingRequest extends Packet<ClientPacketListener> {

    public PacketS2CPingRequest() {
        super(PacketTypes.S2C_PING_REQUEST);
    }

    @Override
    public void read(PacketByteBuf buf) {

    }

    @Override
    public void write(PacketByteBuf buf) {

    }

    @Override
    public void apply(ClientPacketListener listener) {
        listener.onPingRequest(this);
    }
}
