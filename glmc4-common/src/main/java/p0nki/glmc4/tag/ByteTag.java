package p0nki.glmc4.tag;

import java.io.DataOutput;
import java.io.IOException;

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
    public void write(DataOutput output) throws IOException {
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
