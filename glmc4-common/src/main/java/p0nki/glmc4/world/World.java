package p0nki.glmc4.world;

import org.joml.Rayf;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector3i;
import p0nki.glmc4.block.BlockState;
import p0nki.glmc4.utils.data.Optional;
import p0nki.glmc4.utils.math.MathUtils;
import p0nki.glmc4.utils.math.VoxelShape;
import p0nki.glmc4.world.gen.biomes.Biome;

import java.util.ArrayList;
import java.util.List;

public abstract class World {

    public static Vector2i getCoordinateInChunk(Vector2i worldPos) {
        int x = worldPos.x;
        int z = worldPos.y;
        while (x < 0) x += 16;
        while (z < 0) z += 16;
        while (x >= 16) x -= 16;
        while (z >= 16) z -= 16;
        return new Vector2i(x, z);
    }

    public static Vector2i getChunkCoordinate(Vector2i worldPos) {
        int x = worldPos.x;
        int z = worldPos.y;
        while (x < 0) x += 16;
        while (z < 0) z += 16;
        while (x >= 16) x -= 16;
        while (z >= 16) z -= 16;
        return new Vector2i((worldPos.x - x) / 16, (worldPos.y - z) / 16);
    }

    public abstract boolean isChunkLoaded(Vector2i chunkCoordinate);

    public abstract boolean isClient();

    public abstract BlockState get(Vector3i blockPos);

    public BlockState get(int x, int y, int z) {
        return get(new Vector3i(x, y, z));
    }

    public abstract byte getSunlight(Vector3i blockPos);

    public abstract Chunk getChunk(Vector2i chunkCoordinate);

    public abstract void update(Vector3i blockPos, BlockState blockState);

    public abstract List<Vector2i> getLoadedChunks();

    public abstract Biome getBiome(Vector2i position);

    public final Optional<WorldIntersection> intersectWorld(Vector3f start, Vector3f dir, int range) {
        Vector3f end = new Vector3f(start.x + dir.x * range, start.y + dir.y * range, start.z + dir.z * range);
        final float x1 = start.x;
        final float y1 = start.y;
        final float z1 = start.z;

        final float x2 = end.x;
        final float y2 = end.y;
        final float z2 = end.z;

        int i = MathUtils.floor(x1);
        int j = MathUtils.floor(y1);
        int k = MathUtils.floor(z1);

        final int di = Float.compare(x2, x1);
        final int dj = Float.compare(y2, y1);
        final int dk = Float.compare(z2, z1);

        final float dx = 1.0f / Math.abs(x2 - x1);
        final float dy = 1.0f / Math.abs(y2 - y1);
        final float dz = 1.0f / Math.abs(z2 - z1);

        final float minx = MathUtils.floor(x1), maxx = minx + 1;
        final float miny = MathUtils.floor(y1), maxy = miny + 1;
        final float minz = MathUtils.floor(z1), maxz = minz + 1;

        float tx = ((x1 > x2) ? (x1 - minx) : (maxx - x1)) * dx;
        float ty = ((y1 > y2) ? (y1 - miny) : (maxy - y1)) * dy;
        float tz = ((z1 > z2) ? (z1 - minz) : (maxz - z1)) * dz;

        ArrayList<Vector3i> list = new ArrayList<>();

        Rayf ray = new Rayf(start, dir);

        for (int num = 0; num < range; num++) {
            BlockState blockState = get(new Vector3i(i, j, k));
            VoxelShape shape = blockState.getBlock().getShape(blockState);
            ray.oX -= i;
            ray.oY -= j;
            ray.oZ -= k;
            if (shape.collidesWith(ray)) {
//            if (blockState.getBlock() != Blocks.AIR) {
                Vector3i prev = list.get(list.size() - 1);
                return Optional.of(new WorldIntersection(new Vector3i(i, j, k), new Vector3i(i - prev.x, j - prev.y, k - prev.z)));
            }
            ray.oX += i;
            ray.oY += j;
            ray.oZ += k;
            list.add(new Vector3i(i, j, k));
            if (tx <= ty && tx <= tz) {
                tx += dx;
                i += di;
            } else if (ty <= tz) {
                ty += dy;
                j += dj;
            } else {
                tz += dz;
                k += dk;
            }
        }
        return Optional.empty();
    }
}
