package p0nki.glmc4.tag;

import p0nki.glmc4.network.PacketByteBuf;

public interface Tag extends ToTag {

    byte BYTE = 0;
    byte SHORT = 1;
    byte INT = 2;
    byte LONG = 3;
    byte FLOAT = 4;
    byte DOUBLE = 5;
    byte STRING = 6;
    byte BYTE_ARRAY = 7;
    byte INT_ARRAY = 8;
    byte LONG_ARRAY = 9;
    byte COMPOUND = 10;
    byte LIST = 11;
    byte EMPTY = 12;

    static boolean isTagId(byte id) {
        return id == BYTE || id == SHORT || id == INT || id == LONG || id == FLOAT || id == DOUBLE || id == STRING || id == BYTE_ARRAY || id == INT_ARRAY || id == LONG_ARRAY || id == COMPOUND || id == LIST || id == EMPTY;
    }

    static TagReader<?> getReader(byte id) {
        switch (id) {
            case BYTE:
                return ByteTag.READER;
            case SHORT:
                return ShortTag.READER;
            case INT:
                return IntTag.READER;
            case LONG:
                return LongTag.READER;
            case FLOAT:
                return FloatTag.READER;
            case DOUBLE:
                return DoubleTag.READER;
            case BYTE_ARRAY:
                return ByteArrayTag.READER;
            case STRING:
                return StringTag.READER;
            case LONG_ARRAY:
                return LongArrayTag.READER;
            case INT_ARRAY:
                return IntArrayTag.READER;
            case COMPOUND:
                return CompoundTag.READER;
            case LIST:
                return ListTag.READER;
            case EMPTY:
                return EmptyTag.READER;
            default:
                throw new UnsupportedOperationException("Cannot find reader for id " + id);
        }
    }

    default Tag toTag() {
        return this;
    }

    void write(PacketByteBuf buf);

    TagReader<?> reader();

    byte type();

    default String asString() {
        return toString();
    }

}
