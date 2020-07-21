package p0nki.glmc4.entity;

import org.joml.Vector3f;
import p0nki.glmc4.block.Blocks;
import p0nki.glmc4.server.MinecraftServer;
import p0nki.glmc4.tag.CompoundTag;
import p0nki.glmc4.tag.TagEquivalent;
import p0nki.glmc4.utils.TagUtils;
import p0nki.glmc4.utils.math.AABB;

import java.util.Random;
import java.util.UUID;

public abstract class Entity implements TagEquivalent<Entity, CompoundTag> {

    private Vector3f position;
    private Vector3f velocity;
    private Vector3f facingTowards;
    private Vector3f lookingAt;
    private UUID uuid;
    private final EntityType<?> type;

    public Entity(EntityType<?> type, Vector3f position, UUID uuid) {
        this.type = type;
        this.position = position;
        this.velocity = new Vector3f(0);
        this.facingTowards = new Vector3f(0, 0, -1);
        this.lookingAt = new Vector3f(0, 0, -1);
        this.uuid = uuid;
    }

    public final AABB getAABB() {
        return getAABB(position);
    }

    public final AABB getAABB(Vector3f testPosition) {
        return new AABB(testPosition.x - getSize().x / 2, testPosition.y, testPosition.z - getSize().z / 2, getSize().x, getSize().y, getSize().z);
    }

    public Entity(EntityType<?> type, CompoundTag tag) {
        this.type = type;
        fromTag(tag);
    }

    public void tick(Random random) {
        if (velocity.x != 0 || velocity.z != 0) {
            facingTowards.set(new Vector3f(velocity.x, 0, velocity.z).normalize());
        }
        if (!isValidPosition(new Vector3f(position).add(new Vector3f(velocity).mul(0.05F)))) return;
        position.add(new Vector3f(velocity).mul(0.05F));
        velocity.y += 0.1F;
    }

    public boolean isValidPosition(Vector3f testPosition) {
        return getAABB(testPosition).streamBlockPos().allMatch(blockPos -> MinecraftServer.INSTANCE.getServerWorld().get(blockPos).getBlock() == Blocks.AIR);
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

    public final Vector3f getFacingTowards() {
        return facingTowards;
    }

    public Vector3f getLookingAt() {
        return lookingAt;
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
        facingTowards = tag.get3f("facingTowards");
        lookingAt = tag.get3f("lookingAt");
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
                .insert("facingTowards", TagUtils.of(facingTowards))
                .insert("lookingAt", TagUtils.of(lookingAt))
                .insert("uuid", TagUtils.of(uuid))
                .insert("type", EntityTypes.REGISTRY.get(type).getKey().toTag());
    }
}
