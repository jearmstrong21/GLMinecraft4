package p0nki.glmc4.utils;

import java.util.function.Predicate;

public class PredicateUtils {

    private PredicateUtils() {

    }

    @SafeVarargs
    public static <T> Predicate<T> and(Predicate<T>... predicates) {
        return value -> {
            for (Predicate<T> predicate : predicates) {
                if (!predicate.test(value)) return false;
            }
            return true;
        };
    }

}
