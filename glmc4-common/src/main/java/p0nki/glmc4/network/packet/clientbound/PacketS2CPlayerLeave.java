package p0nki.glmc4.network.packet.clientbound;

import p0nki.glmc4.network.PacketReadBuf;
import p0nki.glmc4.network.PacketWriteBuf;

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
    public void read(PacketReadBuf input) {
        id = input.readString();
    }

    @Override
    public void write(PacketWriteBuf output) {
        output.writeString(id);
    }

    @Override
    public void apply(ClientPacketListener listener) {
        listener.onPlayerLeave(this);
    }

}
