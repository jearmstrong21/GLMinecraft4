package p0nki.glmc4.entity;


import org.joml.Vector3f;
import p0nki.glmc4.registry.Registry;
import p0nki.glmc4.utils.Identifier;

public class EntityTypes {

    public static Registry<EntityType<?>> REGISTRY;

    public static EntityType<PlayerEntity> PLAYER;
    public static EntityType<TestEntity> TEST;

    private static void register(String name, EntityType<?> entityType) {
        REGISTRY.register(new Identifier("minecraft", name), entityType);
    }

    public static void initialize() {
        REGISTRY = new Registry<>();
        register("player", PLAYER = new EntityType<>(1.75F, new Vector3f(0.6F, 1.8F, 0.6F), PlayerEntity::new));
        register("test", TEST = new EntityType<>(0.5F, new Vector3f(1, 1, 1), TestEntity::new));
    }

}
