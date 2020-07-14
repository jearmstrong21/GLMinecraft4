package p0nki.glmc4.block;

import p0nki.glmc4.block.blocks.*;
import p0nki.glmc4.registry.Registry;
import p0nki.glmc4.utils.Identifier;

public class Blocks {

    public final static Registry<Block> REGISTRY = new Registry<>("Block");

    public final static AirBlock AIR = new AirBlock();
    public final static CactusBlock CACTUS = new CactusBlock();
    public final static DirtBlock DIRT = new DirtBlock();
    public final static GrassBlock GRASS = new GrassBlock();
    public final static StoneBlock STONE = new StoneBlock();

    static {
        REGISTRY.register(new Identifier("minecraft:air"), AIR);
        REGISTRY.register(new Identifier("minecraft:cactus"), CACTUS);
        REGISTRY.register(new Identifier("minecraft:dirt"), DIRT);
        REGISTRY.register(new Identifier("minecraft:grass"), GRASS);
        REGISTRY.register(new Identifier("minecraft:stone"), STONE);
    }

}
