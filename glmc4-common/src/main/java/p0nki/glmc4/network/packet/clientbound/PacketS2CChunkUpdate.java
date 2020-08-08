package p0nki.glmc4.network.packet.clientbound;

import p0nki.glmc4.network.PacketByteBuf;
import p0nki.glmc4.network.packet.Packet;
import p0nki.glmc4.network.packet.PacketTypes;
import p0nki.glmc4.world.gen.BulkUpdate;

public class PacketS2CChunkUpdate extends Packet<ClientPacketListener> {

    private BulkUpdate bulkUpdate;

    public PacketS2CChunkUpdate() {
        super(PacketTypes.S2C_CHUNK_UPDATE);
    }

    public PacketS2CChunkUpdate(BulkUpdate bulkUpdate) {
        super(PacketTypes.S2C_CHUNK_UPDATE);
        this.bulkUpdate = bulkUpdate;
    }

    public BulkUpdate getBulkUpdate() {
        return bulkUpdate;
    }

    @Override
    public void read(PacketByteBuf buf) {
        bulkUpdate = buf.readEquivalent(new BulkUpdate());
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeEquivalent(bulkUpdate);
    }

    @Override
    public void apply(ClientPacketListener listener) {
        listener.onChunkUpdate(this);
    }
}
