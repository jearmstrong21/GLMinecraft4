package p0nki.glmc4.world.gen.biomes;

import org.joml.Vector3f;
import p0nki.glmc4.block.BlockState;
import p0nki.glmc4.registry.Registrable;
import p0nki.glmc4.registry.Registry;
import p0nki.glmc4.utils.data.Pair;
import p0nki.glmc4.world.gen.decorator.Decorator;
import p0nki.glmc4.world.gen.feature.Feature;

import java.util.ArrayList;
import java.util.List;

public final class Biome extends Registrable<Biome> {

    private final BlockState topBlock;
    private final Vector3f grassColor;
    private final List<Pair<Decorator, Feature>> decoratorFeatures = new ArrayList<>();

    public Biome(BlockState topBlock, Vector3f grassColor) {
        this.topBlock = topBlock;
        this.grassColor = grassColor;
    }

    public Vector3f getGrassColor() {
        return grassColor;
    }

    public Biome with(Decorator decorator, Feature feature) {
        decoratorFeatures.add(Pair.of(Decorator.filterBiome(this, decorator), feature));
        return this;
    }

    public List<Pair<Decorator, Feature>> getDecoratorFeatures() {
        return decoratorFeatures;
    }

    public BlockState getTopBlockState() {
        return topBlock;
    }

    @Override
    public Registry<Biome> getRegistry() {
        return Biomes.REGISTRY;
    }

    @Override
    public Biome getValue() {
        return this;
    }
}
