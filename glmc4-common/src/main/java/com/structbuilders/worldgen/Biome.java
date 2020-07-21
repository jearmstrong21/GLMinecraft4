package com.structbuilders.worldgen;

import p0nki.glmc4.block.Block;

import java.awt.*;

public class Biome {

    public final int id;
    public String name;
    public int temp;
    public boolean snow;
    public int color;
    public Block topBlock;

    public Biome(int id, String name, int temp, boolean snow, int red, int green, int blue, Block topBlock) {
        this.id = id;
        this.name = name;
        this.temp = temp;
        this.snow = snow;
        this.color=new Color(red,green,blue).getRGB();
        Biomes.BIOMES.put(id,this);
        this.topBlock = topBlock;
    }

}
