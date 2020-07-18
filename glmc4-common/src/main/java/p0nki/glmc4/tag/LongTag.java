package p0nki.glmc4.tag;

import p0nki.glmc4.network.PacketWriteBuf;

public class LongTag extends AbstractNumberTag {

    public static final TagReader<LongTag> READER = input -> new LongTag(input.readLong());

    private final long value;

    private LongTag(long value) {
        this.value = value;
    }

    public static LongTag of(long value) {
        return new LongTag(value);
    }

    @Override
    public void write(PacketWriteBuf output) {
        output.writeLong(value);
    }

    @Override
    public TagReader<LongTag> reader() {
        return READER;
    }

    @Override
    public byte type() {
        return LONG;
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
        return (int) value;
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
