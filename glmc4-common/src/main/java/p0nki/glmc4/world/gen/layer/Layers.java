package p0nki.glmc4.world.gen.layer;

import p0nki.glmc4.world.gen.Biomes;
import p0nki.glmc4.world.gen.RandomContext;

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
//        AtomicInteger salt = new AtomicInteger(0);
//        Supplier<RandomContext> supplier = () -> new RandomContext(seed, salt.incrementAndGet());
//
//        LayerFactory islands = stack(supplier, ScaleLayer.NORMAL, IslandLayer.INSTANCE.create(supplier.get()), 5);
//        LayerFactory baseBiomes = new BiomeChooseLayer(seed).create(supplier.get());
//
//        LayerFactory biomes = ApplyIslandsLayer.INSTANCE.create(supplier.get(), islands, baseBiomes);
//
//        LayerFactory rivers = ApplyRiverLayer.INSTANCE.create(supplier.get(), biomes);
//        LayerFactory beaches = ApplyBeachLayer.INSTANCE.create(supplier.get(), rivers);
//
//        LayerSampler sampler = beaches.make();
//        return (x, z) -> sampler.sample(x - w / 2, z - h / 2);
        return (x, z) -> Biomes.PLAINS.getIndex();
    }

    public static boolean isOcean(int id) {
        return id == Biomes.WARM_OCEAN.getIndex() || id == Biomes.LUKEWARM_OCEAN.getIndex() || id == Biomes.OCEAN.getIndex() || id == Biomes.COLD_OCEAN.getIndex() || id == Biomes.FROZEN_OCEAN.getIndex() || id == Biomes.DEEP_WARM_OCEAN.getIndex() || id == Biomes.DEEP_LUKEWARM_OCEAN.getIndex() || id == Biomes.DEEP_OCEAN.getIndex() || id == Biomes.DEEP_COLD_OCEAN.getIndex() || id == Biomes.DEEP_FROZEN_OCEAN.getIndex();
    }

}
