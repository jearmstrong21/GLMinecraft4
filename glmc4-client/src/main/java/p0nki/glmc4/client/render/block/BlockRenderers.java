package p0nki.glmc4.client.render.block;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import p0nki.glmc4.block.Blocks;
import p0nki.glmc4.client.render.block.renderers.*;
import p0nki.glmc4.registry.Registry;
import p0nki.glmc4.utils.Identifier;

import java.util.HashSet;
import java.util.Set;

public class BlockRenderers {

    private static final Logger LOGGER = LogManager.getLogger();

    public static Registry<BlockRenderer> REGISTRY;

    public static BlockRenderer DIRT;
    public static BlockRenderer GRASS;
    public static BlockRenderer STONE;
    public static BlockRenderer GRAVEL;
    public static BlockRenderer OAK_LEAVES;
    public static BlockRenderer OAK_LOG;
    public static BlockRenderer OAK_PLANKS;
    public static BlockRenderer SAND;
    public static BlockRenderer WATER;

    private static void register(String name, BlockRenderer blockRenderer) {
        REGISTRY.register(new Identifier("minecraft", name), blockRenderer);
    }

    public static void initialize() {
        REGISTRY = new Registry<>();
        register("dirt", DIRT = new DirtBlockRenderer());
        register("grass", GRASS = new GrassBlockRenderer());
        register("stone", STONE = new StoneBlockRenderer());
        register("gravel", GRAVEL = new GravelBlockRenderer());
        register("oak_leaves", OAK_LEAVES = new OakLeavesBlockRenderer());
        register("oak_log", OAK_LOG = new OakLogBlockRenderer());
        register("oak_planks", OAK_PLANKS = new OakPlanksBlockRenderer());
        register("sand", SAND = new SandBlockRenderer());
        register("water", WATER = new WaterBlockRenderer());

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
