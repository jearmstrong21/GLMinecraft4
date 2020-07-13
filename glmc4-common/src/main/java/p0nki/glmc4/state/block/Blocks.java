package p0nki.glmc4.state.block;

import p0nki.glmc4.registry.Registry;
import p0nki.glmc4.state.block.blocks.CactusBlock;
import p0nki.glmc4.utils.Identifier;

public class Blocks {

    public final static Registry<Block> REGISTRY = new Registry<>();

    public final static CactusBlock CACTUS = new CactusBlock();

    public static void register(Identifier identifier, Block block) {
        REGISTRY.register(identifier, block, blockEntry -> blockEntry.getValue().afterRegister());
    }

    static {
        register(new Identifier("minecraft:cactus"), CACTUS);
    }

}
