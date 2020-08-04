package p0nki.glmc4.world.gen.ctx;

import org.joml.Vector3i;
import p0nki.glmc4.block.BlockState;

public interface ReadWriteWorldContext extends ReadWorldContext {

    void set(Vector3i position, BlockState blockState);

    default void set(int x, int y, int z, BlockState blockState) {
        set(new Vector3i(x, y, z), blockState);
    }

}
