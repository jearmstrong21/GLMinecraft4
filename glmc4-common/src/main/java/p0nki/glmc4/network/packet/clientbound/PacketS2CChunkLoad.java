package p0nki.glmc4.network.packet.clientbound;

import p0nki.glmc4.block.Chunk;
import p0nki.glmc4.network.PacketReadBuf;
import p0nki.glmc4.network.PacketWriteBuf;

public class PacketS2CChunkLoad extends PacketS2C {

    private int x;
    private int z;
    private Chunk chunk;

    public PacketS2CChunkLoad() {

    }

    public PacketS2CChunkLoad(int x, int z, Chunk chunk) {
        this.x = x;
        this.z = z;
        this.chunk = chunk;
    }

    @Override
    public void read(PacketReadBuf input) {
        x = input.readInt();
        z = input.readInt();
        chunk = new Chunk(input);
    }

    @Override
    public void write(PacketWriteBuf output) {
        output.writeInt(x);
        output.writeInt(z);
        chunk.write(output);
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
