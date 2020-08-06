package p0nki.glmc4.client;

import org.joml.Vector3f;

public class ClientSettings {

    public static final boolean RENDER_HITBOXES = true;
    public static final Vector3f HITBOX_COLOR = new Vector3f(1, 0, 0);

    public static final boolean CHUNK_BORDERS = false;
    public static final Vector3f CHUNK_COLOR = new Vector3f(0, 0, 1);
    public static final Vector3f CURRENT_CHUNK_COLOR = new Vector3f(1, 1, 0);

    public static final boolean SMOOTH_LIGHTING = true;

    public static final boolean FIRST_PERSON = true;
    public static final float CROSSHAIR_SIZE = 0.1F;

    private ClientSettings() {

    }

}
