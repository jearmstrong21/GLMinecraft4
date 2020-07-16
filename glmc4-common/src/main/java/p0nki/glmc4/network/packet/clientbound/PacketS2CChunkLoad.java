package p0nki.glmc4.network.packet.clientbound;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import p0nki.glmc4.block.Chunk;
import p0nki.glmc4.network.PacketReadBuf;
import p0nki.glmc4.network.PacketWriteBuf;
import p0nki.glmc4.network.packet.Packet;
import p0nki.glmc4.network.packet.PacketDirection;
import p0nki.glmc4.network.packet.PacketTypes;

public class PacketS2CChunkLoad extends Packet<ClientPacketListener> {

    private static final Logger LOGGER = LogManager.getLogger();

    private int x;
    private int z;
    private Chunk chunk;

    public PacketS2CChunkLoad() {
        super(PacketDirection.SERVER_TO_CLIENT, PacketTypes.S2C_CHUNK_LOAD);
    }

    public PacketS2CChunkLoad(int x, int z, Chunk chunk) {
        super(PacketDirection.SERVER_TO_CLIENT, PacketTypes.S2C_CHUNK_LOAD);
        this.x = x;
        this.z = z;
        this.chunk = chunk;
    }

    @Override
    public void read(PacketReadBuf input) {
        x = input.readInt();
        z = input.readInt();
        chunk = input.read(new Chunk());
    }

    @Override
    public void write(PacketWriteBuf output) {
        output.writeInt(x);
        output.writeInt(z);
        output.writeEquivalent(chunk);
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
