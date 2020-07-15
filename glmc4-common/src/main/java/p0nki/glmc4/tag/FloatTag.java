package p0nki.glmc4.tag;

import p0nki.glmc4.network.PacketWriteBuf;

public class FloatTag implements Tag<FloatTag> {

    public static final TagReader<FloatTag> READER = input -> new FloatTag(input.readFloat());

    private final float value;

    public FloatTag(float value) {
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    @Override
    public void write(PacketWriteBuf output) {
        output.writeFloat(value);
    }

    @Override
    public TagReader<FloatTag> reader() {
        return READER;
    }

    @Override
    public byte type() {
        return FLOAT;
    }
}
