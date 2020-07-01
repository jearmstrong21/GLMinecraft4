package p0nki.glmc4.tag;

import p0nki.glmc4.utils.DataStreamUtils;

import java.io.DataOutput;
import java.io.IOException;

public class StringTag implements Tag<StringTag> {

    public static final TagReader<StringTag> READER = input -> new StringTag(DataStreamUtils.readString(input));

    private final String value;

    public StringTag(String value) {
        this.value = value;
    }

    public String get() {
        return value;
    }

    @Override
    public void write(DataOutput output) throws IOException {
        DataStreamUtils.writeString(output, value);
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
