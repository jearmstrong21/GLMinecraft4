package p0nki.glmc4.block;

import p0nki.glmc4.block.blocks.GrassBlock;
import p0nki.glmc4.network.PacketByteBuf;
import p0nki.glmc4.utils.MathUtils;

import java.util.Random;

public class Chunk implements PacketByteBuf.Equivalent {

    private final long[][][] data;

    public Chunk() {
        data = new long[16][256][16];
    }

    public static Chunk generate(int cx, int cz) {
        Chunk c = new Chunk();
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int rx = x + cx * 16;
                int rz = z + cz * 16;
                int h = Math.abs(rx - rz) + 2;
                for (int y = 0; y <= h; y++) {
                    if (y < h - 4) c.set(x, y, z, Blocks.STONE.getDefaultState());
                    else if (y < h) c.set(x, y, z, Blocks.DIRT.getDefaultState());
                    else {
                        Random random = new Random(MathUtils.pack(rx, rz));
                        random.nextFloat();
                        random.nextFloat();
                        random.nextFloat();
                        random.nextFloat();
                        c.set(x, y, z, Blocks.GRASS.getDefaultState().with(GrassBlock.SNOWED, random.nextBoolean()));
                    }
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

    public BlockState get(int x, int y, int z) {
        checkBounds(x, y, z);
        return new BlockState(data[x][y][z]);
    }

    public void set(int x, int y, int z, BlockState state) {
        checkBounds(x, y, z);
        data[x][y][z] = state.toLong();
    }

}
