package p0nki.glmc4.world.gen.biomes;

import p0nki.glmc4.utils.math.SimplexGenerator;

import java.util.concurrent.atomic.AtomicLong;

public class BiomeGenerator {

    private final SimplexGenerator biomeNoise;
    private final SimplexGenerator modNoise;

    public BiomeGenerator(AtomicLong seed) {
        biomeNoise = new SimplexGenerator(seed);
        modNoise = new SimplexGenerator(seed);
    }

    public Biome[][] generate(int cx, int cz) {
        Biome[][] data = new Biome[17][17];
        for (int x = 0; x < 17; x++) {
            for (int z = 0; z < 17; z++) {
//                float rx = cx * 16 + x;
//                float rz = cz * 16 + z;
//                rx *= 0.01F;
//                rz *= 0.01F;
//                float value = biomeNoise.simplex(rx, rz);
//                if (value < -0.1F) {
//                    data[x][z] = Biomes.PLAINS;
//                    if (modNoise.simplex(rx, rz) > 0.0F) data[x][z] = Biomes.HIGH_PLAINS;
//                } else if (value < 0.1F) {
//                    data[x][z] = Biomes.BEACH;
//                } else {
//                    data[x][z] = Biomes.OCEAN;
//                }
                data[x][z] = Biomes.PLAINS;
            }
        }
        return data;
    }
}
