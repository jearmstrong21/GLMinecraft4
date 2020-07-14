package p0nki.glmc4.tag;

import p0nki.glmc4.network.PacketWriteBuf;

public class ByteTag implements Tag<ByteTag> {

    public static final TagReader<ByteTag> READER = input -> new ByteTag(input.readByte());

    private final byte value;

    public ByteTag(byte value) {
        this.value = value;
    }

    public byte get() {
        return value;
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
}
