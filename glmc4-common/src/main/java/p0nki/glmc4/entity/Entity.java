package p0nki.glmc4.entity;

import org.joml.Vector3f;
import p0nki.glmc4.tag.CompoundTag;
import p0nki.glmc4.tag.TagEquivalent;

import java.util.UUID;

public class Entity implements TagEquivalent<Entity, CompoundTag> {

    private Vector3f position;
    private Vector3f size;
    private UUID uuid;

    public Entity(Vector3f position, Vector3f size, UUID uuid) {
        this.position = position;
        this.size = size;
        this.uuid = uuid;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getSize() {
        return size;
    }

    public UUID getUuid() {
        return uuid;
    }

    @Override
    public Entity fromTag(CompoundTag tag) {
        position = tag.get3f("position");
        size = tag.get3f("size");
        uuid = tag.getUUID("uuid");
        return this;
    }

    @Override
    public CompoundTag toTag() {
        return new CompoundTag()
                .insert("position", position)
                .insert("size", size)
                .insert("uuid", uuid)
                ;
    }
}
