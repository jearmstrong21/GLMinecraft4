package p0nki.glmc4.network.packet.clientbound;

import p0nki.glmc4.entity.Entity;
import p0nki.glmc4.entity.EntityType;
import p0nki.glmc4.network.PacketByteBuf;
import p0nki.glmc4.network.packet.Packet;
import p0nki.glmc4.network.packet.PacketTypes;

public class PacketS2CEntitySpawn extends Packet<ClientPacketListener> {

    private Entity entity;

    public PacketS2CEntitySpawn() {
        super(PacketTypes.S2C_ENTITY_SPAWN);
    }

    public PacketS2CEntitySpawn(Entity entity) {
        super(PacketTypes.S2C_ENTITY_SPAWN);
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

    @Override
    public void read(PacketByteBuf buf) {
        entity = EntityType.from(buf.readCompoundTag());
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeTag(entity.toTag());
    }

    @Override
    public void apply(ClientPacketListener listener) {
        listener.onEntitySpawn(this);
    }
}
