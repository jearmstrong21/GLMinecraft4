package p0nki.glmc4.client.render.entity;

import p0nki.glmc4.client.render.entity.renderers.PlayerEntityRenderer;
import p0nki.glmc4.client.render.entity.renderers.TestEntityRenderer;
import p0nki.glmc4.registry.Registry;
import p0nki.glmc4.utils.Identifier;

public class EntityRenderers {

    public static final Registry<EntityRenderer<?>> REGISTRY = new Registry<>();

    public static final PlayerEntityRenderer PLAYER = new PlayerEntityRenderer();
    public static final TestEntityRenderer TEST = new TestEntityRenderer();

    public static void initialize() {
        REGISTRY.register(new Identifier("minecraft:player"), PLAYER);
        REGISTRY.register(new Identifier("minecraft:test"), TEST);
    }

}
