package p0nki.glmc4.tag;

import p0nki.glmc4.network.PacketWriteBuf;

public class StringTag implements Tag<StringTag> {

    public static final TagReader<StringTag> READER = input -> new StringTag(input.readString());

    private final String value;

    public StringTag(String value) {
        this.value = value;
    }

    public String get() {
        return value;
    }

    @Override
    public void write(PacketWriteBuf output) {
        output.writeString(value);
    }

    @Override
    public TagReader<StringTag> reader() {
        return READER;
    }

    @Override
    public byte type() {
        return STRING;
    }
}
