package p0nki.glmc4.tag;

import org.apache.commons.lang3.ArrayUtils;
import p0nki.glmc4.network.PacketByteBuf;

public class ByteArrayTag extends AbstractListTag<ByteTag> {

    public static final TagReader<ByteArrayTag> READER = buf -> of(buf.readBytes(buf.readInt()).array());

    private byte[] values;

    private ByteArrayTag(byte[] values) {
        this.values = values;
    }

    public static ByteArrayTag of(byte... values) {
        return new ByteArrayTag(values);
    }

    @Override
    public ByteTag set(int index, ByteTag element) {
        byte b = values[index];
        values[index] = element.byteValue();
        return ByteTag.of(b);
    }

    @Override
    public void add(int index, ByteTag element) {
        values = ArrayUtils.add(values, index, element.byteValue());
    }

    @Override
    public ByteTag remove(int index) {
        byte b = values[index];
        values = ArrayUtils.remove(values, index);
        return ByteTag.of(b);
    }

    @Override
    public ByteTag get(int index) {
        return ByteTag.of(values[index]);
    }

    @Override
    public int size() {
        return values.length;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeInt(values.length);
        buf.writeBytes(values);
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
