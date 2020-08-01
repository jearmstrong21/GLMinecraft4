package p0nki.glmc4.entity;

import org.joml.Vector3f;
import org.joml.Vector3i;
import p0nki.glmc4.block.Blocks;
import p0nki.glmc4.server.MinecraftServer;
import p0nki.glmc4.server.ServerPlayer;
import p0nki.glmc4.tag.CompoundTag;

import java.util.Random;

public class PlayerEntity extends Entity {

    private ServerPlayer serverPlayer;

    public PlayerEntity(Vector3f position, ServerPlayer serverPlayer) {
        super(EntityTypes.PLAYER, position, serverPlayer.getUuid());
        this.serverPlayer = serverPlayer;
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
    public void tick(Random random) {
        super.tick(random);
        final int x0 = (int) Math.floor(getPosition().x) - 6;
        final int y0 = (int) Math.floor(getPosition().y) - 6;
        final int z0 = (int) Math.floor(getPosition().z) - 6;
        final int x1 = (int) Math.floor(getPosition().x + getSize().x) + 6;
        final int y1 = (int) Math.floor(getPosition().y + getSize().y) + 6;
        final int z1 = (int) Math.floor(getPosition().z + getSize().z) + 6;
        for (int y = y0; y <= y1; y++) {
            if (y < 0 || y > 255) continue;
            for (int x = x0; x <= x1; x++) {
                for (int z = z0; z <= z1; z++) {
                    MinecraftServer.INSTANCE.getServerWorld().update(new Vector3i(x, y, z), Blocks.AIR.getDefaultState());
                }
            }
        }
    }

    @Override
    public CompoundTag toTag() {
        return super.toTag().insert("serverPlayer", serverPlayer.toTag());
    }

    public ServerPlayer getServerPlayer() {
        return serverPlayer;
    }
}
