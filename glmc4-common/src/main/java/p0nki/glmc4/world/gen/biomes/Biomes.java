package p0nki.glmc4.world.gen.biomes;

import org.joml.Vector3f;
import p0nki.glmc4.block.Blocks;
import p0nki.glmc4.registry.Registry;
import p0nki.glmc4.utils.Identifier;
import p0nki.glmc4.world.gen.feature.TreeFeature;

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

    public static final Vector3f BAD = new Vector3f(1, 0, 1);

    public static final Vector3f GRASS_COLOR = new Vector3f(0.5F, 0.9F, 0.4F);
    public static final Vector3f GREEN = new Vector3f(1, 0, 0);

    public static void initialize() {
        REGISTRY = new Registry<>();
        register("ocean", OCEAN = new Biome(Blocks.STONE.getDefaultState(), BAD));
        register("plains", PLAINS = new Biome(Blocks.GRASS.getDefaultState(), GRASS_COLOR).with(TreeFeature.DECORATOR, TreeFeature.INSTANCE));
        register("mountains", MOUNTAINS = new Biome(Blocks.GRASS.getDefaultState(), BAD));
        register("river", RIVER = new Biome(Blocks.SAND.getDefaultState(), BAD));
        register("frozen_ocean", FROZEN_OCEAN = new Biome(Blocks.STONE.getDefaultState(), BAD));
        register("beach", BEACH = new Biome(Blocks.SAND.getDefaultState(), BAD));
        register("deep_ocean", DEEP_OCEAN = new Biome(Blocks.GRAVEL.getDefaultState(), BAD));
        register("badlands", BADLANDS = new Biome(Blocks.SAND.getDefaultState(), BAD));
        register("warm_ocean", WARM_OCEAN = new Biome(Blocks.STONE.getDefaultState(), BAD));
        register("lukewarm_ocean", LUKEWARM_OCEAN = new Biome(Blocks.STONE.getDefaultState(), BAD));
        register("cold_ocean", COLD_OCEAN = new Biome(Blocks.STONE.getDefaultState(), BAD));
        register("deep_warm_ocean", DEEP_WARM_OCEAN = new Biome(Blocks.GRAVEL.getDefaultState(), BAD));
        register("deep_lukewarm_ocean", DEEP_LUKEWARM_OCEAN = new Biome(Blocks.GRAVEL.getDefaultState(), BAD));
        register("deep_cold_ocean", DEEP_COLD_OCEAN = new Biome(Blocks.GRAVEL.getDefaultState(), BAD));
        register("deep_frozen_ocean", DEEP_FROZEN_OCEAN = new Biome(Blocks.GRAVEL.getDefaultState(), BAD));
    }

}
