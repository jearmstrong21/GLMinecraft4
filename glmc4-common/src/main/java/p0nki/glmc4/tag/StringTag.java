package p0nki.glmc4.tag;

import p0nki.glmc4.network.PacketByteBuf;

import javax.annotation.Nonnull;

public class StringTag implements Tag, CharSequence {

    public static final TagReader<StringTag> READER = buf -> new StringTag(buf.readString());

    private final String value;

    private StringTag(String value) {
        this.value = value;
    }

    public static StringTag of(String value) {
        return new StringTag(value);
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeString(value);
    }

    @Override
    public TagReader<StringTag> reader() {
        return READER;
    }

    @Override
    public byte type() {
        return STRING;
    }

    @Override
    public int length() {
        return value.length();
    }

    @Override
    @Nonnull
    public String toString() {
        return value;
    }

    @Override
    public char charAt(int index) {
        return value.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return of(value.substring(start, end));
    }

    @Override
    public String asString() {
        return value;
    }
}
