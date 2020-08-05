package p0nki.glmc4.world.gen.biomes;

import org.joml.Vector3f;
import p0nki.glmc4.block.Blocks;
import p0nki.glmc4.registry.Registry;
import p0nki.glmc4.utils.Identifier;
import p0nki.glmc4.world.gen.feature.LakeFeature;
import p0nki.glmc4.world.gen.feature.OakTreeFeature;
import p0nki.glmc4.world.gen.feature.PalmTreeFeature;
import p0nki.glmc4.world.gen.surfacebuilder.TwoLayerSurfaceBuilder;

public class Biomes {

    public static Registry<Biome> REGISTRY;

    public static Biome OCEAN;
    public static Biome PLAINS;
    public static Biome HIGH_PLAINS;
    public static Biome BEACH;

    private static void register(String name, Biome biome) {
        REGISTRY.register(new Identifier("minecraft", name), biome);
    }

    public static final Vector3f BAD = new Vector3f(1, 0, 1);

    public static final Vector3f GRASS_COLOR = new Vector3f(0.5F, 0.9F, 0.4F);

    public static void initialize() {
        REGISTRY = new Registry<>();
        register("ocean", OCEAN = new Biome(
                BAD,
                new TwoLayerSurfaceBuilder(Blocks.SAND.getDefaultState(), Blocks.SAND.getDefaultState()))
                .addNoiseRange(-20));
        register("plains", PLAINS = new Biome(
                GRASS_COLOR,
                new TwoLayerSurfaceBuilder(Blocks.GRASS.getDefaultState(), Blocks.DIRT.getDefaultState()))
                .presurface(LakeFeature.DECORATOR, LakeFeature.FEATURE)
                .postsurface(OakTreeFeature.PLAINS, OakTreeFeature.INSTANCE)
                .withNoiseRange(70, 75));
        register("high_plains", HIGH_PLAINS = new Biome(
                new Vector3f(GRASS_COLOR).add(0.3F, 0.05F, 0),
                new TwoLayerSurfaceBuilder(Blocks.GRASS.getDefaultState(), Blocks.DIRT.getDefaultState()))
                .presurface(LakeFeature.DECORATOR, LakeFeature.FEATURE)
                .postsurface(OakTreeFeature.HIGH_PLAINS, OakTreeFeature.INSTANCE)
                .addNoiseRange(10));
        register("beach", BEACH = new Biome(
                BAD,
                new TwoLayerSurfaceBuilder(Blocks.SAND.getDefaultState(), Blocks.SAND.getDefaultState()))
                .postsurface(PalmTreeFeature.DECORATOR, PalmTreeFeature.INSTANCE)
                .addNoiseRange(-2));
    }

}
