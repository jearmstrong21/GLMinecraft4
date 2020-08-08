package p0nki.glmc4.world;

import org.joml.Vector2i;
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

    public BlockState getBlock(Vector3i blockPos) {
        return getBlock(blockPos.x, blockPos.y, blockPos.z);
    }

    public BlockState getBlock(int x, int y, int z) {
        if (y < 0 || y > 255) return Blocks.AIR.getDefaultState();
        return new BlockState(data[x][y][z]);
    }

    public void setBlock(int x, int y, int z, BlockState state) {
        data[x][y][z] = state.toLong();
    }

    public Biome getBiome(Vector2i position) {
        return getBiome(position.x, position.y);
    }

    public void setSunlight(int x, int y, int z, byte sunlight) {
        this.sunlight[x][y][z] = sunlight;
    }

    public void setSunlight(Vector3i position, byte sunlight) {
        setSunlight(position.x, position.y, position.z, sunlight);
    }

    public enum HeightMapType implements Predicate<BlockState> {
        FULL_NOT_WATER {
            @Override
            public boolean test(BlockState blockState) {
                return blockState.getIndex() != Blocks.WATER.getIndex() && blockState.isFullBlock();
            }
        }
    }
}