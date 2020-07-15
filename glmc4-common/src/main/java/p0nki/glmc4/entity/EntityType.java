package p0nki.glmc4.entity;

import org.joml.Vector3f;
import p0nki.glmc4.tag.CompoundTag;

public class EntityType<E extends Entity> {

    private final Vector3f size;
    private final Factory<E> factory;

    public EntityType(Vector3f size, Factory<E> factory) {
        this.size = size;
        this.factory = factory;
    }

    public E create(CompoundTag tag) {
        return factory.create(tag);
    }

    public Vector3f getSize() {
        return size;
    }

    public interface Factory<E extends Entity> {

        E create(CompoundTag tag);

    }


}
