package p0nki.glmc4.block;

import org.joml.Vector3i;
import p0nki.glmc4.network.PacketByteBuf;

public class Chunk implements PacketByteBuf.Equivalent {
    private final long[][][] data;
//    public static final long seed = System.currentTimeMillis();

    public Chunk() {
        data = new long[16][256][16];
    }

    public static Chunk generate(int cx, int cz) {
        Chunk c = new Chunk();
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int rx = cx * 16 + x;
                int rz = cz * 16 + z;
                int h;
                if (-4 < rx && rx < 4 && -4 < rz && rz < 4) {
                    h = 4;
                } else {
                    h = 10;
                }
                if (rx == 0 && rz == 0) h = 2;
//                int h = Math.abs(Math.abs(rx) - Math.abs(rz));
                for (int y = 0; y <= h; y++) {
                    if (y < h - 3) c.set(x, y, z, Blocks.STONE.getDefaultState());
                    else if (y < h) c.set(x, y, z, Blocks.DIRT.getDefaultState());
                    else c.set(x, y, z, Blocks.GRASS.getDefaultState());
                }
            }
        }
        return c;
//        var biomes = Generator.generate(seed, 16, 16, cx, cz);
//
//        for (int x = 0; x < 16; x++) {
//            for (int z = 0; z < 16; z++) {
//                Biome b = Biomes.BIOMES.get(biomes[x][z]);
//                c.set(x, 1, z, b.topBlock.getDefaultState());
//            }
//        }
//        return c;
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
