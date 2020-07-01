package p0nki.glmc4.network.packet.clientbound;

import p0nki.glmc4.utils.DataStreamUtils;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class PacketS2CPlayerLeave extends PacketS2C {

    private String id;

    public PacketS2CPlayerLeave() {

    }

    public PacketS2CPlayerLeave(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public void read(DataInput input) throws IOException {
        id = DataStreamUtils.readString(input);
    }

    @Override
    public void write(DataOutput output) throws IOException {
        DataStreamUtils.writeString(output, id);
    }

    @Override
    public void apply(ClientPacketListener listener) {
        listener.onPlayerLeave(this);
    }

}
