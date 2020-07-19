package p0nki.glmc4.tag;

import p0nki.glmc4.network.PacketByteBuf;

public class ShortTag extends AbstractNumberTag {

    public static final TagReader<ShortTag> READER = buf -> of(buf.readShort());

    private final short value;

    private ShortTag(short value) {
        this.value = value;
    }

    public static ShortTag of(short value) {
        return new ShortTag(value);
    }

    @Override
    public byte byteValue() {
        return (byte) value;
    }

    @Override
    public short shortValue() {
        return value;
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

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeShort(value);
    }

    @Override
    public TagReader<?> reader() {
        return null;
    }

    @Override
    public byte type() {
        return SHORT;
    }
}
