package p0nki.glmc4.network.packet.clientbound;

import p0nki.glmc4.network.PacketReadBuf;
import p0nki.glmc4.network.PacketWriteBuf;
import p0nki.glmc4.network.packet.Packet;
import p0nki.glmc4.network.packet.PacketDirection;
import p0nki.glmc4.network.packet.PacketTypes;
import p0nki.glmc4.tag.CompoundTag;

import java.util.UUID;

public class PacketS2CEntityUpdate extends Packet<ClientPacketListener> {

    private UUID uuid;
    private CompoundTag newData;

    public PacketS2CEntityUpdate() {
        super(PacketDirection.SERVER_TO_CLIENT, PacketTypes.S2C_ENTITY_UPDATE);
    }

    public PacketS2CEntityUpdate(UUID uuid, CompoundTag newData) {
        super(PacketDirection.SERVER_TO_CLIENT, PacketTypes.S2C_ENTITY_UPDATE);
        this.uuid = uuid;
        this.newData = newData;
    }

    public UUID getUuid() {
        return uuid;
    }

    public CompoundTag getNewData() {
        return newData;
    }

    @Override
    public void read(PacketReadBuf input) {
        uuid = input.readUuid();
        newData = CompoundTag.READER.read(input);
    }

    @Override
    public void write(PacketWriteBuf output) {
        output.writeUuid(uuid);
        newData.write(output);
    }

    @Override
    public void apply(ClientPacketListener listener) {
        listener.onEntityUpdate(this);
    }
}
