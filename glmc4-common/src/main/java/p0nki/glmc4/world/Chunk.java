package p0nki.glmc4.world;

import org.joml.Vector3i;
import p0nki.glmc4.block.BlockState;
import p0nki.glmc4.block.Blocks;
import p0nki.glmc4.network.PacketByteBuf;
import p0nki.glmc4.world.gen.biomes.Biome;
import p0nki.glmc4.world.gen.biomes.Biomes;

import java.util.function.Predicate;

public class Chunk implements PacketByteBuf.Equivalent {
    private final long[][][] data;
    private final byte[][][] sunlight;
    private final int[][] biomes;

    public Chunk() {
        data = new long[16][256][16];
        sunlight = new byte[16][256][16];
        biomes = new int[16][16];
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                biomes[x][z] = Biomes.OCEAN.getIndex();
            }
        }
    }

    public byte[][][] getSunlight() {
        return sunlight;
    }

    public byte getSunlight(int x, int y, int z) {
        if (y < 0 || y >= 256) {
            return 16;
        }
        return sunlight[x][y][z];
    }

    public int getHeight(int x, int z, HeightMapType heightMapType) {
        for (int y = 255; y >= 0; y--) {
            if (heightMapType.test(get(x, y, z))) return y;
        }
        return 0;
    }

    public void setBiome(int x, int z, Biome biome) {
        biomes[x][z] = biome.getIndex();
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
                    sunlight[x][y][z] = buf.readByte();
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
                    buf.writeByte(sunlight[x][y][z]);
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

    public enum HeightMapType implements Predicate<BlockState> {
        FULL_NOT_WATER {
            @Override
            public boolean test(BlockState blockState) {
                return blockState.getBlock() != Blocks.WATER && blockState.getBlock().isFullBlock(blockState);
            }
        }
    }
}