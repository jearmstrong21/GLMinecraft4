package p0nki.glmc4.tag;

import p0nki.glmc4.network.PacketWriteBuf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface Tag<T extends Tag<T>> extends ToTag<T> {

    int BYTE = 0;
    int BYTE_ARRAY = 1;
    int STRING = 2;
    int LONG = 3;
    int LONG_ARRAY = 4;
    int INT = 5;
    int INT_ARRAY = 6;
    int COMPOUND = 7;
    int LIST = 8;
    int FLOAT = 9;

    static boolean isValidCompoundValue(Object object) {
        if (!(object instanceof Map)) return false;
        Map<?, ?> map = (Map<?, ?>) object;
        for (Object value : map.keySet()) {
            if (!(value instanceof CharSequence)) return false;
        }
        for (Object value : map.values()) {
            if (!isValidTag(value)) return false;
        }
        return true;
    }

    static boolean isValidListValue(Object object) {
        if (object instanceof List) {
            List<?> list = (List<?>) object;
            for (Object value : list) {
                if (!isValidTag(value)) return false;
            }
            return true;
        } else if (object instanceof Object[]) {
            Object[] array = (Object[]) object;
            for (Object value : array) {
                if (!isValidTag(value)) return false;
            }
            return true;
        } else {
            return false;
        }
    }

    static boolean isValidTag(Object object) {
        return object instanceof Byte ||
                object instanceof byte[] ||
                object instanceof String ||
                object instanceof Long ||
                object instanceof Integer ||
                object instanceof int[] ||
                object instanceof ToTag<?> ||
                object instanceof Float ||
                isValidCompoundValue(object) ||
                isValidListValue(object);
    }

    static CompoundTag ofMap(Map<?, ?> map) {
        if (!isValidCompoundValue(map))
            throw new UnsupportedOperationException("Object must be valid compound value");
        Map<String, Tag<?>> values = new HashMap<>();
        for (Object key : map.keySet()) {
            if (!(key instanceof CharSequence))
                throw new UnsupportedOperationException("Cannot convert non-CharSequence to CharSequence");
            Object value = values.get(key.toString());
            if (!isValidTag(value))
                throw new UnsupportedOperationException("Cannot convert non-Tag to Tag");
            values.put((String) key, of(value));
        }
        return new CompoundTag(values);
    }

    static ListTag ofList(List<?> list) {
        Tag<?>[] values = new Tag<?>[list.size()];
        for (int i = 0; i < list.size(); i++) {
            values[i] = of(list.get(i));
        }
        return new ListTag(values);
    }

    static ListTag ofList(Object[] array) {
        Tag<?>[] values = new Tag<?>[array.length];
        for (int i = 0; i < array.length; i++) {
            values[i] = of(array[i]);
        }
        return new ListTag(values);
    }

    static Tag<?> of(Object object) {
        if (object instanceof Tag) return (Tag<?>) object;
        if (object instanceof Byte) return new ByteTag((Byte) object);
        if (object instanceof byte[]) return new ByteArrayTag((byte[]) object);
        if (object instanceof String) return new StringTag((String) object);
        if (object instanceof Integer) return new IntTag((Integer) object);
        if (object instanceof int[]) return new IntArrayTag((int[]) object);
        if (object instanceof ToTag) return ((ToTag<?>) object).toTag();
        if (object instanceof Float) return new FloatTag((Float) object);
        if (isValidCompoundValue(object)) return ofMap((Map<?, ?>) object);
        if (isValidListValue(object)) {
            if (object instanceof List) {
                return ofList((List<?>) object);
            } else if (object instanceof Object[]) {
                return ofList((Object[]) object);
            } else {
                throw new AssertionError("This should not have happened");
            }
        }
        throw new UnsupportedOperationException("Cannot convert " + object + " of class " + object.getClass() + " to a tag");
    }

    static boolean isTagId(int id) {
        return id == BYTE || id == BYTE_ARRAY || id == STRING || id == LONG || id == LONG_ARRAY || id == INT || id == INT_ARRAY || id == COMPOUND || id == LIST || id == FLOAT;
    }

    static TagReader<?> getReader(int id) {
        switch (id) {
            case BYTE:
                return ByteTag.READER;
            case BYTE_ARRAY:
                return ByteArrayTag.READER;
            case STRING:
                return StringTag.READER;
            case LONG:
                return LongTag.READER;
            case LONG_ARRAY:
                return LongArrayTag.READER;
            case INT:
                return IntTag.READER;
            case INT_ARRAY:
                return IntArrayTag.READER;
            case COMPOUND:
                return CompoundTag.READER;
            case LIST:
                return ListTag.READER;
            case FLOAT:
                return FloatTag.READER;
            default:
                throw new UnsupportedOperationException("Cannot find reader for id " + id);
        }
    }

    @SuppressWarnings("unchecked")
    default T toTag() {
        return (T) this;
    }

    void write(PacketWriteBuf output);

    TagReader<T> reader();

    byte type();

}
