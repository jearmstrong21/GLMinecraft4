package p0nki.glmc4.entity;

import org.joml.Vector3f;
import p0nki.glmc4.tag.CompoundTag;

public class TestEntity extends Entity {

    private Vector3f color;

    public TestEntity(CompoundTag tag) {
        super(EntityTypes.TEST, tag);
    }

    @Override
    public Entity fromTag(CompoundTag tag) {
        color = tag.get3f("color");
        return super.fromTag(tag);
    }

    @Override
    public CompoundTag toTag() {
        return super.toTag().insert("color", color);
    }
}
