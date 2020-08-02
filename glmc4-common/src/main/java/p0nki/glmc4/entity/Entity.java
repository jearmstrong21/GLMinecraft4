package p0nki.glmc4.entity;

import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector3i;
import p0nki.glmc4.block.Blocks;
import p0nki.glmc4.server.MinecraftServer;
import p0nki.glmc4.tag.CompoundTag;
import p0nki.glmc4.tag.TagEquivalent;
import p0nki.glmc4.utils.TagUtils;
import p0nki.glmc4.world.World;

import java.util.Random;
import java.util.UUID;

public abstract class Entity implements TagEquivalent<Entity, CompoundTag> {

    private final EntityType<?> type;
    private Vector3f position;
    private Vector3f velocity;
    private Vector3f facingTowards;
    private Vector3f lookingAt;
    private UUID uuid;

    public Entity(EntityType<?> type, Vector3f position, UUID uuid) {
        this.type = type;
        this.position = position;
        this.velocity = new Vector3f(0);
        this.facingTowards = new Vector3f(0, 0, -1);
        this.lookingAt = new Vector3f(0, 0, -1);
        this.uuid = uuid;
    }

    public Entity(EntityType<?> type, CompoundTag tag) {
        this.type = type;
        fromTag(tag);
    }

    private Vector3f verifyMotion(Vector3f dir) {
        if (dir.x == 0 && dir.y == 0 && dir.z == 0) return dir;
        Vector3f res = new Vector3f(dir);
        if (!isValidPosition(new Vector3f(position).add(dir.x * 0.05F, 0, 0))) {
            res.x = 0;
        }
        if (!isValidPosition(new Vector3f(position).add(0, 0, dir.z * 0.05F))) {
            res.z = 0;
        }
        if (!isValidPosition(new Vector3f(position).add(0, dir.y * 0.05F, 0))) {
            res.y = 0;
        }
        return res;
    }

    public void tick(Random random) {
        if (velocity.x != 0 || velocity.z != 0) {
            facingTowards.set(new Vector3f(velocity.x, 0, velocity.z).normalize());
        }
        position.add(new Vector3f(verifyMotion(velocity)).mul(0.05F));
    }

    public boolean canTick() {
        return MinecraftServer.INSTANCE.getServerWorld().isChunkLoaded(World.getChunkCoordinate(new Vector2i((int) position.x, (int) position.z)));
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public final boolean isValidPosition(Vector3f testPosition) {
        final int x0 = (int) Math.floor(testPosition.x);
        final int y0 = (int) Math.floor(testPosition.y);
        final int z0 = (int) Math.floor(testPosition.z);
        final int x1 = (int) Math.floor(testPosition.x + getSize().x);
        final int y1 = (int) Math.floor(testPosition.y + getSize().y);
        final int z1 = (int) Math.floor(testPosition.z + getSize().z);
        for (int x = x0; x <= x1; x++) {
            for (int y = y0; y <= y1; y++) {
                for (int z = z0; z <= z1; z++) {
                    if (MinecraftServer.INSTANCE.getServerWorld().get(new Vector3i(x, y, z)).getBlock() != Blocks.AIR)
                        return false;
                }
            }
        }
        return true;
    }

    public final EntityType<?> getType() {
        return type;
    }

    public final Vector3f getPosition() {
        return position;
    }

    public final Vector3f getEyePosition() {
        return new Vector3f(position.x + getSize().x / 2, position.y + type.getEyeHeight(), position.z + getSize().z / 2);
    }

    public final Vector3f getVelocity() {
        return velocity;
    }

    public final Vector3f getFacingTowards() {
        return facingTowards;
    }

    public final Vector3f getLookingAt() {
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
