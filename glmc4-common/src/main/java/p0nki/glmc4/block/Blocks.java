package p0nki.glmc4.block;

import p0nki.glmc4.block.blocks.*;
import p0nki.glmc4.registry.Registry;
import p0nki.glmc4.utils.Identifier;

public class Blocks {

    public static Registry<Block> REGISTRY;

    public static Block AIR;
    public static Block CACTUS;
    public static Block DIRT;
    public static Block GRASS;
    public static Block STONE;

    private static void register(String name, Block block) {
        REGISTRY.register(new Identifier("minecraft", name), block);
    }

    public static void initialize() {
        REGISTRY = new Registry<>();
        register("air", AIR = new AirBlock());
        register("cactus", CACTUS = new CactusBlock());
        register("dirt", DIRT = new DirtBlock());
        register("grass", GRASS = new GrassBlock());
        register("stone", STONE = new StoneBlock());
    }

}
