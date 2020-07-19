package p0nki.glmc4.network.packet.clientbound;

import p0nki.glmc4.network.PacketByteBuf;
import p0nki.glmc4.network.packet.Packet;
import p0nki.glmc4.network.packet.PacketTypes;

import java.util.UUID;

public class PacketS2CEntityDespawn extends Packet<ClientPacketListener> {

    private UUID uuid;

    public PacketS2CEntityDespawn() {
        super(PacketTypes.S2C_ENTITY_DESPAWN);
    }

    public PacketS2CEntityDespawn(UUID uuid) {
        super(PacketTypes.S2C_ENTITY_DESPAWN);
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
        listener.onEntityDespawn(this);
    }
}
