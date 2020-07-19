package p0nki.glmc4.tag;

import org.apache.commons.lang3.ArrayUtils;
import p0nki.glmc4.network.PacketByteBuf;

public class IntArrayTag extends AbstractListTag<IntTag> {

    public static final TagReader<IntArrayTag> READER = buf -> {
        int length = buf.readInt();
        int[] values = new int[length];
        for (int i = 0; i < length; i++) values[i] = buf.readInt();
        return new IntArrayTag(values);
    };

    private int[] values;

    private IntArrayTag(int[] values) {
        this.values = values;
    }

    public static IntArrayTag of(int... values) {
        return new IntArrayTag(values);
    }

    @Override
    public IntTag get(int index) {
        return IntTag.of(values[index]);
    }

    @Override
    public IntTag set(int index, IntTag element) {
        int original = values[index];
        values[index] = element.intValue();
        return IntTag.of(original);
    }

    @Override
    public IntTag remove(int index) {
        int original = values[index];
        values = ArrayUtils.remove(values, index);
        return IntTag.of(original);
    }

    @Override
    public void add(int index, IntTag element) {
        values = ArrayUtils.add(values, index, element.intValue());
    }

    @Override
    public int size() {
        return values.length;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeInt(values.length);
        for (int i : values) buf.writeInt(i);
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
