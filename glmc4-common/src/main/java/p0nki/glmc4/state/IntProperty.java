package p0nki.glmc4.state;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class IntProperty extends Property<Integer> {

    private final int min;
    private final int max;

    public IntProperty(String name, int min, int max) {
        super(name, IntStream.range(min, max).boxed().collect(Collectors.toList()));
        this.min = min;
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }
}
