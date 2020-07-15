package p0nki.glmc4.entity;

import org.joml.Vector3f;
import p0nki.glmc4.tag.CompoundTag;

public class EntityType<T extends Entity> {

    private final Vector3f size;
    private final Factory<T> factory;

    public EntityType(Vector3f size, Factory<T> factory) {
        this.size = size;
        this.factory = factory;
    }

    public Factory<T> getFactory() {
        return factory;
    }

    public Vector3f getSize() {
        return size;
    }

    public interface Factory<T extends Entity> {

        T create(CompoundTag tag);

    }


}
