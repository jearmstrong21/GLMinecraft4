package p0nki.glmc4.tag;

import p0nki.glmc4.network.PacketWriteBuf;

public class IntTag implements Tag<IntTag> {

    public static final TagReader<IntTag> READER = input -> new IntTag(input.readInt());

    private final int value;

    public IntTag(int value) {
        this.value = value;
    }

    public int get() {
        return value;
    }

    @Override
    public void write(PacketWriteBuf output) {
        output.writeInt(value);
    }

    @Override
    public TagReader<IntTag> reader() {
        return READER;
    }

    @Override
    public byte type() {
        return INT;
    }
}
