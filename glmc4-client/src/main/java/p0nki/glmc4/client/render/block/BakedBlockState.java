package p0nki.glmc4.client.render.block;

import p0nki.glmc4.block.Block;
import p0nki.glmc4.block.BlockState;
import p0nki.glmc4.block.Blocks;
import p0nki.glmc4.client.gl.MeshData;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class BakedBlockState {

    private final Block block;
    private final Map<Integer, MeshData> data;

    public BakedBlockState(Block block, Function<BlockState, MeshData> function) {
        this.block = block;
        data = new HashMap<>();
        block.getStates().forEach(blockState -> data.put(blockState.getMeta(), function.apply(blockState)));
    }

    public MeshData render(BlockState blockState) {
        if (blockState.getBlock() != block)
            throw new IllegalArgumentException("Cannot render " + blockState.toString() + " with block " + Blocks.REGISTRY.get(block).getKey());
        if (!data.containsKey(blockState.getMeta()))
            throw new IllegalArgumentException("Unsupported meta " + blockState + ": " + blockState.getMeta());
        return data.get(blockState.getMeta());
    }

}
