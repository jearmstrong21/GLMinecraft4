package p0nki.glmc4.state;

import java.util.Collections;
import java.util.List;

public abstract class Property<T> {

    private final String name;
    private final List<T> values;

    protected Property(String name, List<T> values) {
        this.name = name;
        this.values = Collections.unmodifiableList(values);
        if (values.size() == 0) throw new AssertionError("Cannot create empty property.");
    }

    public String getName() {
        return name;
    }

    public final List<T> values() {
        return values;
    }

    public int getIndex(T value) {
        return values.indexOf(value);
    }

    public boolean supportsValue(T value) {
        return values.contains(value);
    }

    public final int bits() {
        return (int) Math.ceil(Math.log(values.size()) / Math.log(2));
    }

}
