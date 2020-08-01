package p0nki.glmc4.network.packet.clientbound;

import p0nki.glmc4.block.BlockState;
import p0nki.glmc4.network.PacketByteBuf;
import p0nki.glmc4.network.packet.Packet;
import p0nki.glmc4.network.packet.PacketTypes;

public class PacketS2CChunkUpdate extends Packet<ClientPacketListener> {

    private int x;
    private int y;
    private int z;
    private BlockState blockState;

    public PacketS2CChunkUpdate() {
        super(PacketTypes.S2C_CHUNK_UPDATE);
    }

    public PacketS2CChunkUpdate(int x, int y, int z, BlockState blockState) {
        super(PacketTypes.S2C_CHUNK_UPDATE);
        this.x = x;
        this.y = y;
        this.z = z;
        this.blockState = blockState;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public BlockState getBlockState() {
        return blockState;
    }

    @Override
    public void read(PacketByteBuf buf) {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
        blockState = new BlockState(buf.readLong());
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        buf.writeLong(blockState.toLong());
    }

    @Override
    public void apply(ClientPacketListener listener) {
        listener.onChunkUpdate(this);
    }
}
