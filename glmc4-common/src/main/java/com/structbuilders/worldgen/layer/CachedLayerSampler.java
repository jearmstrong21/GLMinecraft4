package com.structbuilders.worldgen.layer;

import com.structbuilders.worldgen.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CachedLayerSampler implements LayerSampler {
    private int cacheCapacity;
    private List<Long> coords;
    private Map<Long, Integer> cache;
    private LayerSampler sampler;

    public CachedLayerSampler(int cacheCapacity, LayerSampler sampler) {
        this.cacheCapacity = cacheCapacity;
        this.sampler = sampler;
        coords = new ArrayList<>();
        cache = new HashMap<>();
    }

    @Override
    public int sample(int x, int z) {
        long l = Utils.hash(x, z);
        if (cache.containsKey(l)) return cache.get(l);
        int i = sampler.sample(x, z);
        cache.put(l, i);
        coords.add(l);
        if (cache.size() >= cacheCapacity) {
            cache.remove(coords.get(0));
            coords.remove(0);
        }
        return i;
    }

    public static CachedLayerSampler create(LayerSampler sampler) {
        return new CachedLayerSampler(25, sampler);
    }
}
