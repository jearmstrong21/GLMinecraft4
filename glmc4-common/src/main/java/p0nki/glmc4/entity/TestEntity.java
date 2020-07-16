package p0nki.glmc4.entity;

import org.joml.Vector3f;
import p0nki.glmc4.tag.CompoundTag;

import java.util.Random;
import java.util.UUID;

public class TestEntity extends Entity {

    private Vector3f color;

    public TestEntity(Vector3f position, UUID uuid, Vector3f color) {
        super(EntityTypes.TEST, position, uuid);
        this.color = color;
    }

    @Override
    public void tick(Random random) {
        color = new Vector3f(random.nextFloat(), random.nextFloat(), random.nextFloat()).mul(5);
    }

    public Vector3f getColor() {
        return color;
    }

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
