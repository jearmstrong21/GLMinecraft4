package p0nki.glmc4.utils;

import java.util.function.Consumer;
import java.util.function.Function;

public abstract class Either<L, R> {

    private static final class Left<L, R> extends Either<L, R> {

        private final L value;

        public Left(L value) {
            this.value = value;
        }

        @Override
        public <L1, R1> Either<L1, R1> mapBoth(Function<L, L1> f1, Function<R, R1> f2) {
            return new Left<>(f1.apply(value));
        }

        @Override
        public <T> T map(Function<L, T> f1, Function<R, T> f2) {
            return f1.apply(value);
        }

        @Override
        public Either<L, R> ifLeft(Consumer<L> consumer) {
            consumer.accept(value);
            return this;
        }

        @Override
        public Either<L, R> ifRight(Consumer<R> consumer) {
            return this;
        }

        @Override
        public Optional<L> left() {
            return Optional.of(value);
        }

        @Override
        public Optional<R> right() {
            return Optional.empty();
        }
    }

    private static final class Right<L, R> extends Either<L, R> {

        private final R value;

        public Right(R value) {
            this.value = value;
        }

        @Override
        public <L1, R1> Either<L1, R1> mapBoth(Function<L, L1> f1, Function<R, R1> f2) {
            return new Right<>(f2.apply(value));
        }

        @Override
        public <T> T map(Function<L, T> f1, Function<R, T> f2) {
            return f2.apply(value);
        }

        @Override
        public Either<L, R> ifLeft(Consumer<L> consumer) {
            return this;
        }

        @Override
        public Either<L, R> ifRight(Consumer<R> consumer) {
            consumer.accept(value);
            return this;
        }

        @Override
        public Optional<L> left() {
            return Optional.empty();
        }

        @Override
        public Optional<R> right() {
            return Optional.of(value);
        }
    }

    public abstract <L1, R1> Either<L1, R1> mapBoth(Function<L, L1> f1, Function<R, R1> f2);

    public abstract <T> T map(Function<L, T> f1, Function<R, T> f2);

    public abstract Either<L, R> ifLeft(Consumer<L> consumer);

    public abstract Either<L, R> ifRight(Consumer<R> consumer);

    public abstract Optional<L> left();

    public abstract Optional<R> right();

    public <T> Either<T, R> mapLeft(Function<L, T> function) {
        return map(t -> left(function.apply(t)), Either::right);
    }

    public <T> Either<L, T> mapRight(Function<R, T> function) {
        return map(Either::left, t -> right(function.apply(t)));
    }

    public Either<R, L> swap() {
        return map(Either::right, Either::left);
    }

    public static <L, R> Either<L, R> left(L value) {
        return new Left<>(value);
    }

    public static <L, R> Either<L, R> right(R value) {
        return new Right<>(value);
    }

}
