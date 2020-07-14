package p0nki.glmc4.state;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class IntProperty extends Property<Integer> {

    public IntProperty(String name, int min, int max) {
        super(name, IntStream.range(min, max).boxed().collect(Collectors.toList()));
    }

}
