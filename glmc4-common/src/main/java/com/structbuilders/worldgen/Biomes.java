package com.structbuilders.worldgen;

import p0nki.glmc4.block.Blocks;

import java.util.HashMap;
import java.util.Map;

public class Biomes {

    public static Map<Integer,Biome>BIOMES=new HashMap<>();

    public static final Biome OCEAN = new Biome(0,"OCEAN",0,false,0,0,112, Blocks.STONE);
    public static final Biome PLAINS = new Biome(1,"PLAINS",2,false,141,179,96, Blocks.GRASS);
    public static final Biome DESERT = new Biome(2,"DESERT",3,false,250,148,24, Blocks.GRASS);
    public static final Biome MOUNTAINS = new Biome(3,"MOUNTAINS",2,false,96,96,96, Blocks.GRASS);
    public static final Biome FOREST = new Biome(4,"FOREST",2,false,5,102,33, Blocks.GRASS);
    public static final Biome TAIGA = new Biome(5,"TAIGA",2,false,11,102,89, Blocks.GRASS);
    public static final Biome SWAMP = new Biome(6,"SWAMP",2,false,7,249,178, Blocks.GRASS);
    public static final Biome RIVER = new Biome(7,"RIVER",2,false,0,0,255, Blocks.STONE);
    public static final Biome NETHER = new Biome(8,"NETHER",3,false,255,0,0, Blocks.GRASS);
    public static final Biome THE_END = new Biome(9,"THE_END",2,false,128,128,255, Blocks.GRASS);
    public static final Biome FROZEN_OCEAN = new Biome(10,"FROZEN_OCEAN",0,true,112,112,214, Blocks.STONE);
    public static final Biome FROZEN_RIVER = new Biome(11,"FROZEN_RIVER",1,true,160,160,255, Blocks.GRASS);
    public static final Biome SNOWY_TUNDRA = new Biome(12,"SNOWY_TUNDRA",1,true,255,255,255, Blocks.GRASS);
    public static final Biome SNOWY_MOUNTAINS = new Biome(13,"SNOWY_MOUNTAINS",1,true,160,160,160, Blocks.GRASS);
    public static final Biome MUSHROOM_FIELDS = new Biome(14,"MUSHROOM_FIELDS",2,false,255,0,255, Blocks.GRASS);
    public static final Biome MUSHROOM_FIELDS_SHORE = new Biome(15,"MUSHROOM_FIELDS_SHORE",2,false,160,0,255, Blocks.GRASS);
    public static final Biome BEACH = new Biome(16,"BEACH",2,false,250,222,85, Blocks.DIRT);
    public static final Biome DESERT_HILLS = new Biome(17,"DESERT_HILLS",3,false,210,95,18, Blocks.GRASS);
    public static final Biome WOODED_HILLS = new Biome(18,"WOODED_HILLS",2,false,34,85,28, Blocks.GRASS);
    public static final Biome TAIGA_HILLS = new Biome(19,"TAIGA_HILLS",2,false,22,57,51, Blocks.GRASS);
    public static final Biome MOUNTAIN_EDGE = new Biome(20,"MOUNTAIN_EDGE",2,false,114,120,154, Blocks.GRASS);
    public static final Biome JUNGLE = new Biome(21,"JUNGLE",2,false,83,123,9, Blocks.GRASS);
    public static final Biome JUNGLE_HILLS = new Biome(22,"JUNGLE_HILLS",2,false,44,66,5, Blocks.GRASS);
    public static final Biome JUNGLE_EDGE = new Biome(23,"JUNGLE_EDGE",2,false,98,139,23, Blocks.GRASS);
    public static final Biome DEEP_OCEAN = new Biome(24,"DEEP_OCEAN",0,false,0,0,48, Blocks.STONE);
    public static final Biome STONE_SHORE = new Biome(25,"STONE_SHORE",2,false,162,162,132, Blocks.GRASS);
    public static final Biome SNOWY_BEACH = new Biome(26,"SNOWY_BEACH",1,true,250,240,192, Blocks.GRASS);
    public static final Biome BIRCH_FOREST = new Biome(27,"BIRCH_FOREST",2,false,48,116,68, Blocks.GRASS);
    public static final Biome BIRCH_FOREST_HILLS = new Biome(28,"BIRCH_FOREST_HILLS",2,false,31,95,50, Blocks.GRASS);
    public static final Biome DARK_FOREST = new Biome(29,"DARK_FOREST",2,false,64,81,26, Blocks.GRASS);
    public static final Biome SNOWY_TAIGA = new Biome(30,"SNOWY_TAIGA",1,true,49,85,74, Blocks.GRASS);
    public static final Biome SNOWY_TAIGA_HILLS = new Biome(31,"SNOWY_TAIGA_HILLS",1,true,36,63,54, Blocks.GRASS);
    public static final Biome GIANT_TREE_TAIGA = new Biome(32,"GIANT_TREE_TAIGA",2,false,89,102,81, Blocks.GRASS);
    public static final Biome GIANT_TREE_TAIGA_HILLS = new Biome(33,"GIANT_TREE_TAIGA_HILLS",2,false,69,79,62, Blocks.GRASS);
    public static final Biome WOODED_MOUNTAINS = new Biome(34,"WOODED_MOUNTAINS",2,false,80,112,80, Blocks.GRASS);
    public static final Biome SAVANNA = new Biome(35,"SAVANNA",3,false,189,178,95, Blocks.GRASS);
    public static final Biome SAVANNA_PLATEAU = new Biome(36,"SAVANNA_PLATEAU",3,false,167,157,100, Blocks.GRASS);
    public static final Biome BADLANDS = new Biome(37,"BADLANDS",3,false,217,69,21, Blocks.CACTUS);
    public static final Biome WOODED_BADLANDS_PLATEAU = new Biome(38,"WOODED_BADLANDS_PLATEAU",3,false,176,151,101, Blocks.GRASS);
    public static final Biome BADLANDS_PLATEAU = new Biome(39,"BADLANDS_PLATEAU",3,false,202,140,101, Blocks.GRASS);
    public static final Biome WARM_OCEAN = new Biome(44,"WARM_OCEAN",0,false,0,0,172, Blocks.STONE);
    public static final Biome LUKEWARM_OCEAN = new Biome(45,"LUKEWARM_OCEAN",0,false,0,0,144, Blocks.STONE);
    public static final Biome COLD_OCEAN = new Biome(46,"COLD_OCEAN",0,false,32,32,112, Blocks.STONE);
    public static final Biome DEEP_WARM_OCEAN = new Biome(47,"DEEP_WARM_OCEAN",0,false,0,0,80, Blocks.STONE);
    public static final Biome DEEP_LUKEWARM_OCEAN = new Biome(48,"DEEP_LUKEWARM_OCEAN",0,false,0,0,64, Blocks.STONE);
    public static final Biome DEEP_COLD_OCEAN = new Biome(49,"DEEP_COLD_OCEAN",0,false,32,32,56, Blocks.STONE);
    public static final Biome DEEP_FROZEN_OCEAN = new Biome(50,"DEEP_FROZEN_OCEAN",0,false,64,64,144, Blocks.STONE);
    public static final Biome SUNFLOWER_PLAINS = new Biome(129,"SUNFLOWER_PLAINS",2,false,181,219,136, Blocks.GRASS);
    public static final Biome DESERT_LAKES = new Biome(130,"DESERT_LAKES",3,false,255,188,64, Blocks.GRASS);
    public static final Biome GRAVELLY_MOUNTAINS = new Biome(131,"GRAVELLY_MOUNTAINS",2,false,136,136,136, Blocks.GRASS);
    public static final Biome FLOWER_FOREST = new Biome(132,"FLOWER_FOREST",2,false,45,142,73, Blocks.GRASS);
    public static final Biome TAIGA_MOUNTAINS = new Biome(133,"TAIGA_MOUNTAINS",2,false,51,142,129, Blocks.GRASS);
    public static final Biome SWAMP_HILLS = new Biome(134,"SWAMP_HILLS",2,false,47,255,218, Blocks.GRASS);
    public static final Biome ICE_SPIKES = new Biome(140,"ICE_SPIKES",1,true,180,220,220, Blocks.GRASS);
    public static final Biome MODIFIED_JUNGLE = new Biome(149,"MODIFIED_JUNGLE",2,false,123,163,49, Blocks.GRASS);
    public static final Biome MODIFIED_JUNGLE_EDGE = new Biome(151,"MODIFIED_JUNGLE_EDGE",2,false,138,179,63, Blocks.GRASS);
    public static final Biome TALL_BIRCH_FOREST = new Biome(155,"TALL_BIRCH_FOREST",2,false,88,156,108, Blocks.GRASS);
    public static final Biome TALL_BIRCH_HILLS = new Biome(156,"TALL_BIRCH_HILLS",2,false,71,135,90, Blocks.GRASS);
    public static final Biome DARK_FOREST_HILLS = new Biome(157,"DARK_FOREST_HILLS",2,false,104,121,66, Blocks.GRASS);
    public static final Biome SNOWY_TAIGA_MOUNTAINS = new Biome(158,"SNOWY_TAIGA_MOUNTAINS",1,true,89,125,114, Blocks.GRASS);
    public static final Biome GIANT_SPRUCE_TAIGA = new Biome(160,"GIANT_SPRUCE_TAIGA",2,false,129,142,121, Blocks.GRASS);
    public static final Biome GIANT_SPRUCE_TAIGA_HILLS = new Biome(161,"GIANT_SPRUCE_TAIGA_HILLS",2,false,109,119,102, Blocks.GRASS);
    public static final Biome GRAVELLY_MOUNTAINS_PLUS = new Biome(162,"GRAVELLY_MOUNTAINS_PLUS",2,false,120,152,120, Blocks.GRASS);
    public static final Biome SHATTERED_SAVANNA = new Biome(163,"SHATTERED_SAVANNA",3,false,229,218,135, Blocks.GRASS);
    public static final Biome SHATTERED_SAVANNA_PLATEAU = new Biome(164,"SHATTERED_SAVANNA_PLATEAU",3,false,207,197,140, Blocks.GRASS);
    public static final Biome ERODED_BADLANDS = new Biome(165,"ERODED_BADLANDS",3,false,255,109,61, Blocks.GRASS);
    public static final Biome MODIFIED_WOODED_BADLANDS_PLATEAU = new Biome(166,"MODIFIED_WOODED_BADLANDS_PLATEAU",3,false,216,191,141, Blocks.GRASS);
    public static final Biome MODIFIED_BADLANDS_PLATEAU = new Biome(167,"MODIFIED_BADLANDS_PLATEAU",3,false,242,180,141, Blocks.GRASS);
    public static final Biome BAMBOO_JUNGLE = new Biome(168,"BAMBOO_JUNGLE",2,false,118,142,20, Blocks.GRASS);
    public static final Biome BAMBOO_JUNGLE_HILLS = new Biome(169,"BAMBOO_JUNGLE_HILLS",2,false,59,71,10, Blocks.GRASS);

}
