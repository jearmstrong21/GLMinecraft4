package p0nki.glmc4.network.packet.clientbound;

import p0nki.glmc4.entity.Entity;
import p0nki.glmc4.entity.EntityTypes;
import p0nki.glmc4.network.PacketReadBuf;
import p0nki.glmc4.network.PacketWriteBuf;
import p0nki.glmc4.network.packet.Packet;
import p0nki.glmc4.network.packet.PacketDirection;
import p0nki.glmc4.network.packet.PacketTypes;
import p0nki.glmc4.tag.CompoundTag;

public class PacketS2CEntitySpawn extends Packet<ClientPacketListener> {

    private Entity entity;

    public PacketS2CEntitySpawn() {
        super(PacketDirection.SERVER_TO_CLIENT, PacketTypes.S2C_ENTITY_SPAWN);
    }

    public PacketS2CEntitySpawn(Entity entity) {
        super(PacketDirection.SERVER_TO_CLIENT, PacketTypes.S2C_ENTITY_SPAWN);
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

    @Override
    public void read(PacketReadBuf input) {
        entity = EntityTypes.from(CompoundTag.READER.read(input));
    }

    @Override
    public void write(PacketWriteBuf output) {
        entity.toTag().write(output);
    }

    @Override
    public void apply(ClientPacketListener listener) {
        listener.onEntitySpawn(this);
    }
}
