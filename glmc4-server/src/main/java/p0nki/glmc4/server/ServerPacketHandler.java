package p0nki.glmc4.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import p0nki.glmc4.network.packet.clientbound.PacketS2CDisconnectReason;
import p0nki.glmc4.network.packet.clientbound.PacketS2CPingRequest;
import p0nki.glmc4.network.packet.serverbound.PacketC2SPingResponse;
import p0nki.glmc4.network.packet.serverbound.PacketC2SPlayerMovement;
import p0nki.glmc4.network.packet.serverbound.ServerPacketListener;

public class ServerPacketHandler extends ServerPacketListener {

    private final static Logger LOGGER = LogManager.getLogger();

    private final static long TIMEOUT = 500;
    private final static int TIMEOUT_COUNT = 20;

    private int timeoutTicks = 0;
    private long lastPingResponse = -1;

    @Override
    public void onPingResponse(PacketC2SPingResponse packet) {
        lastPingResponse = System.currentTimeMillis();
        timeoutTicks = 0;
    }

    @Override
    public void onPlayerMovement(PacketC2SPlayerMovement packet) {
        float movement = 0.05F;
        if (packet.isForward()) getPlayerEntity().getPosition().z -= movement;
        if (packet.isBack()) getPlayerEntity().getPosition().z += movement;
        if (packet.isLeft()) getPlayerEntity().getPosition().x -= movement;
        if (packet.isRight()) getPlayerEntity().getPosition().x += movement;
    }

    @Override
    public void onConnected() {
        MinecraftServer.INSTANCE.onJoin(this);
        lastPingResponse = System.currentTimeMillis();
    }

    @Override
    public void tick() {
        getConnection().write(new PacketS2CPingRequest());
        if (System.currentTimeMillis() - lastPingResponse > TIMEOUT) {
            timeoutTicks++;
        }
        if (timeoutTicks >= TIMEOUT_COUNT) {
            getConnection().write(new PacketS2CDisconnectReason("Timed out"));
            getConnection().close();
        }
    }

    @Override
    public void onDisconnected(String reason) {
        MinecraftServer.INSTANCE.onLeave(getPlayer().getUuid());
    }
}
