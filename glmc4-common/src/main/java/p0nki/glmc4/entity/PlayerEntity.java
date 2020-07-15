package p0nki.glmc4.entity;

import org.joml.Vector3f;
import p0nki.glmc4.server.ServerPlayer;
import p0nki.glmc4.tag.CompoundTag;

import java.util.UUID;

public class PlayerEntity extends Entity {

    private ServerPlayer serverPlayer;

    public PlayerEntity(Vector3f position, UUID uuid) {
        super(EntityTypes.PLAYER, position, uuid);
    }

    public PlayerEntity(CompoundTag tag) {
        super(EntityTypes.PLAYER, tag);
    }

    @Override
    public Entity fromTag(CompoundTag tag) {
        serverPlayer = new ServerPlayer().fromTag(tag.getCompound("serverPlayer"));
        return super.fromTag(tag);
    }

    @Override
    public CompoundTag toTag() {
        return super.toTag().insert("serverPlayer", serverPlayer);
    }

    public ServerPlayer getServerPlayer() {
        return serverPlayer;
    }
}
