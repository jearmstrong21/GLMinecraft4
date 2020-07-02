package p0nki.glmc4.player;

import p0nki.glmc4.tag.CompoundTag;
import p0nki.glmc4.tag.TagEquivalent;

public class ServerPlayer implements TagEquivalent<ServerPlayer, CompoundTag> {

    private String id;
    private String name;

    public ServerPlayer() {

    }

    @Override
    public String toString() {
        return String.format("ServerPlayer[id=%s:name=%s]", id, name);
    }

    public ServerPlayer(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public CompoundTag toTag() {
        return new CompoundTag().insert("id", id).insert("name", name);
    }

    @Override
    public ServerPlayer fromTag(CompoundTag tag) {
        id = tag.getString("id");
        name = tag.getString("name");
        return this;
    }
}
