package p0nki.glmc4.client.render.block;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import p0nki.glmc4.block.Blocks;
import p0nki.glmc4.client.render.block.renderers.DirtBlockRenderer;
import p0nki.glmc4.client.render.block.renderers.ErrorBlockRenderer;
import p0nki.glmc4.client.render.block.renderers.GrassBlockRenderer;
import p0nki.glmc4.client.render.block.renderers.StoneBlockRenderer;
import p0nki.glmc4.registry.Registry;
import p0nki.glmc4.utils.Identifier;

import java.util.HashSet;
import java.util.Set;

public class BlockRenderers {

    private static final Logger LOGGER = LogManager.getLogger();

    public static Registry<BlockRenderer> REGISTRY;

    public static DirtBlockRenderer DIRT;
    public static GrassBlockRenderer GRASS;
    public static StoneBlockRenderer STONE;

    private static void register(String name, BlockRenderer blockRenderer) {
        REGISTRY.register(new Identifier("minecraft", name), blockRenderer);
    }

    public static void initialize() {
        REGISTRY = new Registry<>();
        register("dirt", DIRT = new DirtBlockRenderer());
        register("grass", GRASS = new GrassBlockRenderer());
        register("stone", STONE = new StoneBlockRenderer());
        Set<Identifier> identifiers = new HashSet<>();
        Blocks.REGISTRY.getEntries().forEach(entry -> {
            if (!REGISTRY.hasKey(entry.getKey())) {
                identifiers.add(entry.getKey());
                LOGGER.error("No renderer found for {}", entry.getKey());
            }
        });
        identifiers.forEach(identifier -> REGISTRY.register(identifier, new ErrorBlockRenderer(Blocks.REGISTRY.get(identifier).getValue())));
    }

}
