package p0nki.glmc4.block.blocks;

import p0nki.glmc4.block.Block;
import p0nki.glmc4.block.BlockState;
import p0nki.glmc4.state.IntProperty;

public class CactusBlock extends Block {

    public static final IntProperty AGE = new IntProperty("age", 0, 16);

    @Override
    protected void initProperties() {
        getSchema().addProperty(AGE);
    }

    @Override
    public BlockState getDefaultState() {
        return super.getDefaultState().with(AGE, 5);
    }

}
