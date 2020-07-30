package p0nki.glmc4.block.blocks;

import p0nki.glmc4.block.Block;
import p0nki.glmc4.block.BlockState;
import p0nki.glmc4.state.EnumProperty;
import p0nki.glmc4.utils.math.Axis;

public class OakLogBlock extends Block {

    public static final EnumProperty<Axis> AXIS = new EnumProperty<>("axis", Axis.class);

    @Override
    protected void initProperties() {
        getSchema().addProperty(AXIS);
    }

    @Override
    public BlockState getDefaultState() {
        return super.getDefaultState().with(AXIS, Axis.Y);
    }
}
