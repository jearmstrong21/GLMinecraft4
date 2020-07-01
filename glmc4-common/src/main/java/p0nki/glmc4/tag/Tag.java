package p0nki.glmc4.tag;

import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface Tag<T extends Tag<T>> {

    static boolean isValidCompoundValue(Object object) {
        if (!(object instanceof Map)) return false;
        Map map = (Map) object;
        for (Object value : map.keySet()) {
            if (!(value instanceof CharSequence)) return false;
        }
        for (Object value : map.values()) {
            if (!isTagType(value)) return false;
        }
        return true;
    }

    static boolean isValidListValue(Object object) {
        if (object instanceof List) {
            List list = (List) object;
            for (Object value : list) {
                if (!isTagType(value)) return false;
            }
            return true;
        } else if (object instanceof Object[]) {
            Object[] array = (Object[]) object;
            for (Object value : array) {
                if (!isTagType(value)) return false;
            }
            return true;
        } else {
            return false;
        }
    }

    static boolean isTagType(Object object) {
        return object instanceof Tag ||
                object instanceof Byte ||
                object instanceof byte[] ||
                object instanceof String ||
                object instanceof Long ||
                object instanceof Integer ||
                object instanceof int[] ||
                object instanceof ToCompoundTag ||
                isValidCompoundValue(object) ||
                isValidListValue(object);
    }

    static Tag<?> of(Object object) {
        if (object instanceof Tag) return (Tag<?>) object;
        if (object instanceof Byte) return new ByteTag((Byte) object);
        if (object instanceof byte[]) return new ByteArrayTag((byte[]) object);
        if (object instanceof String) return new StringTag((String) object);
        if (object instanceof Integer) return new IntTag((Integer) object);
        if (object instanceof int[]) return new IntArrayTag((int[]) object);
        if (object instanceof ToCompoundTag) return ((ToCompoundTag) object).toCompoundTag();
        if (isValidCompoundValue(object)) {
            Map map = (Map) object;
            Map<String, Tag<?>> values = new HashMap<>();
            for (Object key : map.keySet()) {
                values.put(((CharSequence) key).toString(), of(values.get(key)));
            }
            return new CompoundTag(values);
        }
        if (isValidListValue(object)) {
            if (object instanceof List) {
                List list = (List) object;
                Tag<?>[] values = new Tag<?>[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    values[i] = of(list.get(i));
                }
                return new ListTag(values);
            } else if (object instanceof Object[]) {
                Object[] array = (Object[]) object;
                Tag<?>[] values = new Tag<?>[array.length];
                for (int i = 0; i < array.length; i++) {
                    values[i] = of(array[i]);
                }
                return new ListTag(values);
            } else {
                throw new AssertionError("This should not have happened");
            }
        }
        throw new UnsupportedOperationException("Cannot convert " + object + " of class " + object.getClass() + " to a tag");
    }

    int BYTE = 0;
    int BYTE_ARRAY = 1;
    int STRING = 2;
    int LONG = 3;
    int LONG_ARRAY = 4;
    int INT = 5;
    int INT_ARRAY = 6;
    int COMPOUND = 7;
    int LIST = 8;

    TagReader<?>[] READERS = new TagReader<?>[]{
            ByteTag.READER,
            ByteArrayTag.READER,
            StringTag.READER,
            LongTag.READER,
            LongArrayTag.READER,
            IntTag.READER,
            IntArrayTag.READER,
            CompoundTag.READER,
            ListTag.READER
    };

    void write(DataOutput output) throws IOException;

    TagReader<T> reader();

    byte type();

}
