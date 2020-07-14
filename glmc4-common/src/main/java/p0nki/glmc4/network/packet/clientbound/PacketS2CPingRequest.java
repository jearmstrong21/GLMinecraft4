package p0nki.glmc4.network.packet.clientbound;

import p0nki.glmc4.network.PacketReadBuf;
import p0nki.glmc4.network.PacketWriteBuf;

public class PacketS2CPingRequest extends PacketS2C {

    public PacketS2CPingRequest() {

    }

    @Override
    public void read(PacketReadBuf input) {

    }

    @Override
    public void write(PacketWriteBuf output) {

    }

    @Override
    public void apply(ClientPacketListener listener) {
        listener.onPingRequest(this);
    }
}
