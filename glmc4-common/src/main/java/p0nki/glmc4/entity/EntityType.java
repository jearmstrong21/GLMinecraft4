package p0nki.glmc4.entity;

import org.joml.Vector3f;
import p0nki.glmc4.registry.Registrable;
import p0nki.glmc4.registry.Registry;
import p0nki.glmc4.tag.CompoundTag;
import p0nki.glmc4.utils.Identifier;

public class EntityType<E extends Entity> extends Registrable<EntityType<?>> {

    public static Entity from(CompoundTag tag) {
        Identifier type = tag.getIdentifier("type");
        if (EntityTypes.REGISTRY.hasKey(type)) {
            return EntityTypes.REGISTRY.get(type).getValue().create(tag);
        }
        throw new IllegalArgumentException("Illegal entity type " + type);
    }

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

    @Override
    public Registry<EntityType<?>> getRegistry() {
        return EntityTypes.REGISTRY;
    }

    @Override
    public EntityType<E> getValue() {
        return this;
    }

    public interface Factory<E extends Entity> {

        E create(CompoundTag tag);

    }


}
