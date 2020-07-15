package p0nki.glmc4.network.packet.clientbound;

import p0nki.glmc4.network.PacketReadBuf;
import p0nki.glmc4.network.PacketWriteBuf;
import p0nki.glmc4.network.packet.Packet;
import p0nki.glmc4.network.packet.PacketDirection;
import p0nki.glmc4.network.packet.PacketTypes;

public class PacketS2CPingRequest extends Packet<ClientPacketListener> {

    public PacketS2CPingRequest() {
        super(PacketDirection.SERVER_TO_CLIENT, PacketTypes.S2C_PING_REQUEST);
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
