package p0nki.glmc4.tag;

import org.apache.commons.lang3.ArrayUtils;
import p0nki.glmc4.network.PacketWriteBuf;

import java.util.AbstractList;

public class IntArrayTag extends AbstractList<Integer> implements Tag<IntArrayTag> {

    public static final TagReader<IntArrayTag> READER = input -> {
        int length = input.readInt();
        int[] values = new int[length];
        for (int i = 0; i < length; i++) values[i] = input.readInt();
        return new IntArrayTag(values);
    };

    private int[] values;

    public IntArrayTag() {
        values = new int[0];
    }

    public IntArrayTag(int[] values) {
        this.values = values;
    }

    @Override
    public Integer get(int index) {
        return values[index];
    }

    @Override
    public Integer set(int index, Integer element) {
        int original = values[index];
        values[index] = element;
        return original;
    }

    @Override
    public Integer remove(int index) {
        int original = values[index];
        values = ArrayUtils.remove(values, index);
        return original;
    }

    @Override
    public void add(int index, Integer element) {
        values = ArrayUtils.add(values, index, element);
    }

    @Override
    public int size() {
        return values.length;
    }

    @Override
    public void write(PacketWriteBuf output) {
        output.writeInt(values.length);
        for (int i : values) output.writeInt(i);
    }

    @Override
    public TagReader<IntArrayTag> reader() {
        return READER;
    }

    @Override
    public byte type() {
        return INT_ARRAY;
    }
}
