package p0nki.glmc4.tag;

import p0nki.glmc4.network.PacketWriteBuf;

public class EmptyTag implements Tag {

    public static final EmptyTag INSTANCE = new EmptyTag();

    public static final TagReader<EmptyTag> READER = input -> INSTANCE;

    private EmptyTag() {

    }

    @Override
    public void write(PacketWriteBuf output) {

    }

    @Override
    public TagReader<?> reader() {
        return READER;
    }

    @Override
    public byte type() {
        return EMPTY;
    }
}
