package p0nki.glmc4.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector3f;
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

    private boolean forward = false;
    private boolean back = false;
    private boolean left = false;
    private boolean right = false;

    @Override
    public void onPingResponse(PacketC2SPingResponse packet) {
        lastPingResponse = System.currentTimeMillis();
        timeoutTicks = 0;
    }

    @Override
    public void onPlayerMovement(PacketC2SPlayerMovement packet) {
        forward = packet.isForward();
        back = packet.isBack();
        left = packet.isLeft();
        right = packet.isRight();
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
        float speed = 5.0F;
        Vector3f velocity = new Vector3f();
        if (forward) velocity.z -= speed;
        if (back) velocity.z += speed;
        if (left) velocity.x -= speed;
        if (right) velocity.x += speed;
        getPlayerEntity().getVelocity().set(velocity);
    }

    @Override
    public void onDisconnected(String reason) {
        MinecraftServer.INSTANCE.onLeave(getPlayer().getUuid());
    }
}
