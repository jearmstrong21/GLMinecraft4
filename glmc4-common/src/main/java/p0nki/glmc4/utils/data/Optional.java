package p0nki.glmc4.utils.data;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Optional<T> {

    private final T value;

    private Optional(T value) {
        this.value = value;
    }

    public static <T> Optional<T> of(T value) {
        return new Optional<>(Objects.requireNonNull(value));
    }

    public static <T> Optional<T> ofNullable(T value) {
        return value == null ? empty() : of(value);
    }

    public static <T> Optional<T> empty() {
        return new Optional<>(null);
    }

    public void assertPresent() {
        if (value == null) throw new UnsupportedOperationException("Empty optional");
    }

    public boolean isPresent() {
        return value != null;
    }

    public T get() {
        assertPresent();
        return value;
    }

    public T orElse(T defaultValue) {
        return value == null ? defaultValue : value;
    }

    public <R> Optional<R> map(Function<T, Optional<R>> function) {
        return value == null ? empty() : function.apply(value);
    }

    public Optional<T> filter(Predicate<T> predicate) {
        return value != null && predicate.test(value) ? of(value) : empty();
    }

    public T orElse(Supplier<T> supplier) {
        return value == null ? supplier.get() : value;
    }

    public Stream<T> stream() {
        return value == null ? Stream.empty() : Stream.of(value);
    }

}