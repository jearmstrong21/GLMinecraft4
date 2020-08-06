package p0nki.glmc4.block.blocks;

import p0nki.glmc4.block.Block;
import p0nki.glmc4.block.BlockState;
import p0nki.glmc4.state.IntProperty;
import p0nki.glmc4.utils.math.VoxelShape;

public class WaterBlock extends Block {

    public static final IntProperty HEIGHT = new IntProperty("height", 1, 16);

    @Override
    protected void initProperties() {
        getSchema().addProperty(HEIGHT);
    }

    @Override
    public boolean isFullBlock(BlockState blockState) {
        return blockState.get(HEIGHT) == 15;
    }

    @Override
    public float getAOContribution() {
        return 1;
    }

    @Override
    public BlockState getDefaultState() {
        return super.getDefaultState().with(HEIGHT, 15);
    }

    @Override
    public VoxelShape getShape(BlockState blockState) {
        return VoxelShape.EMPTY;
    }

    @Override
    public byte getBlockedSunlight() {
        return 1;
    }
}
