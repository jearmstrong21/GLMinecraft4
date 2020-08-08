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
import org.joml.*;
import p0nki.glmc4.block.Blocks;
import p0nki.glmc4.client.gl.Framebuffer;
import p0nki.glmc4.client.gl.Mesh;
import p0nki.glmc4.client.gl.Shader;
import p0nki.glmc4.client.render.DebugRenderer3D;
import p0nki.glmc4.client.render.MeshData;
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
import p0nki.glmc4.network.packet.clientbound.PacketS2CChunkUpdate;
import p0nki.glmc4.tag.CompoundTag;
import p0nki.glmc4.utils.Identifier;
import p0nki.glmc4.utils.math.MathUtils;
import p0nki.glmc4.world.Chunk;
import p0nki.glmc4.world.gen.biomes.Biome;

import java.lang.Math;
import java.lang.Runtime;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static org.lwjgl.opengl.GL11.*;

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
    private static Framebuffer gameFramebuffer;
    private static Shader postprocessShader;
    private static Mesh postprocessMesh;

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

    public static void onChunkUpdate(PacketS2CChunkUpdate packet) {
        clientWorld.acceptUpdate(packet.getBulkUpdate());
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
        gameFramebuffer = new Framebuffer();
        postprocessShader = Shader.create("postprocess");
        MeshData postprocessMeshData = new MeshData();
        postprocessMeshData.addBuffer(2);
        postprocessMeshData.appendBuffer2f(0, List.of(new Vector2f(0, 0), new Vector2f(1, 0), new Vector2f(0, 1), new Vector2f(1, 1)));
        postprocessMeshData.appendTri(List.of(0, 1, 2, 1, 2, 3));
        postprocessMesh = new Mesh(postprocessMeshData);
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
        Vector3f cameraPosition = new Vector3f(thisEntity.getEyePosition()).sub(new Vector3f(thisEntity.getLookingAt()).mul(ClientSettings.FIRST_PERSON ? 0 : 3));
        Matrix4f view = new Matrix4f().lookAt(
                cameraPosition,
                new Vector3f(thisEntity.getEyePosition()).add(new Vector3f(thisEntity.getLookingAt()).mul(1)),
                new Vector3f(0, 1, 0)
        );

        gameFramebuffer.start();
        glClearColor(0, 0, 0, 1);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LESS);
        WorldRenderContext worldRenderContext = new WorldRenderContext(perspective, view);
        clientWorld.render(worldRenderContext);
        for (Entity entity : entities.values()) {
//            if (entity.getUuid().equals(packetListener.getPlayer().getUuid())) continue;
            EntityType<?> type = entity.getType();
            Identifier identifier = EntityTypes.REGISTRY.get(type).getKey();
            EntityRenderer<?> renderer = EntityRenderers.REGISTRY.get(identifier).getValue();
            renderer.render(worldRenderContext, entity);
        }
        gameFramebuffer.end();

        glDisable(GL_DEPTH_TEST);

        postprocessShader.use();
        postprocessShader.setFramebuffer("tex", gameFramebuffer, 0);
        postprocessShader.setBoolean("underwater", clientWorld.getOrAir(new Vector3i((int) cameraPosition.x, (int) cameraPosition.y, (int) cameraPosition.z)).getIndex() == Blocks.WATER.getIndex());
        postprocessMesh.triangles();

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
        textRenderer.renderString(-ClientSettings.CROSSHAIR_SIZE / 2, -ClientSettings.CROSSHAIR_SIZE / 2, ClientSettings.CROSSHAIR_SIZE, "+");

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
