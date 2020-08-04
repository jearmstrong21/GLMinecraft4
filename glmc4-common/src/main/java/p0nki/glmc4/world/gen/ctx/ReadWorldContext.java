package p0nki.glmc4.world.gen.ctx;

import org.joml.Vector2i;
import org.joml.Vector3i;
import p0nki.glmc4.block.BlockState;
import p0nki.glmc4.world.gen.biomes.Biome;

public interface ReadWorldContext {

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    boolean canTouch(Vector2i position);

    BlockState get(Vector3i position);

    default BlockState get(int x, int y, int z) {
        return get(new Vector3i(x, y, z));
    }

    int getGroundHeightMap(Vector2i position);

    default int getGroundHeightMap(int x, int z) {
        return getGroundHeightMap(new Vector2i(x, z));
    }

    Biome getBiome(Vector2i position);

    default Biome getBiome(int x, int z) {
        return getBiome(new Vector2i(x, z));
    }

}
