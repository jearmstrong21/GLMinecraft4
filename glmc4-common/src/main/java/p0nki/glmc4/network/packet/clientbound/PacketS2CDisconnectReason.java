package p0nki.glmc4.network.packet.clientbound;

import p0nki.glmc4.network.PacketByteBuf;
import p0nki.glmc4.network.packet.Packet;
import p0nki.glmc4.network.packet.PacketTypes;

public class PacketS2CDisconnectReason extends Packet<ClientPacketListener> {

    private String reason;

    public PacketS2CDisconnectReason() {
        super(PacketTypes.S2C_DISCONNECT_REASON);
    }

    public PacketS2CDisconnectReason(String reason) {
        super(PacketTypes.S2C_DISCONNECT_REASON);
        this.reason = reason;
    }

    @Override
    public void read(PacketByteBuf buf) {
        reason = buf.readString();
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeString(reason);
    }

    public String getReason() {
        return reason;
    }

    @Override
    public void apply(ClientPacketListener listener) {
        listener.onDisconnectReason(this);
    }
}
