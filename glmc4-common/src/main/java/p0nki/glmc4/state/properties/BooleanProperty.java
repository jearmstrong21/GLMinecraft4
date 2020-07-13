package p0nki.glmc4.state.properties;

import java.util.List;

public final class BooleanProperty extends Property<Boolean> {

    public BooleanProperty(String name) {
        super(name, List.of(false, true));
    }

}
