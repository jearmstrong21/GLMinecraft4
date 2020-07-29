package p0nki.glmc4.wgen.layer;

import p0nki.glmc4.wgen.RandomContext;

public interface ParentedLayer {

    default LayerFactory create(RandomContext context, LayerFactory parent) {
        return () -> {
            LayerSampler p = parent.make();
            return CachedLayerSampler.create((x, z) -> {
                context.initSeed(x, z);
                return sample(context, p, x, z);
            });
        };
    }

    int sample(RandomContext context, LayerSampler parent, int x, int z);

}
