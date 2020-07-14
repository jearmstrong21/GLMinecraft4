package p0nki.glmc4.block;

import p0nki.glmc4.state.properties.Property;
import p0nki.glmc4.utils.MathUtils;

public class BlockState {

    private int id;
    private int meta;

    public BlockState(long value) {
        this.id = MathUtils.unpackFirst(value);
        this.meta = MathUtils.unpackSecond(value);
    }

    private BlockState(int id, int meta) {
        this.id = id;
        this.meta = meta;
    }

    public int getMeta() {
        return meta;
    }

    public BlockState copy() {
        return new BlockState(id, meta);
    }

    public <T> BlockState with(Property<T> property, T value) {
        meta = getBlock().getSchema().set(meta, property, value);
        return this;
    }

    @SuppressWarnings("unchecked")
    public BlockState withUnsafe(@SuppressWarnings("rawtypes") Property property, Object value) {
        meta = getBlock().getSchema().set(meta, property, value);
        return this;
    }

    @Override
    public String toString() {
        return Blocks.REGISTRY.get(id).getKey() + Blocks.REGISTRY.get(id).getValue().getSchema().toString(meta);
    }

    public Block getBlock() {
        return Blocks.REGISTRY.get(id).getValue();
    }

    public long toLong() {
        return MathUtils.pack(id, meta);
    }

}
