package p0nki.glmc4.world.gen;

import org.joml.Vector3i;
import p0nki.glmc4.block.BlockState;
import p0nki.glmc4.network.PacketByteBuf;
import p0nki.glmc4.server.MinecraftServer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BulkUpdate implements PacketByteBuf.Equivalent {

    private final Map<Vector3i, BlockState> blocks;
    private final Map<Vector3i, Byte> sunlights;

    public BulkUpdate() {
        blocks = new ConcurrentHashMap<>();
        sunlights = new ConcurrentHashMap<>();
    }

    public BlockState getBlock(int x, int y, int z) {
        return getBlock(new Vector3i(x, y, z));
    }

    public BlockState getBlock(Vector3i position) {
        if (blocks.containsKey(position)) {
            return blocks.get(position);
        }
        return MinecraftServer.INSTANCE.getServerWorld().getBlock(position);
    }

    public BlockState getOriginalBlock(int x, int y, int z) {
        return getOriginalBlock(new Vector3i(x, y, z));
    }

    public BlockState getOriginalBlock(Vector3i position) {
        return MinecraftServer.INSTANCE.getServerWorld().getBlock(position);
    }

    public byte getSunlight(Vector3i position) {
        if (sunlights.containsKey(position)) {
            return sunlights.get(position);
        }
        return MinecraftServer.INSTANCE.getServerWorld().getSunlight(position);
    }

    public byte getOriginalSunlight(Vector3i position) {
        return MinecraftServer.INSTANCE.getServerWorld().getSunlight(position);
    }

    public void setBlock(Vector3i position, BlockState blockState) {
        if (getBlock(position).toLong() == blockState.toLong()) return;
        blocks.put(position, blockState);
    }

    public void setBlock(int x, int y, int z, BlockState blockState) {
        setBlock(new Vector3i(x, y, z), blockState);
    }

    public void setSunlight(Vector3i position, byte sunlight) {
        if (getSunlight(position) == sunlight) return;
        sunlights.put(position, sunlight);
    }

    public void setSunlight(int x, int y, int z, byte sunlight) {
        setSunlight(new Vector3i(x, y, z), sunlight);
    }

    public Map<Vector3i, BlockState> getBlocks() {
        return blocks;
    }

    public Map<Vector3i, Byte> getSunlights() {
        return sunlights;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeInt(blocks.size());
        blocks.forEach((position, state) -> {
            buf.write3i(position);
            buf.writeEquivalent(state);
        });
        buf.writeInt(sunlights.size());
        sunlights.forEach((position, sunlight) -> {
            buf.write3i(position);
            buf.writeByte(sunlight);
        });
    }

    @Override
    public void read(PacketByteBuf buf) {
        int blockSize = buf.readInt();
        blocks.clear();
        for (int i = 0; i < blockSize; i++) {
            blocks.put(buf.read3i(), buf.readEquivalent(new BlockState(0)));
        }
        int sunlightSize = buf.readInt();
        sunlights.clear();
        for (int i = 0; i < sunlightSize; i++) {
            sunlights.put(buf.read3i(), buf.readByte());
        }
    }
}
