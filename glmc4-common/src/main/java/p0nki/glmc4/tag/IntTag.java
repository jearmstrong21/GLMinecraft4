package p0nki.glmc4.tag;

import p0nki.glmc4.network.PacketByteBuf;

public class IntTag extends AbstractNumberTag {

    public static final TagReader<IntTag> READER = buf -> of(buf.readInt());

    private final int value;

    private IntTag(int value) {
        this.value = value;
    }

    public static IntTag of(int value) {
        return new IntTag(value);
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeInt(value);
    }

    @Override
    public TagReader<IntTag> reader() {
        return READER;
    }

    @Override
    public byte type() {
        return INT;
    }

    @Override
    public byte byteValue() {
        return (byte) value;
    }

    @Override
    public short shortValue() {
        return (short) value;
    }

    @Override
    public int intValue() {
        return value;
    }

    @Override
    public long longValue() {
        return value;
    }

    @Override
    public float floatValue() {
        return value;
    }

    @Override
    public double doubleValue() {
        return value;
    }
}
