package p0nki.glmc4.state;

import java.util.List;

public final class BooleanProperty extends Property<Boolean> {

    public BooleanProperty(String name) {
        super(name, List.of(false, true));
    }

}
