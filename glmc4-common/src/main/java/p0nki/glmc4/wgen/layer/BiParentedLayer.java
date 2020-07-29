package p0nki.glmc4.wgen.layer;

import p0nki.glmc4.wgen.RandomContext;

public interface BiParentedLayer {

    default LayerFactory create(RandomContext context, LayerFactory a, LayerFactory b) {
        return () -> {
            LayerSampler a1 = a.make();
            LayerSampler b1 = b.make();
            return CachedLayerSampler.create((x, z) -> {
                context.initSeed(x, z);
                return sample(context, a1, b1, x, z);
            });
        };
    }

    int sample(RandomContext context, LayerSampler a, LayerSampler b, int x, int z);

}
