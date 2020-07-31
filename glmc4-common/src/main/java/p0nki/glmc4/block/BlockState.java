package p0nki.glmc4.block;

import p0nki.glmc4.state.Property;

import javax.annotation.CheckReturnValue;

public final class BlockState {

    private final long value;

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
    public int getId() {
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
        return new BlockState(getId(), getBlock().getSchema().set(getMeta(), property, value));
    }

    @CheckReturnValue
    public <T> T get(Property<T> property) {
        return getBlock().getSchema().get(getMeta(), property);
    }

    @SuppressWarnings("unchecked")
    @CheckReturnValue
    public BlockState withUnsafe(@SuppressWarnings("rawtypes") Property property, Object value) {
        return new BlockState(getId(), getBlock().getSchema().set(getMeta(), property, value));
    }

    @Override
    public String toString() {
        return Blocks.REGISTRY.get(getId()).getKey() + Blocks.REGISTRY.get(getId()).getValue().getSchema().toString(getMeta());
    }

    @CheckReturnValue
    public Block getBlock() {
        return Blocks.REGISTRY.get(getId()).getValue();
    }

    @CheckReturnValue
    public long toLong() {
        return value;
    }

}
