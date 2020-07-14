package p0nki.glmc4.block.blocks;

import p0nki.glmc4.block.Block;
import p0nki.glmc4.block.BlockState;
import p0nki.glmc4.state.BooleanProperty;

public class GrassBlock extends Block {

    public static final BooleanProperty SNOWED = new BooleanProperty("snowed");

    @Override
    protected void initProperties() {
        getSchema().addProperty(SNOWED);
    }

    @Override
    public BlockState getDefaultState() {
        return super.getDefaultState().with(SNOWED, false);
    }

}
