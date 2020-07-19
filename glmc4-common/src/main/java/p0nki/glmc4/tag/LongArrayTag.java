package p0nki.glmc4.tag;

import org.apache.commons.lang3.ArrayUtils;
import p0nki.glmc4.network.PacketByteBuf;

import java.util.AbstractList;

public class LongArrayTag extends AbstractList<LongTag> implements Tag {

    public static final TagReader<LongArrayTag> READER = buf -> {
        int length = buf.readInt();
        long[] values = new long[length];
        for (int i = 0; i < length; i++) values[i] = buf.readLong();
        return new LongArrayTag(values);
    };

    private long[] values;

    private LongArrayTag(long[] values) {
        this.values = values;
    }

    public static LongArrayTag of(long... values) {
        return new LongArrayTag(values);
    }

    @Override
    public LongTag get(int index) {
        return LongTag.of(values[index]);
    }

    @Override
    public int size() {
        return values.length;
    }

    @Override
    public LongTag set(int index, LongTag element) {
        long original = values[index];
        values[index] = element.longValue();
        return LongTag.of(original);
    }

    @Override
    public void add(int index, LongTag element) {
        values = ArrayUtils.add(values, index, element.longValue());
    }

    @Override
    public LongTag remove(int index) {
        long original = values[index];
        values = ArrayUtils.remove(values, index);
        return LongTag.of(original);
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeInt(values.length);
        for (long l : values) buf.writeLong(l);
    }

    @Override
    public TagReader<LongArrayTag> reader() {
        return READER;
    }

    @Override
    public byte type() {
        return LONG_ARRAY;
    }
}
