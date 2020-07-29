package p0nki.glmc4.client.render.block;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import p0nki.glmc4.block.Blocks;
import p0nki.glmc4.client.render.block.renderers.DirtBlockRenderer;
import p0nki.glmc4.client.render.block.renderers.GrassBlockRenderer;
import p0nki.glmc4.client.render.block.renderers.NotFoundBlockRenderer;
import p0nki.glmc4.client.render.block.renderers.StoneBlockRenderer;
import p0nki.glmc4.registry.Registry;
import p0nki.glmc4.utils.Identifier;

import java.util.HashSet;
import java.util.Set;

public class BlockRenderers {

    private static final Logger LOGGER = LogManager.getLogger();

    public static final Registry<BlockRenderer> REGISTRY = new Registry<>();

    public static final DirtBlockRenderer DIRT = new DirtBlockRenderer();
    public static final GrassBlockRenderer GRASS = new GrassBlockRenderer();
    public static final StoneBlockRenderer STONE = new StoneBlockRenderer();

    public static void initialize() {
        REGISTRY.register(new Identifier("minecraft:dirt"), DIRT);
        REGISTRY.register(new Identifier("minecraft:grass"), GRASS);
        REGISTRY.register(new Identifier("minecraft:stone"), STONE);
        Set<Identifier> identifiers = new HashSet<>();
        Blocks.REGISTRY.getEntries().forEach(entry -> {
            if (!REGISTRY.hasKey(entry.getKey())) {
                identifiers.add(entry.getKey());
                LOGGER.error("No renderer found for {}", entry.getKey());
            }
        });
        identifiers.forEach(identifier -> REGISTRY.register(identifier, new NotFoundBlockRenderer(Blocks.REGISTRY.get(identifier).getValue())));
    }

}
