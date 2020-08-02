package p0nki.glmc4.world;

import org.joml.Vector3i;
import p0nki.glmc4.block.BlockState;
import p0nki.glmc4.block.Blocks;
import p0nki.glmc4.network.PacketByteBuf;
import p0nki.glmc4.utils.math.SimplexNoiseGenerator;
import p0nki.glmc4.world.gen.biomes.Biome;
import p0nki.glmc4.world.gen.biomes.BiomeGenerator;
import p0nki.glmc4.world.gen.biomes.Biomes;

public class Chunk implements PacketByteBuf.Equivalent {
    public static final long seed = System.currentTimeMillis();
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

    public static Chunk generate(int cx, int cz) {
        Chunk c = new Chunk();
        int[][] biomes = BiomeGenerator.generate(seed, 16, 16, cx, cz);
        SimplexNoiseGenerator generator = new SimplexNoiseGenerator(0.05f, seed);

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                c.biomes[x][z] = biomes[x][z];
                Biome b = Biomes.REGISTRY.get(c.biomes[x][z]).getValue();
                int genX = x + (16 * cx);
                int genZ = z + (16 * cz);
                int y = (int) (8 + 4 * generator.generate(genX, genZ));
                c.set(x, y, z, b.getTopBlockState());
                for (int i = y - 1; i > 0; i--) {
                    c.set(x, i, z, Blocks.STONE.getDefaultState());
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