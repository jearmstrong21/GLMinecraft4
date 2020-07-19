package p0nki.glmc4.network.packet.clientbound;

import p0nki.glmc4.network.PacketByteBuf;
import p0nki.glmc4.network.packet.Packet;
import p0nki.glmc4.network.packet.PacketTypes;

import java.util.UUID;

public class PacketS2CPlayerLeave extends Packet<ClientPacketListener> {

    private UUID uuid;

    public PacketS2CPlayerLeave() {
        super(PacketTypes.S2C_PLAYER_LEAVE);
    }

    public PacketS2CPlayerLeave(UUID uuid) {
        super(PacketTypes.S2C_PLAYER_LEAVE);
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    @Override
    public void read(PacketByteBuf buf) {
        uuid = buf.readUuid();
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeUuid(uuid);
    }

    @Override
    public void apply(ClientPacketListener listener) {
        listener.onPlayerLeave(this);
    }

}
