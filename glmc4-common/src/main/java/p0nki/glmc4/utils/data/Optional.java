package p0nki.glmc4.utils.data;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Optional<T> {

    private final T value;
    private final boolean hasValue;

    private Optional(T value, boolean hasValue) {
        this.value = value;
        this.hasValue = hasValue;
    }

    public static <T> Optional<T> of(T value) {
        return new Optional<>(value, true);
    }

    public static <T> Optional<T> empty() {
        return new Optional<>(null, false);
    }

    public void assertPresent() {
        if (!hasValue) throw new UnsupportedOperationException("Empty optional");
    }

    public boolean isPresent() {
        return hasValue;
    }

    public T get() {
        assertPresent();
        return value;
    }

    public T orElse(T defaultValue) {
        return hasValue ? value : defaultValue;
    }

    public <R> Optional<R> map(Function<T, Optional<R>> function) {
        return hasValue ? function.apply(value) : empty();
    }

    public Optional<T> filter(Predicate<T> predicate) {
        return hasValue && predicate.test(value) ? of(value) : empty();
    }

    public T orElse(Supplier<T> supplier) {
        return hasValue ? value : supplier.get();
    }

    public Stream<T> stream() {
        return hasValue ? Stream.of(value) : Stream.empty();
    }

}