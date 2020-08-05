package p0nki.glmc4.client.render.block;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import p0nki.glmc4.block.Blocks;
import p0nki.glmc4.client.assets.AtlasPosition;
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
    public static BlockRenderer RED_TULIP;
    public static BlockRenderer ORANGE_TULIP;
    public static BlockRenderer WHITE_TULIP;
    public static BlockRenderer PINK_TULIP;

    private static void register(String name, BlockRenderer blockRenderer) {
        REGISTRY.register(new Identifier("minecraft", name), blockRenderer);
    }

    public static void initialize() {
        REGISTRY = new Registry<>();
        register("dirt", DIRT = new NoContextConstantTexturedSidedBlockRenderer(Blocks.DIRT, AtlasPosition.block(new Identifier("minecraft:dirt")).layer().quad()));
        register("grass", GRASS = new GrassBlockRenderer());
        register("stone", STONE = new NoContextConstantTexturedSidedBlockRenderer(Blocks.STONE, AtlasPosition.block(new Identifier("minecraft:stone")).layer().quad()));
        register("gravel", GRAVEL = new NoContextConstantTexturedSidedBlockRenderer(Blocks.GRASS, AtlasPosition.block(new Identifier("minecraft:gravel")).layer().quad()));
        register("oak_leaves", OAK_LEAVES = new OakLeavesBlockRenderer());
        register("oak_log", OAK_LOG = new OakLogBlockRenderer());
        register("oak_planks", OAK_PLANKS = new NoContextConstantTexturedSidedBlockRenderer(Blocks.OAK_PLANKS, AtlasPosition.block(new Identifier("minecraft:oak_planks")).layer().quad()));
        register("sand", SAND = new NoContextConstantTexturedSidedBlockRenderer(Blocks.SAND, AtlasPosition.block(new Identifier("minecraft:sand")).layer().quad()));
        register("water", WATER = new WaterBlockRenderer());
        register("red_tulip", RED_TULIP = new ConstantCrossBlockRenderer(Blocks.RED_TULIP, AtlasPosition.block(new Identifier("minecraft:flower_red_tulip")).layer().quad()));
        register("orange_tulip", ORANGE_TULIP = new ConstantCrossBlockRenderer(Blocks.ORANGE_TULIP, AtlasPosition.block(new Identifier("minecraft:flower_orange_tulip")).layer().quad()));
        register("white_tulip", WHITE_TULIP = new ConstantCrossBlockRenderer(Blocks.WHITE_TULIP, AtlasPosition.block(new Identifier("minecraft:flower_white_tulip")).layer().quad()));
        register("pink_tulip", PINK_TULIP = new ConstantCrossBlockRenderer(Blocks.PINK_TULIP, AtlasPosition.block(new Identifier("minecraft:flower_pink_tulip")).layer().quad()));

        Set<Identifier> identifiers = new HashSet<>();
        Blocks.REGISTRY.getEntries().forEach(entry -> {
            if (!REGISTRY.hasKey(entry.getKey())) {
                identifiers.add(entry.getKey());
                LOGGER.error("No renderer found for {}", entry.getKey());
            }
        });
        identifiers.forEach(identifier -> REGISTRY.register(identifier, new NoContextConstantTexturedSidedBlockRenderer(Blocks.REGISTRY.get(identifier).getValue(), AtlasPosition.block(new Identifier("minecraft:error")).layer().quad())));
    }

}
