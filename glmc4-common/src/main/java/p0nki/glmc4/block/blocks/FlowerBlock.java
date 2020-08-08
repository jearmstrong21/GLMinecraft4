package p0nki.glmc4.block.blocks;

import p0nki.glmc4.block.Block;
import p0nki.glmc4.block.BlockState;
import p0nki.glmc4.utils.math.VoxelShape;

public class FlowerBlock extends Block {

    @Override
    public boolean isFullBlock(BlockState blockState) {
        return false;
    }

    @Override
    public float getAOContribution(BlockState blockState) {
        return 1;
    }

    @Override
    public VoxelShape getShape(BlockState blockState) {
        return VoxelShape.EMPTY;
    }

    @Override
    public byte getBlockedSunlight(BlockState blockState) {
        return 0;
    }
}
