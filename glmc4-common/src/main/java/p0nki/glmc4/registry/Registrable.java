package p0nki.glmc4.registry;

import p0nki.glmc4.utils.Identifier;

public abstract class Registrable<T extends Registrable<T>> {

    public abstract Registry<T> getRegistry();

    public abstract T getValue();

    public final int getIndex() {
        return getRegistry().get(getValue()).getIndex();
    }

    public final Identifier getKey() {
        return getRegistry().get(getValue()).getKey();
    }

}
