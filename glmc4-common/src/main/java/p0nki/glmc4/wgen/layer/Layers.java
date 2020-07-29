package p0nki.glmc4.wgen.layer;

import p0nki.glmc4.wgen.Biomes;
import p0nki.glmc4.wgen.RandomContext;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class Layers {

    private static LayerFactory stack(Supplier<RandomContext> supplier, ParentedLayer layer, LayerFactory parent, int count) {
        LayerFactory layerFactory = parent;
        for (int i = 0; i < count; i++) {
            layerFactory = layer.create(supplier.get(), layerFactory);
        }
        return layerFactory;
    }

    public static LayerSampler build(long seed, int w, int h) {
        AtomicInteger salt = new AtomicInteger(0);
        Supplier<RandomContext> supplier = () -> new RandomContext(seed, salt.incrementAndGet());

        LayerFactory islands = stack(supplier, ScaleLayer.NORMAL, IslandLayer.INSTANCE.create(supplier.get()), 5);
        LayerFactory baseBiomes = new BiomeChooseLayer(seed).create(supplier.get());

        LayerFactory biomes = ApplyIslandsLayer.INSTANCE.create(supplier.get(), islands, baseBiomes);

        LayerFactory rivers = ApplyRiverLayer.INSTANCE.create(supplier.get(), biomes);
        LayerFactory yeetBeetchez = ApplyBeechLayer.INSTANCE.create(supplier.get(), rivers);

        LayerSampler sampler = yeetBeetchez.make();
        return (x, z) -> sampler.sample(x - w / 2, z - h / 2);
    }

    public static boolean isOcean(int id) {
        return id == Biomes.WARM_OCEAN.id || id == Biomes.LUKEWARM_OCEAN.id || id == Biomes.OCEAN.id || id == Biomes.COLD_OCEAN.id || id == Biomes.FROZEN_OCEAN.id || id == Biomes.DEEP_WARM_OCEAN.id || id == Biomes.DEEP_LUKEWARM_OCEAN.id || id == Biomes.DEEP_OCEAN.id || id == Biomes.DEEP_COLD_OCEAN.id || id == Biomes.DEEP_FROZEN_OCEAN.id;
    }

    public static boolean isShallowOcean(int id) {
        return id == Biomes.WARM_OCEAN.id || id == Biomes.LUKEWARM_OCEAN.id || id == Biomes.OCEAN.id || id == Biomes.COLD_OCEAN.id || id == Biomes.FROZEN_OCEAN.id;
    }
}
