package p0nki.glmc4.entity;

import org.joml.Vector3f;
import p0nki.glmc4.tag.CompoundTag;
import p0nki.glmc4.tag.TagEquivalent;

import java.util.UUID;

public class Entity implements TagEquivalent<Entity, CompoundTag> {

    private Vector3f position;
    private UUID uuid;
    private final EntityType<?> type;

    public Entity(EntityType<?> type, Vector3f position, UUID uuid) {
        this.type = type;
        this.position = position;
        this.uuid = uuid;
    }

    public Entity(EntityType<?> type, CompoundTag tag) {
        this.type = type;
        fromTag(tag);
    }

    public EntityType<?> getType() {
        return type;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getSize() {
        return type.getSize();
    }

    public UUID getUuid() {
        return uuid;
    }

    @Override
    public Entity fromTag(CompoundTag tag) {
        position = tag.get3f("position");
        uuid = tag.getUUID("uuid");
        if (!EntityTypes.REGISTRY.get(type).getKey().equals(tag.getIdentifier("type")))
            throw new IllegalStateException(tag.getString("type"));
        return this;
    }

    @Override
    public CompoundTag toTag() {
        return new CompoundTag()
                .insert("position", position)
                .insert("uuid", uuid)
                .insert("type", EntityTypes.REGISTRY.get(type).getKey())
                ;
    }
}
