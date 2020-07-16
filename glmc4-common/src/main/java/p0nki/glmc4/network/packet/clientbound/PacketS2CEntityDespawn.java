package p0nki.glmc4.network.packet.clientbound;

import p0nki.glmc4.network.PacketReadBuf;
import p0nki.glmc4.network.PacketWriteBuf;
import p0nki.glmc4.network.packet.Packet;
import p0nki.glmc4.network.packet.PacketDirection;
import p0nki.glmc4.network.packet.PacketTypes;

import java.util.UUID;

public class PacketS2CEntityDespawn extends Packet<ClientPacketListener> {

    private UUID uuid;

    public PacketS2CEntityDespawn() {
        super(PacketDirection.SERVER_TO_CLIENT, PacketTypes.S2C_ENTITY_DESPAWN);
    }

    public PacketS2CEntityDespawn(UUID uuid) {
        super(PacketDirection.SERVER_TO_CLIENT, PacketTypes.S2C_ENTITY_DESPAWN);
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    @Override
    public void read(PacketReadBuf input) {
        uuid = input.readUuid();
    }

    @Override
    public void write(PacketWriteBuf output) {
        output.writeUuid(uuid);
    }

    @Override
    public void apply(ClientPacketListener listener) {
        listener.onEntityDespawn(this);
    }
}
