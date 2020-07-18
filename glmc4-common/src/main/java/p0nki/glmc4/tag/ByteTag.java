package p0nki.glmc4.tag;

import p0nki.glmc4.network.PacketWriteBuf;

public class ByteTag extends AbstractNumberTag {

    public static final TagReader<ByteTag> READER = input -> of(input.readByte());

    private final byte value;

    private ByteTag(byte value) {
        this.value = value;
    }

    public static ByteTag of(byte value) {
        return new ByteTag(value);
    }

    @Override
    public void write(PacketWriteBuf output) {
        output.writeByte(value);
    }

    @Override
    public TagReader<ByteTag> reader() {
        return READER;
    }

    @Override
    public byte type() {
        return BYTE;
    }

    @Override
    public byte byteValue() {
        return value;
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
}
