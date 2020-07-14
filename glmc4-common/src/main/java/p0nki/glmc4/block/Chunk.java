package p0nki.glmc4.block;

import p0nki.glmc4.network.PacketReadBuf;
import p0nki.glmc4.network.PacketWriteBuf;

public class Chunk {

    private final long[][][] data;

    public Chunk() {
        data = new long[16][256][16];
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 256; y++) {
                for (int z = 0; z < 16; z++) {
                    data[x][y][z] = 0;
                }
            }
        }
    }

    public Chunk(PacketReadBuf input) {
        data = new long[16][256][16];
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 256; y++) {
                for (int z = 0; z < 16; z++) {
                    data[x][y][z] = y < 16 ? input.readLong() : 0;
                }
            }
        }
    }

    public BlockState getOrAir(int x, int y, int z) {
        if (x < 0 || y < 0 || z < 0 || x >= 16 || y >= 256 || z >= 16) return Blocks.AIR.getDefaultState();
        return new BlockState(data[x][y][z]);
    }

    public void write(PacketWriteBuf output) {
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 16; y++) {
                for (int z = 0; z < 16; z++) {
                    output.writeLong(data[x][y][z]);
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
