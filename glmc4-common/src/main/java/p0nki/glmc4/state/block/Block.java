package p0nki.glmc4.state.block;

import p0nki.glmc4.state.properties.PropertySchema;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Block {

    private final PropertySchema schema;
    private List<BlockState> states;

    protected Block() {
        schema = new PropertySchema();
        initProperties();
    }

    final void afterRegister() {
        states = new ArrayList<>();
        schema.forEach(objects -> {
            BlockState state = getDefaultState();
            for (int i = 0; i < objects.size(); i++) {
                state.withUnsafe(schema.getProperties().get(i), objects.get(i));
            }
            states.add(state);
        });
        states = Collections.unmodifiableList(states);
    }

    protected void initProperties() {

    }

    public PropertySchema getSchema() {
        return schema;
    }

    public BlockState getDefaultState() {
        return new BlockState(Blocks.REGISTRY.get(this).getIndex(), 0);
    }

    public List<BlockState> getStates() {
        return states;
    }
}
