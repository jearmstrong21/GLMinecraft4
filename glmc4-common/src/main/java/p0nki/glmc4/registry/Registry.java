package p0nki.glmc4.registry;

import p0nki.glmc4.utils.Identifier;

import java.util.*;
import java.util.function.Consumer;

public class Registry<T> {

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

    private final Map<Identifier, Entry<T>> identifierMap;
    private final Map<T, Entry<T>> valueMap;
    private final List<Entry<T>> entries;

    public Registry() {
        identifierMap = new HashMap<>();
        valueMap = new HashMap<>();
        entries = new ArrayList<>();
    }

    public void register(Identifier identifier, T value, Consumer<Entry<T>> afterRegister) {
        if (identifierMap.containsKey(identifier)) throw new AssertionError(identifier);
        Entry<T> entry = new Entry<>(identifier, value, entries.size());
        identifierMap.put(identifier, entry);
        valueMap.put(value, entry);
        entries.add(entry);
        afterRegister.accept(entry);
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

    public boolean hasIndex(int index) {
        return index >= 0 && index < entries.size();
    }

    public List<Entry<T>> getEntries() {
        return Collections.unmodifiableList(entries);
    }

    public Set<Identifier> getKeys() {
        return Collections.unmodifiableSet(identifierMap.keySet());
    }

    public int size() {
        return entries.size();
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

}
