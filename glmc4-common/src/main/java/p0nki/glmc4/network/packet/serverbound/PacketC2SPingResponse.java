package p0nki.glmc4.network.packet.serverbound;

import p0nki.glmc4.network.PacketReadBuf;
import p0nki.glmc4.network.PacketWriteBuf;
import p0nki.glmc4.network.packet.Packet;
import p0nki.glmc4.network.packet.PacketDirection;
import p0nki.glmc4.network.packet.PacketTypes;

public class PacketC2SPingResponse extends Packet<ServerPacketListener> {

    public PacketC2SPingResponse() {
        super(PacketDirection.CLIENT_TO_SERVER, PacketTypes.C2S_PING_RESPONSE);
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
