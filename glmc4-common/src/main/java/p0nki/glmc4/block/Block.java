package p0nki.glmc4.block;

import org.joml.AABBf;
import p0nki.glmc4.registry.AfterRegisterCallback;
import p0nki.glmc4.registry.Registrable;
import p0nki.glmc4.registry.Registry;
import p0nki.glmc4.state.PropertySchema;
import p0nki.glmc4.utils.Identifier;
import p0nki.glmc4.utils.math.VoxelShape;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Block extends Registrable<Block> implements AfterRegisterCallback {

    private final PropertySchema schema;
    private List<BlockState> states;

    protected Block() {
        schema = new PropertySchema();
        initProperties();
    }

    public VoxelShape getShape(BlockState blockState) {
        return VoxelShape.of(new AABBf(0, 0, 0, 1, 1, 1), "1x1x1 cube");
    }

    @Override
    public Registry<Block> getRegistry() {
        return Blocks.REGISTRY;
    }

    @Override
    public Block getValue() {
        return this;
    }

    @Override
    public void onAfterRegister(Identifier identifier, int index) {
        // This must be onAfterRegister since getDefaultState() requires
        //  that the block is already registered and that the numerical ID known
        states = new ArrayList<>();
        schema.forEach(objects -> {
            BlockState state = getDefaultState();
            for (int i = 0; i < objects.size(); i++) {
                state = state.withUnsafe(schema.getProperties().get(i), objects.get(i));
            }
            states.add(state);
        });
//        LOGGER.debug(identifier, "{} states, {} properties", states.size(), schema.getProperties().size());
        states = Collections.unmodifiableList(states);
    }

    protected void initProperties() {

    }

    public boolean isFullBlock(BlockState blockState) {
        return true;
    }

    public PropertySchema getSchema() {
        return schema;
    }

    public BlockState getDefaultState() {
        return new BlockState((long) Blocks.REGISTRY.get(this).getIndex() << 32);
    }

    public List<BlockState> getStates() {
        return states;
    }

    public byte getBlockedSunlight() {
        return 16;
    }

    public float getAOContribution() {
        return 0;
    }
}
