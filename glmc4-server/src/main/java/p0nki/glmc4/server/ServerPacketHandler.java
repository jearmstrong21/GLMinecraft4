package p0nki.glmc4.server;

import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector3i;
import p0nki.glmc4.block.Blocks;
import p0nki.glmc4.network.packet.clientbound.PacketS2CChunkLoad;
import p0nki.glmc4.network.packet.clientbound.PacketS2CDisconnectReason;
import p0nki.glmc4.network.packet.clientbound.PacketS2CPingRequest;
import p0nki.glmc4.network.packet.serverbound.PacketC2SPingResponse;
import p0nki.glmc4.network.packet.serverbound.PacketC2SPlayerMovement;
import p0nki.glmc4.network.packet.serverbound.ServerPacketListener;
import p0nki.glmc4.utils.math.MathUtils;
import p0nki.glmc4.world.World;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class ServerPacketHandler extends ServerPacketListener {

    private final static long TIMEOUT = 500;
    private final static int TIMEOUT_COUNT = 20;

    private int timeoutTicks = 0;
    private long lastPingResponse = -1;

    private boolean forward = false;
    private boolean back = false;
    private boolean left = false;
    private boolean right = false;
    private boolean jump = false;
    private boolean sprint = false;
    private boolean M1 = false;
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
        jump = packet.isJump();
        sprint = packet.isSprint();
        M1 = packet.isM1();
        lookAt = packet.getLookAt();
    }

    private final Set<Vector2i> clientChunks = new HashSet<>();
    private final int viewDistance = 3;

    private void forViewDistance(Consumer<Vector2i> consumer) {
        Vector2i startChunkCoordinate = World.getChunkCoordinate(new Vector2i((int) getPlayerEntity().getPosition().x, (int) getPlayerEntity().getPosition().z));
        for (int x = -viewDistance; x <= viewDistance; x++) {
            for (int z = -viewDistance; z <= viewDistance; z++) {
                consumer.accept(new Vector2i(x, z).add(startChunkCoordinate));
            }
        }
    }

    @Override
    public void onConnected() {
        MinecraftServer.INSTANCE.onJoin(this);
        lastPingResponse = System.currentTimeMillis();
        forViewDistance(MinecraftServer.INSTANCE.getServerWorld()::queueLoad);
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
        forViewDistance(v -> {
            if (clientChunks.contains(v)) return;
            if (MinecraftServer.INSTANCE.getServerWorld().isChunkLoaded(v)) {
                clientChunks.add(v);
                getConnection().write(new PacketS2CChunkLoad(v.x, v.y, MinecraftServer.INSTANCE.getServerWorld().getChunk(v)));
            } else {
                MinecraftServer.INSTANCE.getServerWorld().queueLoad(v);
            }
        });
    }

    private void tickMovement() {
        lookAt.normalize();
        float speed = 5.0F;
        Vector3f look2d = new Vector3f(lookAt);
        look2d.y = 0;
        look2d.normalize();
        Vector3f left2d = new Vector3f(look2d);
        left2d.rotateY(MathUtils.PI * 0.5F);
        getPlayerEntity().getMotion().set(0, 0, 0);
        if (forward) getPlayerEntity().getMotion().add(new Vector3f(look2d).mul(speed).mul(sprint ? 4 : 1));
        if (back) getPlayerEntity().getMotion().add(new Vector3f(look2d).mul(-speed));
        if (left) getPlayerEntity().getMotion().add(new Vector3f(left2d).mul(speed));
        if (right) getPlayerEntity().getMotion().add(new Vector3f(left2d).mul(-speed));
        if (jump) {
            if (getPlayerEntity().isGrounded())
                getPlayerEntity().getVelocity().add(0, getPlayerEntity().getJumpPower(), 0);
            jump = false;
        }
        if (M1) {
            for (int x = -5; x <= 5; x++) {
                for (int y = -5; y <= 5; y++) {
                    for (int z = -5; z <= 5; z++) {
                        MinecraftServer.INSTANCE.getServerWorld().update(new Vector3i(x + (int) getPlayerEntity().getEyePosition().x, y + (int) getPlayerEntity().getEyePosition().y, z + (int) getPlayerEntity().getEyePosition().z), Blocks.AIR.getDefaultState());
                    }
                }
            }
        }
        getPlayerEntity().getLookingAt().set(lookAt);
    }

    @Override
    public void onDisconnected(String reason) {
        MinecraftServer.INSTANCE.onLeave(getPlayer().getUuid());
    }
}
