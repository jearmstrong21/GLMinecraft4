package p0nki.glmc4.tag;

import org.apache.commons.lang3.ArrayUtils;

import java.io.DataOutput;
import java.io.IOException;
import java.util.AbstractList;

public class LongArrayTag extends AbstractList<Long> implements Tag<LongArrayTag> {

    public static final TagReader<LongArrayTag> READER = input -> {
        int length = input.readInt();
        long[] values = new long[length];
        for (int i = 0; i < length; i++) values[i] = input.readInt();
        return new LongArrayTag(values);
    };

    private long[] values;

    public LongArrayTag() {
        values = new long[0];
    }

    public LongArrayTag(long[] values) {
        this.values = values;
    }

    @Override
    public Long get(int index) {
        return values[index];
    }

    @Override
    public int size() {
        return values.length;
    }

    @Override
    public Long set(int index, Long element) {
        long original = values[index];
        values[index] = element;
        return original;
    }

    @Override
    public void add(int index, Long element) {
        values = ArrayUtils.add(values, index, element);
    }

    @Override
    public Long remove(int index) {
        long original = values[index];
        values = ArrayUtils.remove(values, index);
        return original;
    }

    @Override
    public void write(DataOutput output) throws IOException {
        output.writeInt(values.length);
        for (long l : values) output.writeLong(l);
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
