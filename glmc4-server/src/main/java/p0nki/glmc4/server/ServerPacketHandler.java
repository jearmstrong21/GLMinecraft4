package p0nki.glmc4.server;

import org.joml.Vector3f;
import p0nki.glmc4.network.packet.clientbound.PacketS2CDisconnectReason;
import p0nki.glmc4.network.packet.clientbound.PacketS2CPingRequest;
import p0nki.glmc4.network.packet.serverbound.PacketC2SPingResponse;
import p0nki.glmc4.network.packet.serverbound.PacketC2SPlayerMovement;
import p0nki.glmc4.network.packet.serverbound.ServerPacketListener;
import p0nki.glmc4.utils.MathUtils;

public class ServerPacketHandler extends ServerPacketListener {

    private final static long TIMEOUT = 500;
    private final static int TIMEOUT_COUNT = 20;

    private int timeoutTicks = 0;
    private long lastPingResponse = -1;

    private boolean forward = false;
    private boolean back = false;
    private boolean left = false;
    private boolean right = false;
    private Vector3f lookAt = new Vector3f(0, 0, -1);

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
        lookAt = packet.getLookAt();
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
        tickMovement();
    }

    private void tickMovement() {
        lookAt.normalize();
        float speed = 5.0F;
        Vector3f left2d = new Vector3f(lookAt);
        left2d.y = 0;
        left2d.normalize();
        left2d.rotateY(MathUtils.PI * 0.5F);
        getPlayerEntity().getVelocity().set(0, 0, 0);
        if (forward) getPlayerEntity().getVelocity().add(new Vector3f(lookAt).mul(speed));
        if (back) getPlayerEntity().getVelocity().add(new Vector3f(lookAt).mul(-speed));
        if (left) getPlayerEntity().getVelocity().add(new Vector3f(left2d).mul(speed));
        if (right) getPlayerEntity().getVelocity().add(new Vector3f(left2d).mul(-speed));
        getPlayerEntity().getLookingAt().set(lookAt);
    }

    @Override
    public void onDisconnected(String reason) {
        MinecraftServer.INSTANCE.onLeave(getPlayer().getUuid());
    }
}
