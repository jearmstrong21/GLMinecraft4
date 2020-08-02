package p0nki.glmc4.world.gen.biomes;

import p0nki.glmc4.utils.math.SimplexGenerator;

public class BiomeGenerator {

    private final SimplexGenerator biomeNoise;

    public BiomeGenerator(long seed) {
        biomeNoise = new SimplexGenerator(seed);
    }

    public Biome[][] generate(int cx, int cz) {
        Biome[][] data = new Biome[16][16];
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                float rx = cx * 16 + x;
                float rz = cz * 16 + z;
                rx *= 0.01F;
                rz *= 0.01F;
                float value = biomeNoise.simplex(rx, rz);
                if (value < -0.1F) {
                    data[x][z] = Biomes.PLAINS;
                } else if (value < 0.1F) {
                    data[x][z] = Biomes.BEACH;
                } else {
                    data[x][z] = Biomes.OCEAN;
                }
            }
        }
        return data;
    }
}
