package p0nki.glmc4.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector3i;
import p0nki.glmc4.block.BlockState;
import p0nki.glmc4.client.render.DebugRenderer3D;
import p0nki.glmc4.client.render.TextRenderer;
import p0nki.glmc4.client.render.WorldRenderContext;
import p0nki.glmc4.client.render.entity.EntityRenderer;
import p0nki.glmc4.client.render.entity.EntityRenderers;
import p0nki.glmc4.entity.Entity;
import p0nki.glmc4.entity.EntityType;
import p0nki.glmc4.entity.EntityTypes;
import p0nki.glmc4.network.NetworkHandler;
import p0nki.glmc4.network.PacketCodec;
import p0nki.glmc4.network.packet.clientbound.ClientPacketListener;
import p0nki.glmc4.tag.CompoundTag;
import p0nki.glmc4.utils.Identifier;
import p0nki.glmc4.utils.math.MathUtils;
import p0nki.glmc4.world.Chunk;
import p0nki.glmc4.world.gen.biomes.Biome;

import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class GLMC4Client {

    public static String resourcePath(String name) {
        try {
            return Paths.get(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResource(name)).toURI()).toString();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Marker SOCKET = MarkerManager.getMarker("SOCKET");
    private static final Marker RENDER = MarkerManager.getMarker("RENDER");
    private static ClientWorld clientWorld;
    private static ClientPacketListener packetListener;

    public static TextRenderer textRenderer;
    private static final Map<UUID, Long> lastReceivedEntityUpdate = new HashMap<>();
    public static DebugRenderer3D debugRenderer3D;
    private static Map<UUID, Entity> entities = new HashMap<>();
    private static String lastFreeMem = "n/a";
    private static String lastTotalMem = "n/a";
    private static String lastMaxMem = "n/a";
    private static float lastFrameTime = 0;

    public static long getLastUpdateTime(UUID uuid) {
        return lastReceivedEntityUpdate.get(uuid);
    }

    public static ClientWorld getClientWorld() {
        return clientWorld;
    }

    public static void loadInitialEntities(List<Entity> entities) {
        GLMC4Client.entities = entities.stream().collect(Collectors.toMap(Entity::getUuid, entity -> entity));
        entities.forEach(entity -> lastReceivedEntityUpdate.put(entity.getUuid(), System.currentTimeMillis()));
    }

    public static Vector3f lookDir = new Vector3f(0, 0, -1);

    public static void spawnEntity(Entity entity) {
        entities.put(entity.getUuid(), entity);
        lastReceivedEntityUpdate.put(entity.getUuid(), System.currentTimeMillis());
    }

    public static void despawnEntity(UUID uuid) {
        entities.remove(uuid);
        lastReceivedEntityUpdate.remove(uuid);
    }

    public static void onLoadChunk(int x, int z, Chunk chunk) {
        clientWorld.loadChunk(new Vector2i(x, z), chunk);
    }

    public static void onChunkUpdate(Vector3i blockPos, BlockState newState) {
        clientWorld.update(blockPos, newState);
    }

    public static void updateEntity(UUID uuid, CompoundTag newData) {
        entities.get(uuid).fromTag(newData);
        if (uuid.equals(packetListener.getPlayer().getUuid())) {
            lookDir = entities.get(uuid).getLookingAt();
        }
        lastReceivedEntityUpdate.put(uuid, System.currentTimeMillis());
    }

    private static void initializeRenderer() {
        MCWindow.captureMouse();
        clientWorld = new ClientWorld();
        textRenderer = new TextRenderer();
        debugRenderer3D = new DebugRenderer3D();
        LOGGER.info(RENDER, "Renderer initialized");
        EntityRenderers.REGISTRY.getEntries().forEach(entry -> entry.getValue().initialize());
        new Thread(GLMC4Client::runNetty, "Netty Main Thread").start();
    }

    private static void mouseMoveRenderer(double x, double y) {
        x -= MCWindow.getWidth() / 2.0F;
        y -= MCWindow.getHeight() / 2.0F;
        float dx = MathUtils.map((float) y, -10, 10, -MathUtils.PI * 0.5F, MathUtils.PI * 0.5F);
        float dy = MathUtils.map((float) x, -10, 10, MathUtils.PI, -MathUtils.PI);
        dx *= 0.01F;
        dy *= 0.01F;
        dx = MathUtils.clamp(dx, -0.49F * MathUtils.PI, 0.49F * MathUtils.PI);
        lookDir = new Vector3f(0, 0, 1).rotateX(dx).rotateY(dy);
    }

    public static Entity getThisEntity() {
        return entities.get(packetListener.getPlayer().getUuid());
    }

    private static void tickRenderer(int frameCount) {
        if (packetListener == null) return;
        if (packetListener.getPlayer() == null) return;
        if (!entities.containsKey(packetListener.getPlayer().getUuid())) return;

        Matrix4f perspective = new Matrix4f().perspective((float) Math.toRadians(80), 1.0F, 0.001F, 300);
        Entity thisEntity = getThisEntity();
        Matrix4f view = new Matrix4f().lookAt(
                new Vector3f(thisEntity.getEyePosition()).sub(new Vector3f(thisEntity.getLookingAt()).mul(3)),
                new Vector3f(thisEntity.getEyePosition()),
                new Vector3f(0, 1, 0)
        );

        WorldRenderContext worldRenderContext = new WorldRenderContext(perspective, view);
        clientWorld.render(worldRenderContext);

        for (Entity entity : entities.values()) {
//            if (entity.getUuid().equals(packetListener.getPlayer().getUuid())) continue;
            EntityType<?> type = entity.getType();
            Identifier identifier = EntityTypes.REGISTRY.get(type).getKey();
            EntityRenderer<?> renderer = EntityRenderers.REGISTRY.get(identifier).getValue();
            renderer.render(worldRenderContext, entity);
        }
        System.runFinalization();
        textRenderer.renderString(-1, 1 - 0.04F, 0.04F, String.format("GLMinecraft4\n" +
                        "FPS: %s\n" +
                        "Position: %s\n" +
                        "Looking: %s\n" +
                        "World stats: %s\n" +
                        "Free memory: %s\n" +
                        "Total memory: %s\n" +
                        "Maximum memory: %s\n" +
                        "Biome: %s",
                MCWindow.getFps(),
                thisEntity.getPosition(),
                lookDir,
                clientWorld.worldStats(),
                lastFreeMem,
                lastTotalMem,
                lastMaxMem,
                clientWorld.getOptionalBiome(new Vector2i((int) thisEntity.getPosition().x, (int) thisEntity.getPosition().z)).map(Biome::getKey).map(Identifier::toString).orElse("not yet loaded")
        ));

        packetListener.tick();
        if ((int) MCWindow.time() != (int) lastFrameTime) {
            lastFreeMem = MathUtils.readableBytes(Runtime.getRuntime().freeMemory());
            lastTotalMem = MathUtils.readableBytes(Runtime.getRuntime().totalMemory());
            lastMaxMem = MathUtils.readableBytes(Runtime.getRuntime().maxMemory());
        }
        lastFrameTime = MCWindow.time();
    }

    private static void runRenderer() {
        try {
            MCWindow.setInitializeCallback(GLMC4Client::initializeRenderer);
            MCWindow.setMouseMoveCallback(GLMC4Client::mouseMoveRenderer);
            MCWindow.setFrameCallback(GLMC4Client::tickRenderer);
            MCWindow.setEndCallback(GLMC4Client::endRenderer);
            MCWindow.start();
        } catch (Error | RuntimeException error) {
            error.printStackTrace();
            System.exit(0);
        }
    }

    private static void endRenderer() {
        LOGGER.info(RENDER, "Rendering thread ended.");
        System.exit(0);
    }

    private static void runNetty() {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        packetListener = new ClientPacketHandler();
        Bootstrap bootstrap = new Bootstrap()
                .group(workerGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<>() {
                    @Override
                    protected void initChannel(Channel ch) {
                        ch.pipeline().addLast(new PacketCodec(), new NetworkHandler<>(packetListener));
                    }
                });
        bootstrap.connect("localhost", 8080);
        LOGGER.info(SOCKET, "Connected to localhost:8080");
    }

    public static void main(String[] args) {
        ClientBootstrap.initialize();
        runRenderer();
    }

}
