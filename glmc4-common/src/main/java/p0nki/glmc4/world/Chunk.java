package p0nki.glmc4.world;

import org.joml.Vector3i;
import p0nki.glmc4.block.BlockState;
import p0nki.glmc4.block.Blocks;
import p0nki.glmc4.network.PacketByteBuf;
import p0nki.glmc4.utils.math.MathUtils;
import p0nki.glmc4.utils.math.SimplexGenerator;
import p0nki.glmc4.world.gen.biomes.Biome;
import p0nki.glmc4.world.gen.biomes.BiomeGenerator;
import p0nki.glmc4.world.gen.biomes.Biomes;

import java.util.concurrent.atomic.AtomicLong;

public class Chunk implements PacketByteBuf.Equivalent {
    private final long[][][] data;
    private final int[][] biomes;

    public Chunk() {
        data = new long[16][256][16];
        biomes = new int[16][16];
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                biomes[x][z] = Biomes.OCEAN.getIndex();
            }
        }
    }

    public int getHeight(int x, int z) {
        for (int y = 255; y >= 0; y--) {
            if (data[x][y][z] != 0) return y;
        }
        return 0;
    }

    private static BiomeGenerator biomeGenerator = null;
    private static SimplexGenerator simplexGenerator = null;

    private static float sampleHeightMap(Biome biome, float x, float z) {
        x *= 0.01F;
        z *= 0.01F;
        float value = simplexGenerator.simplex(x, z) + 0.5F * simplexGenerator.simplex(x * 2, z * 2) + 0.25F * simplexGenerator.simplex(x * 4, z * 4);
        value /= (1 + 0.5 + 0.25);
        value = MathUtils.map(value, -1, 1, biome.getNoiseRange().getFirst(), biome.getNoiseRange().getSecond());
        return value;
    }

    public static Chunk generateHeightMap(int cx, int cz) {
        if (biomeGenerator == null) {
            AtomicLong seed = new AtomicLong(System.currentTimeMillis());
            simplexGenerator = new SimplexGenerator(seed);
            biomeGenerator = new BiomeGenerator(seed);
        }

        Chunk c = new Chunk();

        Biome[][] biomes = biomeGenerator.generate(cx, cz);
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                c.biomes[x][z] = biomes[x][z].getIndex();
            }
        }

        int cellSize = 16;
        float[][] samples = new float[16 / cellSize + 1][16 / cellSize + 1];
        for (int i = 0; i < 16 / cellSize + 1; i++) {
            for (int j = 0; j < 16 / cellSize + 1; j++) {
                samples[i][j] = sampleHeightMap(biomes[i * cellSize][j * cellSize], cx * 16 + i * cellSize, cz * 16 + j * cellSize);
            }
        }
        int[][] heightmap = new int[16][16];
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int i = x / cellSize;
                int j = z / cellSize;
                float di = (x % cellSize) / (float) cellSize;
                float dj = (z % cellSize) / (float) cellSize;
                float minx = MathUtils.lerp(di, samples[i][j], samples[i + 1][j]);
                float maxx = MathUtils.lerp(di, samples[i][j + 1], samples[i + 1][j + 1]);
                float value = MathUtils.lerp(dj, minx, maxx);
                heightmap[x][z] = (int) value;
            }
        }

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 0; y <= heightmap[x][z]; y++) {
                    if (y < heightmap[x][z] - 4) c.set(x, y, z, Blocks.STONE.getDefaultState());
                    else if (y < heightmap[x][z]) c.set(x, y, z, Blocks.DIRT.getDefaultState());
                    else c.set(x, y, z, biomes[x][z].getTopBlockState());
                }
                if (heightmap[x][z] <= 64) {
                    for (int y = heightmap[x][z] + 1; y <= 64; y++) {
                        c.set(x, y, z, Blocks.WATER.getDefaultState());
                    }
                }
            }
        }

        return c;
    }

    public Biome getBiome(int x, int z) {
        return Biomes.REGISTRY.get(biomes[x][z]).getValue();
    }

    @Override
    public void read(PacketByteBuf buf) {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 0; y < 256; y++) {
                    data[x][y][z] = buf.readLong();
                }
                biomes[x][z] = buf.readInt();
            }
        }
    }

    public BlockState getOrAir(int x, int y, int z) {
        if (x < 0 || y < 0 || z < 0 || x >= 16 || y >= 256 || z >= 16) return Blocks.AIR.getDefaultState();
        return new BlockState(data[x][y][z]);
    }

    @Override
    public void write(PacketByteBuf buf) {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 0; y < 256; y++) {
                    buf.writeLong(data[x][y][z]);
                }
                buf.writeInt(biomes[x][z]);
            }
        }
    }

    private void checkYBounds(int y) {
        if (y < 0 || y > 255) throw new ArrayIndexOutOfBoundsException("Invalid chunk y coordinate " + y);
    }

    private void checkBounds(int x, int y, int z) {
        if (x < 0 || y < 0 || z < 0 || x >= 16 || y >= 256 || z >= 16)
            throw new ArrayIndexOutOfBoundsException(String.format("Invalid chunk coordinates %d, %d, %d", x, y, z));
    }

    private void checkXZBounds(int x, int z) {
        if (x < 0 || z < 0 || x >= 16 || z >= 16)
            throw new ArrayIndexOutOfBoundsException(String.format("Invalid chunk xz coordinates %d, %d", x, z));
    }

    public BlockState get(Vector3i blockPos) {
        return get(blockPos.x, blockPos.y, blockPos.z);
    }

    public BlockState get(int x, int y, int z) {
        checkXZBounds(x, z);
        if (y < 0 || y > 255) return Blocks.AIR.getDefaultState();
        return new BlockState(data[x][y][z]);
    }

    public void set(int x, int y, int z, BlockState state) {
        checkBounds(x, y, z);
        data[x][y][z] = state.toLong();
    }

}