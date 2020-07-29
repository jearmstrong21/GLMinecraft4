package p0nki.glmc4.registry;

import p0nki.glmc4.network.PacketByteBuf;
import p0nki.glmc4.utils.Identifier;

import java.util.*;

public class Registry<T extends Registrable<T>> {

    private final Map<Identifier, Entry<T>> identifierMap;
    private final Map<T, Entry<T>> valueMap;
    private final List<Entry<T>> entries;

    public Registry() {
        identifierMap = new HashMap<>();
        valueMap = new HashMap<>();
        entries = new ArrayList<>();
    }

    public void write(PacketByteBuf buf) {
        buf.writeInt(entries.size());
        for (Entry<T> entry : entries) {
            buf.writeString(entry.getKey().toString());
            buf.writeBoolean(entry.getValue() instanceof VersionedRegistrable);
            if (entry.getValue() instanceof VersionedRegistrable) {
                buf.writeInt(((VersionedRegistrable) entry.getValue()).getVersion());
            }
        }
    }

    public void verify(PacketByteBuf buf) {
        if (!check(buf)) {
            RuntimeException e = new RuntimeException("Invalid registry for server");
            e.fillInStackTrace();
            e.printStackTrace();
            System.exit(1);
        }
    }

    public boolean check(PacketByteBuf buf) {
        int size = buf.readInt();
        if (size != entries.size()) return false;
        for (int i = 0; i < size; i++) {
            Identifier identifier = new Identifier(buf.readString());
            if (!entries.get(i).getKey().equals(identifier)) return false;
            boolean b = buf.readBoolean();
            if (b != entries.get(i).getValue() instanceof VersionedRegistrable) {
                return false;
            }
            if (b) {
                if (((VersionedRegistrable) entries.get(i).getValue()).getVersion() != buf.readInt()) return false;
            }
        }
        return true;
    }

    private Entry<T> internalRegister(Identifier identifier, T value) {
        if (identifierMap.containsKey(identifier)) throw new AssertionError(identifier);
        Entry<T> entry = new Entry<>(identifier, value, entries.size());
        identifierMap.put(identifier, entry);
        valueMap.put(value, entry);
        entries.add(entry);
        return entry;
    }

    public void register(Identifier identifier, T value) {
        if (value instanceof AfterRegisterCallback) {
            ((AfterRegisterCallback) value).onAfterRegister(identifier, internalRegister(identifier, value).getIndex());
        } else {
            internalRegister(identifier, value);
        }
    }

    public void assertHasKey(Identifier identifier) {
        if (!hasKey(identifier)) throw new AssertionError(identifier);
    }

    public void assertHasIndex(int index) {
        if (!hasIndex(index)) throw new AssertionError(index);
    }

    public void assertHasValue(T value) {
        if (!hasValue(value)) throw new AssertionError(value);
    }

    public boolean hasKey(Identifier identifier) {
        return identifierMap.containsKey(identifier);
    }

    public boolean hasValue(T value) {
        return valueMap.containsKey(value);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean hasIndex(int index) {
        return index >= 0 && index < entries.size();
    }

    public List<Entry<T>> getEntries() {
        return Collections.unmodifiableList(entries);
    }

    public Entry<T> get(Identifier identifier) {
        assertHasKey(identifier);
        return identifierMap.get(identifier);
    }

    public Entry<T> get(T value) {
        assertHasValue(value);
        return valueMap.get(value);
    }

    public Entry<T> get(int index) {
        assertHasIndex(index);
        return entries.get(index);
    }

    public static final class Entry<T> {
        private final Identifier identifier;
        private final T value;
        private final int index;

        public Entry(Identifier identifier, T value, int index) {
            this.identifier = identifier;
            this.value = value;
            this.index = index;
        }

        public Identifier getKey() {
            return identifier;
        }

        public T getValue() {
            return value;
        }

        public int getIndex() {
            return index;
        }
    }

}
