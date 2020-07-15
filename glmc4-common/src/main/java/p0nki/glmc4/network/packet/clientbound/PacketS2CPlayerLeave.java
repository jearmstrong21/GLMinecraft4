package p0nki.glmc4.network.packet.clientbound;

import p0nki.glmc4.network.PacketReadBuf;
import p0nki.glmc4.network.PacketWriteBuf;
import p0nki.glmc4.network.packet.Packet;
import p0nki.glmc4.network.packet.PacketDirection;
import p0nki.glmc4.network.packet.PacketTypes;

public class PacketS2CPlayerLeave extends Packet<ClientPacketListener> {

    private String id;

    public PacketS2CPlayerLeave() {
        super(PacketDirection.SERVER_TO_CLIENT, PacketTypes.S2C_PLAYER_LEAVE);
    }

    public PacketS2CPlayerLeave(String id) {
        super(PacketDirection.SERVER_TO_CLIENT, PacketTypes.S2C_PLAYER_LEAVE);
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
