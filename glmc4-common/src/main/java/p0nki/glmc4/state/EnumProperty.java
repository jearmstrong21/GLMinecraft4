package p0nki.glmc4.state;

import java.util.EnumSet;
import java.util.List;

public class EnumProperty<T extends Enum<T>> extends Property<T> {
    public EnumProperty(String name, Class<T> clazz) {
        super(name, List.copyOf(EnumSet.allOf(clazz)));
    }
}
