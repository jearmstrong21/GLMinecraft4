package p0nki.glmc4.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import p0nki.glmc4.block.Blocks;
import p0nki.glmc4.block.Chunk;
import p0nki.glmc4.block.blocks.GrassBlock;
import p0nki.glmc4.network.ClientConnection;
import p0nki.glmc4.network.packet.clientbound.PacketS2CChunkLoad;
import p0nki.glmc4.network.packet.clientbound.PacketS2CHello;
import p0nki.glmc4.network.packet.clientbound.PacketS2CPingRequest;
import p0nki.glmc4.network.packet.serverbound.PacketC2SPingResponse;
import p0nki.glmc4.network.packet.serverbound.ServerPacketListener;
import p0nki.glmc4.utils.MathUtils;

import java.util.Random;

public class ServerPacketHandler implements ServerPacketListener {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Marker WORLD = MarkerManager.getMarker("WORLD");
    private final ClientConnection<ServerPacketListener> connection;
    private long lastPingResponse = -1;
    private boolean hasSentPing = false;

    public ServerPacketHandler(ClientConnection<ServerPacketListener> connection) {
        this.connection = connection;
    }

    private static Chunk generate(int cx, int cz) {
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
//        LOGGER.info(WORLD, "Generated chunk {}, {}", cx, cz);
        return c;
    }

    @Override
    public void onPingResponse(PacketC2SPingResponse packet) {
        lastPingResponse = System.currentTimeMillis();
    }

    @Override
    public void writePing() {
        if (!hasSentPing) lastPingResponse = System.currentTimeMillis();
        hasSentPing = true;
        connection.write(new PacketS2CPingRequest());
    }

    @Override
    public boolean isDead() {
        return hasSentPing && System.currentTimeMillis() - lastPingResponse > 3000;
    }

    @Override
    public void onConnected() {
        connection.write(new PacketS2CHello(connection.getPlayer(), MinecraftServer.INSTANCE.getPlayers()));
        for (int x = -4; x <= 4; x++) {
            for (int z = -4; z <= 4; z++) {
                connection.write(new PacketS2CChunkLoad(x, z, generate(x, z)));
            }
        }
//        connection.write(new PacketS2CChunkLoad(0, 0, generate(0, 0)));
    }

    @Override
    public void onDisconnected(String reason) {
        LOGGER.info("Disconnected {}", reason);
    }

    @Override
    public ClientConnection<ServerPacketListener> getConnection() {
        return connection;
    }
}
