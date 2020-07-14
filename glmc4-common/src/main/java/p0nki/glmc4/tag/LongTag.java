package p0nki.glmc4.tag;

import p0nki.glmc4.network.PacketWriteBuf;

public class LongTag implements Tag<LongTag> {

    public static final TagReader<LongTag> READER = input -> new LongTag(input.readLong());

    private final long value;

    public LongTag(long value) {
        this.value = value;
    }

    public long get() {
        return value;
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
}
