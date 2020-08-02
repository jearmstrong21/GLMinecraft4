package p0nki.glmc4.world.gen;

import p0nki.glmc4.block.Blocks;
import p0nki.glmc4.registry.Registry;
import p0nki.glmc4.utils.Identifier;

public class Biomes {

    public static Registry<Biome> REGISTRY;

    public static Biome OCEAN;
    public static Biome PLAINS;
    public static Biome MOUNTAINS;
    public static Biome RIVER;
    public static Biome FROZEN_OCEAN;
    public static Biome BEACH;
    public static Biome DEEP_OCEAN;
    public static Biome BADLANDS;
    public static Biome WARM_OCEAN;
    public static Biome LUKEWARM_OCEAN;
    public static Biome COLD_OCEAN;
    public static Biome DEEP_WARM_OCEAN;
    public static Biome DEEP_LUKEWARM_OCEAN;
    public static Biome DEEP_COLD_OCEAN;
    public static Biome DEEP_FROZEN_OCEAN;

    private static void register(String name, Biome biome) {
        REGISTRY.register(new Identifier("minecraft", name), biome);
    }

    public static void initialize() {
        REGISTRY = new Registry<>();
        register("ocean", OCEAN = new Biome(Blocks.STONE.getDefaultState()));
        register("plains", PLAINS = new Biome(Blocks.GRASS.getDefaultState()));
        register("mountains", MOUNTAINS = new Biome(Blocks.GRASS.getDefaultState()));
        register("river", RIVER = new Biome(Blocks.STONE.getDefaultState()));
        register("frozen_ocean", FROZEN_OCEAN = new Biome(Blocks.STONE.getDefaultState()));
        register("beach", BEACH = new Biome(Blocks.DIRT.getDefaultState()));
        register("deep_ocean", DEEP_OCEAN = new Biome(Blocks.STONE.getDefaultState()));
        register("badlands", BADLANDS = new Biome(Blocks.CACTUS.getDefaultState()));
        register("warm_ocean", WARM_OCEAN = new Biome(Blocks.STONE.getDefaultState()));
        register("lukewarm_ocean", LUKEWARM_OCEAN = new Biome(Blocks.STONE.getDefaultState()));
        register("cold_ocean", COLD_OCEAN = new Biome(Blocks.STONE.getDefaultState()));
        register("deep_warm_ocean", DEEP_WARM_OCEAN = new Biome(Blocks.STONE.getDefaultState()));
        register("deep_lukewarm_ocean", DEEP_LUKEWARM_OCEAN = new Biome(Blocks.STONE.getDefaultState()));
        register("deep_cold_ocean", DEEP_COLD_OCEAN = new Biome(Blocks.STONE.getDefaultState()));
        register("deep_frozen_ocean", DEEP_FROZEN_OCEAN = new Biome(Blocks.STONE.getDefaultState()));
    }

}
