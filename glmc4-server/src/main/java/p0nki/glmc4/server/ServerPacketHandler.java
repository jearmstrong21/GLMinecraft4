package p0nki.glmc4.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import p0nki.glmc4.block.Chunk;
import p0nki.glmc4.network.packet.clientbound.PacketS2CChatMessage;
import p0nki.glmc4.network.packet.clientbound.PacketS2CChunkLoad;
import p0nki.glmc4.network.packet.serverbound.PacketC2SPingResponse;
import p0nki.glmc4.network.packet.serverbound.PacketC2SPlayerMovement;
import p0nki.glmc4.network.packet.serverbound.ServerPacketListener;

public class ServerPacketHandler extends ServerPacketListener {

    private final static Logger LOGGER = LogManager.getLogger();

    @Override
    public void onPingResponse(PacketC2SPingResponse packet) {

    }

    @Override
    public void onPlayerMovement(PacketC2SPlayerMovement packet) {

    }

    @Override
    public void onConnected() {
        LOGGER.info("Connected");
        getConnection().write(new PacketS2CChatMessage("SERVER", "Welcome to the server! Enjoy your stay"));
        for (int x = -4; x <= 4; x++) {
            for (int z = -4; z <= 4; z++) {
                getConnection().write(new PacketS2CChunkLoad(x, z, Chunk.generate(x, z)));
            }
        }
    }

    @Override
    public void tick() {
        LOGGER.info("Tick");
    }

    @Override
    public void onDisconnected(String reason) {
        LOGGER.info("Disconnected");
    }
}
