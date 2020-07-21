package com.structbuilders.worldgen.layer;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.ExecutionException;

public class GoodCachedLayerSampler implements LayerSampler {
    private int cacheCapacity;
    private LayerSampler sampler;
    private LoadingCache<TwoIntegers, Integer> cache;

    private static class TwoIntegers {
        public int x; public int z;

        public TwoIntegers(int x, int z) {
            this.x = x;
            this.z = z;
        }
    }

    public GoodCachedLayerSampler(int cacheCapacity, LayerSampler sampler) {
        this.cacheCapacity = cacheCapacity;
        this.sampler = sampler;
        this.cache = CacheBuilder.newBuilder()
                .maximumSize(cacheCapacity)
                .build(
                        new CacheLoader<TwoIntegers, Integer>() {
                            @Override
                            public Integer load(TwoIntegers key) {
                                return sampler.sample(key.x, key.z);
                            }
                        });
    }

    @Override
    public int sample(int x, int z) {
        try {
            return cache.get(new TwoIntegers(x, z));
        } catch (ExecutionException e) {
            return -1;
        }
    }

    public static GoodCachedLayerSampler create(LayerSampler sampler) {
        return new GoodCachedLayerSampler(150, sampler);
    }
}
