package p0nki.glmc4.block;

import p0nki.glmc4.registry.AfterRegisterCallback;
import p0nki.glmc4.registry.Registrable;
import p0nki.glmc4.registry.Registry;
import p0nki.glmc4.state.PropertySchema;
import p0nki.glmc4.utils.Identifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Block extends Registrable<Block> implements AfterRegisterCallback {

    private final PropertySchema schema;
    private List<BlockState> states;

    protected Block() {
        schema = new PropertySchema();
        initProperties();
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

    public PropertySchema getSchema() {
        return schema;
    }

    public BlockState getDefaultState() {
        return new BlockState((long) Blocks.REGISTRY.get(this).getIndex() << 32);
    }

    public List<BlockState> getStates() {
        return states;
    }
}
