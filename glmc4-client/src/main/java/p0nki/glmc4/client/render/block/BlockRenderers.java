package p0nki.glmc4.client.render.block;

import p0nki.glmc4.client.render.block.renderers.DirtBlockRenderer;
import p0nki.glmc4.client.render.block.renderers.GrassBlockRenderer;
import p0nki.glmc4.client.render.block.renderers.StoneBlockRenderer;
import p0nki.glmc4.registry.Registry;
import p0nki.glmc4.utils.Identifier;

public class BlockRenderers {

    public static final Registry<BlockRenderer> REGISTRY = new Registry<>("BlockRenderer");

    public static final DirtBlockRenderer DIRT = new DirtBlockRenderer();
    public static final GrassBlockRenderer GRASS = new GrassBlockRenderer();
    public static final StoneBlockRenderer STONE = new StoneBlockRenderer();

    static {
        REGISTRY.register(new Identifier("minecraft:dirt"), DIRT);
        REGISTRY.register(new Identifier("minecraft:grass"), GRASS);
        REGISTRY.register(new Identifier("minecraft:stone"), STONE);
    }

}
