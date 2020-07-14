package p0nki.glmc4.registry;

import p0nki.glmc4.utils.Identifier;

public interface AfterRegisterCallback {

    /**
     * Called immediately after the object is registered.
     * No other objects are guaranteed to be available,
     * however this object's numerical ID will be known.
     */
    void onAfterRegister(Identifier identifier, int index);

}
