package p0nki.glmc4.entity;

import org.joml.Vector3f;
import p0nki.glmc4.tag.CompoundTag;
import p0nki.glmc4.tag.TagEquivalent;
import p0nki.glmc4.utils.AABB;
import p0nki.glmc4.utils.TagUtils;

import java.util.Random;
import java.util.UUID;

public abstract class Entity implements TagEquivalent<Entity, CompoundTag> {

    private Vector3f position;
    private Vector3f velocity;
    private UUID uuid;
    private final EntityType<?> type;

    public Entity(EntityType<?> type, Vector3f position, UUID uuid) {
        this.type = type;
        this.position = position;
        this.velocity = new Vector3f(0);
        this.uuid = uuid;
    }

    public final AABB getAABB() {
        return new AABB(position.x - getSize().x / 2, position.y, position.z - getSize().z / 2, getSize().x, getSize().y, getSize().z);
    }

    public Entity(EntityType<?> type, CompoundTag tag) {
        this.type = type;
        fromTag(tag);
    }

    public void tick(Random random) {
        position.add(new Vector3f(velocity).mul(0.05F));
    }

    public final EntityType<?> getType() {
        return type;
    }

    public final Vector3f getPosition() {
        return position;
    }

    public final Vector3f getVelocity() {
        return velocity;
    }

    public final Vector3f getSize() {
        return type.getSize();
    }

    public final UUID getUuid() {
        return uuid;
    }

    @Override
    public Entity fromTag(CompoundTag tag) {
        position = tag.get3f("position");
        velocity = tag.get3f("velocity");
        uuid = tag.getUUID("uuid");
        if (!EntityTypes.REGISTRY.get(type).getKey().equals(tag.getIdentifier("type")))
            throw new IllegalStateException(tag.getString("type").asString());
        return this;
    }

    @Override
    public CompoundTag toTag() {
        return CompoundTag.empty()
                .insert("position", TagUtils.of(position))
                .insert("velocity", TagUtils.of(velocity))
                .insert("uuid", TagUtils.of(uuid))
                .insert("type", EntityTypes.REGISTRY.get(type).getKey().toTag())
                ;
    }
}
