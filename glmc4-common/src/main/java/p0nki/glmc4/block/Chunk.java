package p0nki.glmc4.block;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class Chunk {

    private final long[] data;

    public Chunk() {
        data = new long[16 * 256 * 16];
        Arrays.fill(data, 0);
    }

    public Chunk(DataInput input) throws IOException {
        data = new long[16 * 256 * 16];
        for (int i = 0; i < 16 * 256 * 16; i++) {
            data[i] = input.readLong();
        }
    }

    public BlockState getOrAir(int x, int y, int z) {
        if (x < 0 || y < 0 || z < 0 || x >= 16 || y >= 256 || z >= 16) return Blocks.AIR.getDefaultState();
        return new BlockState(data[index(x, y, z)]);
    }

    private int index(int x, int y, int z) {
        return x + 16 * y + 256 * z;
    }

    public void write(DataOutput output) throws IOException {
        for (int i = 0; i < 16 * 256 * 16; i++) {
            output.writeLong(data[i]);
        }
    }

    private void checkBounds(int x, int y, int z) {
        if (x < 0 || y < 0 || z < 0 || x >= 16 || y >= 256 || z >= 16)
            throw new ArrayIndexOutOfBoundsException(String.format("Invalid chunk coordinates %d, %d, %d", x, y, z));
    }

    public BlockState get(int x, int y, int z) {
        checkBounds(x, y, z);
        return new BlockState(data[index(x, y, z)]);
    }

    public void set(int x, int y, int z, BlockState state) {
        checkBounds(x, y, z);
        data[index(x, y, z)] = state.toLong();
    }

}
