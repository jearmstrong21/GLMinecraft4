package p0nki.glmc4.network.packet.serverbound;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import p0nki.glmc4.block.Blocks;
import p0nki.glmc4.block.Chunk;
import p0nki.glmc4.network.ClientConnection;
import p0nki.glmc4.network.packet.PacketListener;
import p0nki.glmc4.network.packet.clientbound.PacketS2CChunkLoad;
import p0nki.glmc4.network.packet.clientbound.PacketS2CHello;
import p0nki.glmc4.network.packet.clientbound.PacketS2CPingRequest;
import p0nki.glmc4.server.MinecraftServer;

public class ServerPacketListener implements PacketListener<ServerPacketListener> {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Marker WORLDGEN = MarkerManager.getMarker("WORLDGEN");

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
                    else c.set(x, y, z, Blocks.GRASS.getDefaultState());
                }
            }
        }
        LOGGER.info(WORLDGEN, "Generated chunk {}, {}", cx, cz);
        return c;
    }

    private final ClientConnection<ServerPacketListener> connection;

    public ServerPacketListener(ClientConnection<ServerPacketListener> connection) {
        this.connection = connection;
    }

    public void onPingResponse(PacketC2SPingResponse packet) {
        lastPingResponse = System.currentTimeMillis();
    }

    private long lastPingResponse = -1;
    private boolean hasSentPing = false;

    public void writePing() {
        if (!hasSentPing) lastPingResponse = System.currentTimeMillis();
        hasSentPing = true;
        connection.write(new PacketS2CPingRequest());
    }

    public boolean isDead() {
        return hasSentPing && System.currentTimeMillis() - lastPingResponse > 3000;
    }

    @Override
    public void onConnected() {
        connection.write(new PacketS2CHello(connection.getPlayer(), MinecraftServer.INSTANCE.getPlayers()));
//        for (int x = -2; x <= 2; x++) {
//            for (int z = -2; z <= 2; z++) {
//                connection.write(new PacketS2CChunkLoad(x, z, generate(x, z)));
//            }
//        }
        connection.write(new PacketS2CChunkLoad(0, 0, generate(0, 0)));
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
