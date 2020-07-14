package p0nki.glmc4.network.packet.serverbound;

import p0nki.glmc4.network.PacketReadBuf;
import p0nki.glmc4.network.PacketWriteBuf;

public class PacketC2SPingResponse extends PacketC2S {

    public PacketC2SPingResponse() {

    }

    @Override
    public void read(PacketReadBuf input) {

    }

    @Override
    public void write(PacketWriteBuf output) {

    }

    @Override
    public void apply(ServerPacketListener listener) {
        listener.onPingResponse(this);
    }
}
