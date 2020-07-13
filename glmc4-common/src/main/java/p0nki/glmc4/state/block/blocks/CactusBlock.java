package p0nki.glmc4.state.block.blocks;

import p0nki.glmc4.state.block.Block;
import p0nki.glmc4.state.block.BlockState;
import p0nki.glmc4.state.properties.BooleanProperty;
import p0nki.glmc4.state.properties.IntProperty;

public class CactusBlock extends Block {

    public static final IntProperty AGE = new IntProperty("age", 0, 16);
    public static final BooleanProperty TEST = new BooleanProperty("test");

    @Override
    protected void initProperties() {
        getSchema().addProperty(AGE);
        getSchema().addProperty(TEST);
    }

    @Override
    public BlockState getDefaultState() {
        return super.getDefaultState().with(AGE, 5).with(TEST, true);
    }
}
