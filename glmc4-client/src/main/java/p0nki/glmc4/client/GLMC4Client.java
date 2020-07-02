package p0nki.glmc4.client;

import p0nki.glmc4.client.assets.ResourceLocation;
import p0nki.glmc4.client.assets.TextureAssembler;
import p0nki.glmc4.network.ClientConnection;
import p0nki.glmc4.network.packet.NetworkProtocol;
import p0nki.glmc4.network.packet.PacketType;
import p0nki.glmc4.network.packet.clientbound.ClientPacketListener;
import p0nki.glmc4.utils.Identifier;

import java.io.IOException;
import java.net.Socket;

import static org.lwjgl.opengl.GL33.*;

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

    private static int vao;
    private static int vbo;
    private static int ebo;
    private static int shader;

    private static void initialize() {
        double[] vertexData = new double[]{
                -.5, -.5, 0,
                -.5, .5, 0,
                .5, -.5, 0,
                .5, .5, 0
        };
        int[] triData = new int[]{
                0, 1, 2,
                1, 2, 3
        };

//        String vertexCode = "#version 330 core\nlayout (location=0) in vec3 inPos;\nvoid main(){\n\tgl_Position=vec4(inPos,1.0);\n}";
//        String fragmentCode = "#version 330 core\nout vec4 fc;\nvoid main(){\n\tfc=vec4(0.5,0.2,0.3,1.0);\n}";
        String vertexCode = new ResourceLocation("shader/chunk.vert").loadText();
        String fragmentCode = new ResourceLocation("shader/chunk.frag").loadText();

        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        ebo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, triData, GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_DOUBLE, false, 3 * Double.BYTES, 0);
        glEnableVertexAttribArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glBindVertexArray(0);

        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader, vertexCode);
        glCompileShader(vertexShader);
        if (glGetShaderi(vertexShader, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Error compiling vertex shader\n" + glGetShaderInfoLog(vertexShader));
            throw new UnsupportedOperationException();
        }

        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader, fragmentCode);
        glCompileShader(fragmentShader);
        if (glGetShaderi(fragmentShader, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Error compiling fragment shader\n" + glGetShaderInfoLog(fragmentShader));
            throw new UnsupportedOperationException();
        }

        shader = glCreateProgram();
        glAttachShader(shader, vertexShader);
        glAttachShader(shader, fragmentShader);
        glLinkProgram(shader);
        if (glGetProgrami(shader, GL_LINK_STATUS) == GL_FALSE) {
            System.err.println("Error linking shader\n" + glGetProgramInfoLog(shader));
            throw new UnsupportedOperationException();
        }

    }

    private static void frame(int frameCount) {
        glUseProgram(shader);
        glBindVertexArray(vao);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
        glUseProgram(0);
    }

    private static void end() {

    }

    public static void main(String[] args) throws IOException {
//        runSocket();
        TextureAssembler.assemble(new Identifier("minecraft", "block"), "block");
        MCWindow.setInitializeCallback(GLMC4Client::initialize);
        MCWindow.setFrameCallback(GLMC4Client::frame);
        MCWindow.setEndCallback(GLMC4Client::end);
        MCWindow.start();
    }

}
