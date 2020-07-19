package p0nki.glmc4.network.packet.clientbound;

import p0nki.glmc4.entity.Entity;
import p0nki.glmc4.network.PacketByteBuf;
import p0nki.glmc4.network.packet.Packet;
import p0nki.glmc4.network.packet.PacketTypes;
import p0nki.glmc4.tag.CompoundTag;

import java.util.UUID;

public class PacketS2CEntityUpdate extends Packet<ClientPacketListener> {

    private UUID uuid;
    private CompoundTag newData;

    public PacketS2CEntityUpdate() {
        super(PacketTypes.S2C_ENTITY_UPDATE);
    }

    public PacketS2CEntityUpdate(Entity entity) {
        super(PacketTypes.S2C_ENTITY_UPDATE);
        this.uuid = entity.getUuid();
        this.newData = entity.toTag();
    }

    public UUID getUuid() {
        return uuid;
    }

    public CompoundTag getNewData() {
        return newData;
    }

    @Override
    public void read(PacketByteBuf buf) {
        uuid = buf.readUuid();
        newData = buf.readCompoundTag();
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeUuid(uuid);
        buf.writeTag(newData);
    }

    @Override
    public void apply(ClientPacketListener listener) {
        listener.onEntityUpdate(this);
    }
}
