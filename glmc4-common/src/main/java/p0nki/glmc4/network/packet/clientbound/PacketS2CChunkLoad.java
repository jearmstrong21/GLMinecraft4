package p0nki.glmc4.network.packet.clientbound;

import p0nki.glmc4.block.Chunk;
import p0nki.glmc4.network.PacketByteBuf;
import p0nki.glmc4.network.packet.Packet;
import p0nki.glmc4.network.packet.PacketTypes;

public class PacketS2CChunkLoad extends Packet<ClientPacketListener> {

    private int x;
    private int z;
    private Chunk chunk;

    public PacketS2CChunkLoad() {
        super(PacketTypes.S2C_CHUNK_LOAD);
    }

    public PacketS2CChunkLoad(int x, int z, Chunk chunk) {
        super(PacketTypes.S2C_CHUNK_LOAD);
        this.x = x;
        this.z = z;
        this.chunk = chunk;
    }

    @Override
    public void read(PacketByteBuf buf) {
        x = buf.readInt();
        z = buf.readInt();
        chunk = buf.readEquivalent(new Chunk());
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(z);
        buf.writeEquivalent(chunk);
    }

    @Override
    public void apply(ClientPacketListener listener) {
        listener.onChunkLoad(this);
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public Chunk getChunk() {
        return chunk;
    }
}
