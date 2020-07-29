package p0nki.glmc4.wgen;

import p0nki.glmc4.block.Blocks;

import java.util.HashMap;
import java.util.Map;

public class Biomes {

    public static final Biome OCEAN = new Biome(0, "OCEAN", 0, false, 0, 0, 112, Blocks.STONE);
    public static final Biome PLAINS = new Biome(1, "PLAINS", 2, false, 141, 179, 96, Blocks.GRASS);
    public static final Biome MOUNTAINS = new Biome(3, "MOUNTAINS", 2, false, 96, 96, 96, Blocks.GRASS);
    public static final Biome RIVER = new Biome(7, "RIVER", 2, false, 0, 0, 255, Blocks.STONE);
    public static final Biome FROZEN_OCEAN = new Biome(10, "FROZEN_OCEAN", 0, true, 112, 112, 214, Blocks.STONE);
    public static final Biome BEACH = new Biome(16, "BEACH", 2, false, 250, 222, 85, Blocks.DIRT);
    public static final Biome DEEP_OCEAN = new Biome(24, "DEEP_OCEAN", 0, false, 0, 0, 48, Blocks.STONE);
    public static final Biome BADLANDS = new Biome(37, "BADLANDS", 3, false, 217, 69, 21, Blocks.CACTUS);
    public static final Biome WARM_OCEAN = new Biome(44, "WARM_OCEAN", 0, false, 0, 0, 172, Blocks.STONE);
    public static final Biome LUKEWARM_OCEAN = new Biome(45, "LUKEWARM_OCEAN", 0, false, 0, 0, 144, Blocks.STONE);
    public static final Biome COLD_OCEAN = new Biome(46, "COLD_OCEAN", 0, false, 32, 32, 112, Blocks.STONE);
    public static final Biome DEEP_WARM_OCEAN = new Biome(47, "DEEP_WARM_OCEAN", 0, false, 0, 0, 80, Blocks.STONE);
    public static final Biome DEEP_LUKEWARM_OCEAN = new Biome(48, "DEEP_LUKEWARM_OCEAN", 0, false, 0, 0, 64, Blocks.STONE);
    public static final Biome DEEP_COLD_OCEAN = new Biome(49, "DEEP_COLD_OCEAN", 0, false, 32, 32, 56, Blocks.STONE);
    public static final Biome DEEP_FROZEN_OCEAN = new Biome(50, "DEEP_FROZEN_OCEAN", 0, false, 64, 64, 144, Blocks.STONE);
    public static Map<Integer, Biome> BIOMES = new HashMap<>();

}
