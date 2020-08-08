package p0nki.glmc4.block;

import p0nki.glmc4.network.PacketByteBuf;
import p0nki.glmc4.state.Property;
import p0nki.glmc4.utils.math.VoxelShape;

import javax.annotation.CheckReturnValue;

public final class BlockState implements PacketByteBuf.Equivalent {

    private long value;

    public BlockState(long value) {
        this.value = value;
    }

    private BlockState(int id, int meta) {
        this.value = (((long) id) << 32) | (meta & 0xffffffffL);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BlockState that = (BlockState) o;

        return value == that.value;
    }

    @Override
    public int hashCode() {
        return (int) (value ^ (value >>> 32));
    }

    @CheckReturnValue
    public int getIndex() {
        return (int) (value >> 32);
    }

    @CheckReturnValue
    public int getMeta() {
        return (int) value;
    }

    @CheckReturnValue
    public BlockState copy() {
        return new BlockState(value);
    }

    @CheckReturnValue
    public <T> BlockState with(Property<T> property, T value) {
        return new BlockState(getIndex(), getBlock().getSchema().set(getMeta(), property, value));
    }

    @CheckReturnValue
    public <T> T get(Property<T> property) {
        return getBlock().getSchema().get(getMeta(), property);
    }

    @SuppressWarnings("unchecked")
    @CheckReturnValue
    public BlockState withUnsafe(@SuppressWarnings("rawtypes") Property property, Object value) {
        return new BlockState(getIndex(), getBlock().getSchema().set(getMeta(), property, value));
    }

    @Override
    public String toString() {
        return Blocks.REGISTRY.get(getIndex()).getKey() + Blocks.REGISTRY.get(getIndex()).getValue().getSchema().toString(getMeta());
    }

    //    @CheckReturnValue
    private Block getBlock() {
        return Blocks.REGISTRY.get(getIndex()).getValue();
    }

    @CheckReturnValue
    public long toLong() {
        return value;
    }

    public float getAOContribution() {
        return getBlock().getAOContribution(this);
    }

    public boolean isFullBlock() {
        return getBlock().isFullBlock(this);
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeLong(value);
    }

    @Override
    public void read(PacketByteBuf buf) {
        value = buf.readLong();
    }

    public VoxelShape getShape() {
        return getBlock().getShape(this);
    }

    public byte getBlockedSunlight() {
        return getBlock().getBlockedSunlight(this);
    }

}
