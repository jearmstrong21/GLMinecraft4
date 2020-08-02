package p0nki.glmc4.world.gen.layer;

import p0nki.glmc4.world.gen.RandomContext;

public interface InitLayer {

    default LayerFactory create(RandomContext context) {
        return () -> CachedLayerSampler.create((x, z) -> {
            context.initSeed(x, z);
            return sample(context, x, z);
        });
    }

    int sample(RandomContext context, int x, int z);

}
