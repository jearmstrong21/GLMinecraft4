package p0nki.glmc4.world.gen.biomes;

import org.joml.Vector3f;
import p0nki.glmc4.block.Blocks;
import p0nki.glmc4.registry.Registry;
import p0nki.glmc4.utils.Identifier;
import p0nki.glmc4.world.gen.feature.OakTreeFeature;
import p0nki.glmc4.world.gen.feature.PalmTreeFeature;

public class Biomes {

    public static Registry<Biome> REGISTRY;

    public static Biome OCEAN;
    public static Biome PLAINS;
    public static Biome BEACH;

    private static void register(String name, Biome biome) {
        REGISTRY.register(new Identifier("minecraft", name), biome);
    }

    public static final Vector3f BAD = new Vector3f(1, 0, 1);

    public static final Vector3f GRASS_COLOR = new Vector3f(0.5F, 0.9F, 0.4F);
    public static final Vector3f GREEN = new Vector3f(1, 0, 0);

    public static void initialize() {
        REGISTRY = new Registry<>();
        register("ocean", OCEAN = new Biome(Blocks.STONE.getDefaultState(), BAD));
        register("plains", PLAINS = new Biome(Blocks.GRASS.getDefaultState(), GRASS_COLOR).with(OakTreeFeature.DECORATOR, OakTreeFeature.INSTANCE));
        register("beach", BEACH = new Biome(Blocks.SAND.getDefaultState(), BAD).with(PalmTreeFeature.DECORATOR, PalmTreeFeature.INSTANCE));
    }

}
