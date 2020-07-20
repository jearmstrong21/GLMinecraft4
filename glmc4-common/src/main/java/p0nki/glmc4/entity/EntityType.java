package p0nki.glmc4.entity;

import org.joml.Vector3f;
import p0nki.glmc4.tag.CompoundTag;

public class EntityType<E extends Entity> {

    private final float eyeHeight;
    private final Vector3f size;
    private final Factory<E> factory;

    public EntityType(float eyeHeight, Vector3f size, Factory<E> factory) {
        this.eyeHeight = eyeHeight;
        this.size = size;
        this.factory = factory;
    }

    public E create(CompoundTag tag) {
        return factory.create(tag);
    }

    public float getEyeHeight() {
        return eyeHeight;
    }

    public Vector3f getSize() {
        return size;
    }

    public interface Factory<E extends Entity> {

        E create(CompoundTag tag);

    }


}
