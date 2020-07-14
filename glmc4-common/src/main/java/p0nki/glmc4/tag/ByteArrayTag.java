package p0nki.glmc4.tag;

import org.apache.commons.lang3.ArrayUtils;
import p0nki.glmc4.network.PacketWriteBuf;

import java.util.AbstractList;

public class ByteArrayTag extends AbstractList<Byte> implements Tag<ByteArrayTag> {

    public static final TagReader<ByteArrayTag> READER = input -> new ByteArrayTag(input.readBytes(input.readInt()));

    private byte[] values;

    public ByteArrayTag() {
        values = new byte[0];
    }

    public ByteArrayTag(byte[] values) {
        this.values = values;
    }

    @Override
    public Byte get(int index) {
        return values[index];
    }

    @Override
    public Byte set(int index, Byte element) {
        byte original = values[index];
        values[index] = element;
        return original;
    }

    @Override
    public void add(int index, Byte element) {
        values = ArrayUtils.add(values, index, element);
    }

    @Override
    public Byte remove(int index) {
        byte original = values[index];
        values = ArrayUtils.remove(values, index);
        return original;
    }

    @Override
    public int size() {
        return values.length;
    }

    @Override
    public void write(PacketWriteBuf output) {
        output.writeInt(values.length);
        output.writeBytes(values);
    }

    @Override
    public TagReader<ByteArrayTag> reader() {
        return READER;
    }

    @Override
    public byte type() {
        return BYTE_ARRAY;
    }
}
