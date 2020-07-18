package p0nki.glmc4.tag;

import p0nki.glmc4.network.PacketWriteBuf;

public class DoubleTag extends AbstractNumberTag {

    public static final TagReader<DoubleTag> READER = input -> of(input.readDouble());

    private final double value;

    private DoubleTag(double value) {
        this.value = value;
    }

    public static DoubleTag of(double value) {
        return new DoubleTag(value);
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
        return (long) value;
    }

    @Override
    public float floatValue() {
        return (float) value;
    }

    @Override
    public double doubleValue() {
        return value;
    }

    @Override
    public void write(PacketWriteBuf output) {
        output.writeDouble(value);
    }

    @Override
    public TagReader<?> reader() {
        return READER;
    }

    @Override
    public byte type() {
        return DOUBLE;
    }
}
