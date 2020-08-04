package p0nki.glmc4.block.blocks;

import p0nki.glmc4.block.Block;
import p0nki.glmc4.block.BlockState;

public class AirBlock extends Block {

    @Override
    public boolean isFullBlock(BlockState blockState) {
        return false;
    }

    @Override
    public float getAOContribution() {
        return 1;
    }
}
