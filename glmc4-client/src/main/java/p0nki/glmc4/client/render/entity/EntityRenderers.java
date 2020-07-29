package p0nki.glmc4.client.render.entity;

import p0nki.glmc4.client.render.entity.renderers.PlayerEntityRenderer;
import p0nki.glmc4.client.render.entity.renderers.TestEntityRenderer;
import p0nki.glmc4.registry.Registry;
import p0nki.glmc4.utils.Identifier;

public class EntityRenderers {

    public static final Registry<EntityRenderer<?>> REGISTRY = new Registry<>();

    public static final PlayerEntityRenderer PLAYER = new PlayerEntityRenderer();
    public static final TestEntityRenderer TEST = new TestEntityRenderer();

    private static void register(String name, EntityRenderer<?> entityRenderer) {
        REGISTRY.register(new Identifier("minecraft", name), entityRenderer);
    }

    public static void initialize() {
        register("player", PLAYER);
        register("test", TEST);
    }

}
