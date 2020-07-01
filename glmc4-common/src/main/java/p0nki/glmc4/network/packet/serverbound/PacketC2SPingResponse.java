package p0nki.glmc4.network.packet.serverbound;

import java.io.DataInput;
import java.io.DataOutput;

public class PacketC2SPingResponse extends PacketC2S {

    public PacketC2SPingResponse() {

    }

    @Override
    public void read(DataInput input) {

    }

    @Override
    public void write(DataOutput output) {

    }

    @Override
    public void apply(ServerPacketListener listener) {
        listener.onPingResponse(this);
    }
}
