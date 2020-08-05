package p0nki.glmc4.world.gen.biomes;

import org.joml.Vector3f;
import p0nki.glmc4.registry.Registrable;
import p0nki.glmc4.registry.Registry;
import p0nki.glmc4.utils.data.Pair;
import p0nki.glmc4.world.gen.decorator.Decorator;
import p0nki.glmc4.world.gen.feature.Feature;
import p0nki.glmc4.world.gen.surfacebuilder.SurfaceBuilder;

import java.util.ArrayList;
import java.util.List;

public final class Biome extends Registrable<Biome> {

    private final Vector3f grassColor;
    private final List<Pair<Decorator, Feature>> preSurfaceFeatures = new ArrayList<>();
    private final SurfaceBuilder surfaceBuilder;
    private final List<Pair<Decorator, Feature>> postSurfaceFeatures = new ArrayList<>();
    private Pair<Float, Float> noiseRange;

    public Biome(Vector3f grassColor, SurfaceBuilder surfaceBuilder) {
        this.grassColor = grassColor;
        this.surfaceBuilder = surfaceBuilder;
        noiseRange = Pair.of(64.0F, 84.0F);
    }

    public Pair<Float, Float> getNoiseRange() {
        return noiseRange;
    }

    public Vector3f getGrassColor() {
        return grassColor;
    }

    public Biome presurface(Decorator decorator, Feature feature) {
        preSurfaceFeatures.add(Pair.of(Decorator.filterBiome(this, decorator), feature));
        return this;
    }

    public SurfaceBuilder getSurfaceBuilder() {
        return surfaceBuilder;
    }

    public Biome postsurface(Decorator decorator, Feature feature) {
        postSurfaceFeatures.add(Pair.of(Decorator.filterBiome(this, decorator), feature));
        return this;
    }

    public List<Pair<Decorator, Feature>> getPreSurfaceFeatures() {
        return preSurfaceFeatures;
    }

    public List<Pair<Decorator, Feature>> getPostSurfaceFeatures() {
        return postSurfaceFeatures;
    }

    public Biome withNoiseRange(float min, float max) {
        noiseRange = Pair.of(min, max);
        return this;
    }

    public Biome addNoiseRange(float offset) {
        noiseRange = noiseRange.mapFirst(min -> min + offset).mapSecond(max -> max + offset);
        return this;
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
