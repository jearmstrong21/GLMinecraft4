package p0nki.glmc4.network.packet.serverbound;

import p0nki.glmc4.network.PacketByteBuf;
import p0nki.glmc4.network.packet.Packet;
import p0nki.glmc4.network.packet.PacketTypes;

public class PacketC2SPingResponse extends Packet<ServerPacketListener> {

    public PacketC2SPingResponse() {
        super(PacketTypes.C2S_PING_RESPONSE);
    }

    @Override
    public void read(PacketByteBuf buf) {

    }

    @Override
    public void write(PacketByteBuf buf) {

    }

    @Override
    public void apply(ServerPacketListener listener) {
        listener.onPingResponse(this);
    }
}
