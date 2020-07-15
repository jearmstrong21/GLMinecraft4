package p0nki.glmc4.entity;

import p0nki.glmc4.server.ServerPlayer;
import p0nki.glmc4.tag.CompoundTag;

public class PlayerEntity extends Entity {

    private ServerPlayer serverPlayer;

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
