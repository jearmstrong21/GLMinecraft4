package p0nki.glmc4.network.packet.clientbound;

import java.io.DataInput;
import java.io.DataOutput;

public class PacketS2CPingRequest extends PacketS2C {

    public PacketS2CPingRequest() {

    }

    @Override
    public void read(DataInput input) {

    }

    @Override
    public void write(DataOutput output) {

    }

    @Override
    public void apply(ClientPacketListener listener) {
        listener.onPingRequest(this);
    }
}
