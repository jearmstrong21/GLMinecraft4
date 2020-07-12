package p0nki.glmc4.client;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import p0nki.glmc4.client.assets.AtlasPosition;
import p0nki.glmc4.client.assets.LocalLocation;
import p0nki.glmc4.client.assets.TextureAssembler;
import p0nki.glmc4.client.gl.Mesh;
import p0nki.glmc4.client.gl.MeshData;
import p0nki.glmc4.client.gl.Shader;
import p0nki.glmc4.client.gl.Texture;
import p0nki.glmc4.network.ClientConnection;
import p0nki.glmc4.network.packet.NetworkProtocol;
import p0nki.glmc4.network.packet.PacketType;
import p0nki.glmc4.network.packet.clientbound.ClientPacketListener;
import p0nki.glmc4.utils.Identifier;

import java.io.IOException;
import java.net.Socket;

import static org.lwjgl.opengl.GL21.*;

public class GLMC4Client {

    private static void runSocket() {
        Socket socket;
        try {
            socket = new Socket("localhost", 3333);
        } catch (IOException ioException) {
            System.out.println("Connection refused");
            return;
        }
        System.out.println("Socket connected");
        NetworkProtocol networkProtocol = new NetworkProtocol();
        ClientConnection<ClientPacketListener> connection;
        try {
            connection = new ClientConnection<>(socket, networkProtocol, PacketType.CLIENTBOUND, PacketType.SERVERBOUND);
        } catch (IOException ioException) {
            System.out.println("Error created connection object");
            return;
        }
        ClientPacketListener packetListener = new ClientPacketListener(connection);
        connection.setPacketListener(packetListener);
        connection.startLoop();
        System.out.println("Listening on localhost:3333");
    }

    private static Mesh mesh;
    private static Shader shader;
    private static Texture texture;
    private static int tri;

    private static void quad(Vector3f a, Vector3f b) {
        glColor3f(1, 0, 0);
        glVertex3f(0, 0, 0);

        glColor3f(0, 1, 0);
        glVertex3f(a.x, a.y, a.z);

        glColor3f(0, 0, 1);
        glVertex3f(b.x, b.y, b.z);

        glColor3f(0, 1, 0);
        glVertex3f(a.x, a.y, a.z);

        glColor3f(1, 1, 1);
        glVertex3f(a.x + b.x, a.y + b.y, a.z + b.z);
    }

    private static void xmiQuad() {
        quad(new Vector3f(0, 1, 0), new Vector3f(0, 0, 1));
    }

    private static void xplQuad() {
        glPushMatrix();
        glTranslatef(1, 0, 0);
        quad(new Vector3f(0, 1, 0), new Vector3f(0, 0, 1));
        glPopMatrix();
    }

    private static void ymiQuad() {
        quad(new Vector3f(1, 0, 0), new Vector3f(0, 0, 1));
    }

    private static void yplQuad() {
        glPushMatrix();
        glTranslatef(0, 1, 0);
        quad(new Vector3f(1, 0, 0), new Vector3f(0, 0, 1));
        glPopMatrix();
    }

    private static void zmiQuad() {
        quad(new Vector3f(1, 0, 0), new Vector3f(0, 1, 0));
    }

    private static void zplQuad() {
        glPushMatrix();
        glTranslatef(0, 0, 1);
        quad(new Vector3f(1, 0, 0), new Vector3f(0, 1, 0));
        glPopMatrix();
    }

    private static void initialize() {
        TextureAssembler BLOCK = TextureAssembler.get(new Identifier("minecraft:block"), "block");
        boolean[][][] terrain = new boolean[16][256][16];
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int h = (x + z) / 4;
                for (int y = 0; y < 256; y++) {
                    terrain[x][y][z] = y < h;
                }
            }
        }
        final boolean d = true;
//        tri = glGenLists(1);
//        glNewList(tri, GL_COMPILE);
//        glBegin(GL_TRIANGLES);
//
//        for (int x = 0; x < 16; x++) {
//            for (int y = 0; y < 256; y++) {
//                for (int z = 0; z < 16; z++) {
//                    if (!terrain[x][y][z]) continue;
//                    boolean xmi = d;
//                    boolean xpl = d;
//                    boolean ymi = d;
//                    boolean ypl = d;
//                    boolean zmi = d;
//                    boolean zpl = d;
//                    if (x > 0) xmi = !terrain[x - 1][y][z];
//                    if (x < 15) xpl = !terrain[x + 1][y][z];
//                    if (y > 0) ymi = !terrain[x][y - 1][z];
//                    if (y < 15) ypl = !terrain[x][y + 1][z];
//                    if (z > 0) zmi = !terrain[x][y][z - 1];
//                    if (z < 15) zpl = !terrain[x][y][z + 1];
//                    glTranslatef(x, y, z);
//                    if (xmi) xmiQuad();
//                    if (xpl) xplQuad();
//                    if (ymi) ymiQuad();
//                    if (ypl) yplQuad();
//                    if (zmi) zmiQuad();
//                    if (zpl) zplQuad();
//                    glTranslatef(-x, -y, -z);
//                }
//            }
//        }
//
//        glEnd();
//        glEndList();
        shader = new Shader("chunk");
        MeshData data = new MeshData();

        data.addBuffer(3);
        data.addBuffer(2);

        AtlasPosition SIDE = BLOCK.getTexture(new Identifier("minecraft:grass_side"));
        AtlasPosition TOP = BLOCK.getTexture(new Identifier("minecraft:grass_top"));
        AtlasPosition BOTTOM = BLOCK.getTexture(new Identifier("minecraft:dirt"));
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 256; y++) {
                for (int z = 0; z < 16; z++) {
                    if (!terrain[x][y][z]) continue;
                    boolean xmi = d;
                    boolean xpl = d;
                    boolean ymi = d;
                    boolean ypl = d;
                    boolean zmi = d;
                    boolean zpl = d;
                    if (x > 0) xmi = !terrain[x - 1][y][z];
                    if (x < 15) xpl = !terrain[x + 1][y][z];
                    if (y > 0) ymi = !terrain[x][y - 1][z];
                    if (y < 15) ypl = !terrain[x][y + 1][z];
                    if (z > 0) zmi = !terrain[x][y][z - 1];
                    if (z < 15) zpl = !terrain[x][y][z + 1];
                    Vector3f o = new Vector3f(x, y, z);
                    if (xmi) data.addXmiQuad(0, 1, o, SIDE);
                    if (xpl) data.addXplQuad(0, 1, o, SIDE);
                    if (ymi) data.addYmiQuad(0, 1, o, BOTTOM);
                    if (ypl) data.addYplQuad(0, 1, o, TOP);
                    if (zmi) data.addZmiQuad(0, 1, o, SIDE);
                    if (zpl) data.addZplQuad(0, 1, o, SIDE);
                }
            }
        }

        mesh = new Mesh(data);
        texture = new Texture(new LocalLocation("atlas/block.png"));
    }

    private static void frame(int frameCount) {
//        glBegin(GL_TRIANGLES);
//        glColor3f(1, 0, 0);
//        glVertex2f(0, 0);
//        glColor3f(0, 1, 0);
//        glVertex2f(0, 1);
//        glColor3f(0, 0, 1);
//        glVertex2f(1, 0);
//        glEnd();
        shader.use();
        shader.setTexture("tex", texture, 0);
        shader.setMat4f("perspective", new Matrix4f().perspective((float) Math.toRadians(80), 1.0F, 0.001F, 100));
        float t = MCWindow.time();
        shader.setMat4f("view", new Matrix4f().lookAt(
                new Vector3f((float) (8.0f + 25.0F * Math.cos(t)), 10, (float) (8.0F - 25.0F * Math.cos(t - 4)))
                , new Vector3f(8, 0, 8), new Vector3f(0, 1, 0)));
        for (int x = -2; x < 3; x++) {
            for (int z = -2; z < 3; z++) {
                shader.setFloat("x", x * 16);
                shader.setFloat("z", z * 16);
                mesh.render();
            }
        }
//        glPushMatrix();
//        glCallList(tri);
//        glPopMatrix();
        System.out.println(MCWindow.getFps());
    }

    private static void end() {

    }

    public static void main(String[] args) {
//        runSocket();
        MCWindow.setInitializeCallback(GLMC4Client::initialize);
        MCWindow.setFrameCallback(GLMC4Client::frame);
        MCWindow.setEndCallback(GLMC4Client::end);
        MCWindow.start();
    }

}
