package p0nki.glmc4.server;

import p0nki.glmc4.tag.CompoundTag;
import p0nki.glmc4.tag.StringTag;
import p0nki.glmc4.tag.TagEquivalent;
import p0nki.glmc4.utils.TagUtils;

import java.util.UUID;

public class ServerPlayer implements TagEquivalent<ServerPlayer, CompoundTag> {

    private UUID uuid;
    private String name;

    public ServerPlayer() {

    }

    public ServerPlayer(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("ServerPlayer[uuid=%s:name=%s]", uuid, name);
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    @Override
    public CompoundTag toTag() {
        return CompoundTag.empty().insert("uuid", TagUtils.of(uuid)).insert("name", StringTag.of(name));
    }

    @Override
    public ServerPlayer fromTag(CompoundTag tag) {
        uuid = tag.getUUID("uuid");
        name = tag.getString("name").asString();
        return this;
    }
}
