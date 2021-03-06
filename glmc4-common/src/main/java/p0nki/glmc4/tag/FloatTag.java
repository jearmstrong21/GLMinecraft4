package p0nki.glmc4.tag;

import p0nki.glmc4.network.PacketByteBuf;

public class FloatTag extends AbstractNumberTag {

    public static final TagReader<FloatTag> READER = buf -> of(buf.readFloat());

    private final float value;

    private FloatTag(float value) {
        this.value = value;
    }

    public static FloatTag of(float value) {
        return new FloatTag(value);
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeFloat(value);
    }

    @Override
    public TagReader<FloatTag> reader() {
        return READER;
    }

    @Override
    public byte type() {
        return FLOAT;
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
        return value;
    }

    @Override
    public double doubleValue() {
        return value;
    }
}
