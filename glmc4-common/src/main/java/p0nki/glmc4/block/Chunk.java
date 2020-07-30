package p0nki.glmc4.block;

import org.joml.Vector3i;
import p0nki.glmc4.network.PacketByteBuf;
import p0nki.glmc4.wgen.Biome;
import p0nki.glmc4.wgen.Biomes;
import p0nki.glmc4.wgen.Generator;
import p0nki.glmc4.wgen.SimplexNoiseGenerator;

public class Chunk implements PacketByteBuf.Equivalent {
    public static final long seed = System.currentTimeMillis();
    private final long[][][] data;

    public Chunk() {
        data = new long[16][256][16];
    }

    public static Chunk generate(int cx, int cz) {
        Chunk c = new Chunk();
        int[][] biomes = Generator.generate(seed, 16, 16, cx, cz);
        SimplexNoiseGenerator generator = new SimplexNoiseGenerator(0.05f, seed);

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                Biome b = Biomes.BIOMES.get(biomes[x][z]);
                int genX = x + (16 * cx);
                int genZ = z + (16 * cz);
                // AAIAIAJUAIAIAIIAIAIAIA
                int noise = generator.generate(genX, genZ);
                int y = (int) (8 + 4 * noise + (noise * 0.5f) + (noise * 0.25f));
                c.set(x, y, z, b.topBlock.getDefaultState());
                for (int i = y - 1; i > 0; i--) {
                    c.set(x, i, z, Blocks.STONE.getDefaultState());
                }

            }
        }
        return c;
    }

    @Override
    public void read(PacketByteBuf buf) {
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 256; y++) {
                for (int z = 0; z < 16; z++) {
                    data[x][y][z] = buf.readLong();
                }
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
            for (int y = 0; y < 256; y++) {
                for (int z = 0; z < 16; z++) {
                    buf.writeLong(data[x][y][z]);
                }
            }
        }
    }

    private void checkBounds(int x, int y, int z) {
        if (x < 0 || y < 0 || z < 0 || x >= 16 || y >= 256 || z >= 16)
            throw new ArrayIndexOutOfBoundsException(String.format("Invalid chunk coordinates %d, %d, %d", x, y, z));
    }

    public BlockState get(Vector3i blockPos) {
        return get(blockPos.x, blockPos.y, blockPos.z);
    }

    public BlockState get(int x, int y, int z) {
        checkBounds(x, y, z);
        return new BlockState(data[x][y][z]);
    }

    public void set(int x, int y, int z, BlockState state) {
        checkBounds(x, y, z);
        data[x][y][z] = state.toLong();
    }

}
