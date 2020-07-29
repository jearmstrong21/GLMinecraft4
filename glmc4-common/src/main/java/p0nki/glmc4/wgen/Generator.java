package p0nki.glmc4.wgen;

import p0nki.glmc4.wgen.layer.LayerSampler;
import p0nki.glmc4.wgen.layer.Layers;

public class Generator {

    public static int[][] generate(long seed, int w, int h, int cx, int cz) {
        LayerSampler sampler = Layers.build(seed, w, h);
        int[][] data = new int[w][h];

        for (int x = 0; x < w; x++) {
            for (int z = 0; z < h; z++) {
                data[x][z] = sampler.sample(x + cx * w, z + cz * h);
            }
        }

        return data;
    }
}
