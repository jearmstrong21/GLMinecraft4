package p0nki.glmc4.tag;

import p0nki.glmc4.network.PacketWriteBuf;

public interface Tag extends ToTag {

    int BYTE = 0;
    int SHORT = 1;
    int INT = 2;
    int LONG = 3;
    int FLOAT = 4;
    int DOUBLE = 5;
    int STRING = 6;
    int BYTE_ARRAY = 7;
    int INT_ARRAY = 8;
    int LONG_ARRAY = 9;
    int COMPOUND = 10;
    int LIST = 11;
    int EMPTY = 12;

    static boolean isTagId(int id) {
        return id == BYTE || id == SHORT || id == INT || id == LONG || id == FLOAT || id == DOUBLE || id == STRING || id == BYTE_ARRAY || id == INT_ARRAY || id == LONG_ARRAY || id == COMPOUND || id == LIST || id == EMPTY;
    }

    static TagReader<?> getReader(int id) {
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

    void write(PacketWriteBuf output);

    TagReader<?> reader();

    byte type();

    default String asString() {
        return toString();
    }

}
