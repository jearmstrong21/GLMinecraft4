package p0nki.glmc4.wgen.layer;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import javax.annotation.Nonnull;
import java.util.concurrent.ExecutionException;

public class GoodCachedLayerSampler implements LayerSampler {
    private final int cacheCapacity;
    private final LayerSampler sampler;
    private final LoadingCache<TwoIntegers, Integer> cache;

    public GoodCachedLayerSampler(int cacheCapacity, LayerSampler sampler) {
        this.cacheCapacity = cacheCapacity;
        this.sampler = sampler;
        this.cache = CacheBuilder.newBuilder()
                .maximumSize(cacheCapacity)
                .build(
                        new CacheLoader<>() {
                            @Override
                            public Integer load(@Nonnull TwoIntegers key) {
                                return sampler.sample(key.x, key.z);
                            }
                        });
    }

    public static GoodCachedLayerSampler create(LayerSampler sampler) {
        return new GoodCachedLayerSampler(150, sampler);
    }

    @Override
    public int sample(int x, int z) {
        try {
            return cache.get(new TwoIntegers(x, z));
        } catch (ExecutionException e) {
            return -1;
        }
    }

    private static class TwoIntegers {
        public int x;
        public int z;

        public TwoIntegers(int x, int z) {
            this.x = x;
            this.z = z;
        }
    }
}
