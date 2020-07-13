package p0nki.glmc4.state.block;

import p0nki.glmc4.state.properties.Property;

public class BlockState {

    private int id;
    private int meta;

    public BlockState(long value) {
        this.id = (int) (value >> 32);
        this.meta = (int) value;
    }

    public BlockState(int id, int meta) {
        this.id = id;
        this.meta = meta;
    }

    public BlockState copy() {
        return new BlockState(id, meta);
    }

    public <T> BlockState with(Property<T> property, T value) {
        meta = Blocks.REGISTRY.get(id).getValue().getSchema().set(meta, property, value);
        return this;
    }

    @SuppressWarnings("unchecked")
    public BlockState withUnsafe(@SuppressWarnings("rawtypes") Property property, Object value) {
        meta = Blocks.REGISTRY.get(id).getValue().getSchema().set(meta, property, value);
        return this;
    }

    @Override
    public String toString() {
        return Blocks.REGISTRY.get(id).getKey() + Blocks.REGISTRY.get(id).getValue().getSchema().toString(meta);
    }

    public long toLong() {
        return (((long) id) << 32) | (meta & 0xffffffffL);
    }

}
