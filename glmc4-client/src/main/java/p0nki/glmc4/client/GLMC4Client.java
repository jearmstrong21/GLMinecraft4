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
import org.joml.Vector3f;
import p0nki.glmc4.block.BlockState;
import p0nki.glmc4.block.Blocks;
import p0nki.glmc4.block.Chunk;
import p0nki.glmc4.client.gl.Mesh;
import p0nki.glmc4.client.gl.Shader;
import p0nki.glmc4.client.gl.Texture;
import p0nki.glmc4.client.render.DebugRenderer3D;
import p0nki.glmc4.client.render.MeshData;
import p0nki.glmc4.client.render.TextRenderer;
import p0nki.glmc4.client.render.WorldRenderContext;
import p0nki.glmc4.client.render.block.BlockRenderContext;
import p0nki.glmc4.client.render.block.BlockRenderer;
import p0nki.glmc4.client.render.block.BlockRenderers;
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
import p0nki.glmc4.utils.MathUtils;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
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
    private static final Map<Long, Chunk> chunks = new HashMap<>();
    private static final Map<Long, Mesh> meshes = new HashMap<>();
    private final static Set<Identifier> warnedIdentifiers = new HashSet<>();
    private final static Lock chunkLock = new ReentrantLock();
    private static ClientPacketListener packetListener;

    public static TextRenderer textRenderer;
    private static final Map<UUID, Long> lastReceivedEntityUpdate = new HashMap<>();

    private static Shader shader;
    private static Texture texture;
    public static DebugRenderer3D debugRenderer3D;
    private static Map<UUID, Entity> entities = new HashMap<>();

    public static long getLastUpdateTime(UUID uuid) {
        return lastReceivedEntityUpdate.get(uuid);
    }

    public static void loadInitialEntities(List<Entity> entities) {
        GLMC4Client.entities = entities.stream().collect(Collectors.toMap(Entity::getUuid, entity -> entity));
        entities.forEach(entity -> lastReceivedEntityUpdate.put(entity.getUuid(), System.currentTimeMillis()));
    }

    public static void updateEntity(UUID uuid, CompoundTag newData) {
        entities.get(uuid).fromTag(newData);
        lastReceivedEntityUpdate.put(uuid, System.currentTimeMillis());
    }

    public static void spawnEntity(Entity entity) {
        entities.put(entity.getUuid(), entity);
        lastReceivedEntityUpdate.put(entity.getUuid(), System.currentTimeMillis());
    }

    public static void despawnEntity(UUID uuid) {
        entities.remove(uuid);
        lastReceivedEntityUpdate.remove(uuid);
    }

    public static void onLoadChunk(int x, int z, Chunk chunk) {
        chunkLock.lock();
        chunks.put(MathUtils.pack(x, z), chunk);
        chunkLock.unlock();
    }

    private static MeshData mesh(Chunk chunk) {
        MeshData data = MeshData.chunk();
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 256; y++) {
                for (int z = 0; z < 16; z++) {
                    BlockState state = chunk.get(x, y, z);
                    if (state.getBlock() == Blocks.AIR) continue;
                    Identifier identifier = Blocks.REGISTRY.get(state.getBlock()).getKey();
                    if (BlockRenderers.REGISTRY.hasKey(identifier)) {
                        BlockRenderer renderer = BlockRenderers.REGISTRY.get(identifier).getValue();
                        BlockRenderContext context = new BlockRenderContext(chunk.getOrAir(x - 1, y, z), chunk.getOrAir(x + 1, y, z), chunk.getOrAir(x, y - 1, z), chunk.getOrAir(x, y + 1, z), chunk.getOrAir(x, y, z - 1), chunk.getOrAir(x, y, z + 1), state);
                        MeshData rendered = renderer.render(context);
                        rendered.mult4(0, new Matrix4f().translate(x, y, z));
                        data.append(rendered);
                    } else if (!warnedIdentifiers.contains(identifier)) {
                        LOGGER.warn(RENDER, "No renderer found for {}", state);
                        warnedIdentifiers.add(identifier);
                    }
                }
            }
        }
        return data;
    }

    private static void initializeClient() {
        shader = Shader.create("chunk");
        texture = new Texture(Path.of("run", "atlas", "block.png"));
        textRenderer = new TextRenderer();
        debugRenderer3D = new DebugRenderer3D();
        LOGGER.info(RENDER, "Client initialized");
        EntityRenderers.REGISTRY.getEntries().forEach(entry -> entry.getValue().initialize());
    }

    private static void tickClient(int frameCount) {
        if (packetListener.getPlayer() == null) return;
        if (!entities.containsKey(packetListener.getPlayer().getUuid())) return;
        float t = 0.5F;
        Matrix4f perspective = new Matrix4f().perspective((float) Math.toRadians(80), 1.0F, 0.001F, 300);
        float camHeight = 30;
        float camRadius = 15;
//        Entity thisEntity = entities.get(packetListener.getPlayer().getUuid());
        Matrix4f view = new Matrix4f().lookAt(
                new Vector3f((float) (camRadius * Math.cos(t)), camHeight, (float) (camRadius * Math.sin(t)))
                , new Vector3f(0, 0, 0), new Vector3f(0, 1, 0));
//        Matrix4f view = new Matrix4f().lookAt(thisEntity.getPosition(), new Vector3f(thisEntity.getPosition()).add(thisEntity.getLookingAt()), new Vector3f(0, 1, 0));
        WorldRenderContext context = new WorldRenderContext(perspective, view);
        shader.use();
        shader.setTexture("tex", texture, 0);
        shader.set(context);
        if (chunkLock.tryLock()) {
            for (Map.Entry<Long, Chunk> chunk : chunks.entrySet()) {
                meshes.put(chunk.getKey(), new Mesh(mesh(chunk.getValue())));
            }
            chunks.clear();
            chunkLock.unlock();
        } else {
            LOGGER.trace(RENDER, "Unable to obtain chunk lock. Will attempt next frame");
        }
        for (Map.Entry<Long, Mesh> chunk : meshes.entrySet()) {
            shader.setFloat("x", 16 * MathUtils.unpackFirst(chunk.getKey()));
            shader.setFloat("z", 16 * MathUtils.unpackSecond(chunk.getKey()));
            chunk.getValue().triangles();
        }

        for (Entity entity : entities.values()) {
            EntityType<?> type = entity.getType();
            Identifier identifier = EntityTypes.REGISTRY.get(type).getKey();
            EntityRenderer<?> renderer = EntityRenderers.REGISTRY.get(identifier).getValue();
            renderer.render(context, entity);
        }
//        debugRenderer3D.renderCube(context, new Vector3f(1, 0, 0), new Vector3f(0, 15, 0), new Vector3f(1, 1, 1));
        textRenderer.renderString(-1, 1 - 0.075F, 0.075F, String.format("GLMinecraft4\nFPS: %s", MCWindow.getFps()));

        packetListener.tick();
    }

    private static void runClient() {
        try {
            MCWindow.setInitializeCallback(GLMC4Client::initializeClient);
            MCWindow.setFrameCallback(GLMC4Client::tickClient);
            MCWindow.setEndCallback(GLMC4Client::endClient);
            MCWindow.start();
        } catch (Error | RuntimeException error) {
            error.printStackTrace();
            System.exit(0);
        }
    }

    private static void endClient() {
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
        new Thread(GLMC4Client::runNetty, "Netty Main Thread").start();
        runClient();
    }

}
