package p0nki.glmc4.network;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import p0nki.glmc4.network.packet.NetworkProtocol;
import p0nki.glmc4.network.packet.Packet;
import p0nki.glmc4.network.packet.PacketListener;
import p0nki.glmc4.network.packet.PacketType;
import p0nki.glmc4.network.packet.clientbound.ClientPacketListener;
import p0nki.glmc4.server.MinecraftServer;
import p0nki.glmc4.server.ServerPlayer;

import java.io.*;
import java.net.Socket;

public class ClientConnection<L extends PacketListener<L>> {

    private final static String DEBUG_STR = "Aa0Aa1Aa2Aa3Aa4Aa5Aa6Aa7Aa8Aa9Ab0Ab1Ab2Ab3Ab4Ab5Ab6Ab7Ab8Ab9Ac0Ac1Ac2Ac3Ac4Ac5Ac6Ac7Ac8Ac9Ad0Ad1Ad2A";
    private final static Logger LOGGER = LogManager.getLogger();
    private final static Marker READLOOP = MarkerManager.getMarker("READLOOP");
    private final static Marker WRITE = MarkerManager.getMarker("WRITE");

    private final Socket socket;
    private final DataOutput output;
    private final DataInput input;
    private final NetworkProtocol networkProtocol;
    private L packetListener;
    private boolean isLoopRunning = false;
    private final PacketType readType;
    private final PacketType writeType;
    private ServerPlayer player = null;

    public ClientConnection(Socket socket, NetworkProtocol networkProtocol, PacketType readType, PacketType writeType) throws IOException {
        this.socket = socket;
//        outputStream = new DataOutputStream(new GZIPOutputStream(socket.getOutputStream()));
//        inputStream = new DataInputStream(new GZIPInputStream(socket.getInputStream()));
        output = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        // TODO: make use gzip io streams, instead of new DOS(new GZIPOS(socket.getOS())), use new DOS(new BR(socket.getOS()))
        //  DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(new GZIPInputStream(stream)));
        this.networkProtocol = networkProtocol;
        this.readType = readType;
        this.writeType = writeType;
    }

    public L getPacketListener() {
        return packetListener;
    }

    public void setPlayer(ServerPlayer player) {
        this.player = player;
    }

    public ServerPlayer getPlayer() {
        return player;
    }

    public void setPacketListener(L packetListener) {
        this.packetListener = packetListener;
    }

    private Thread threadLoop = null;

    public void startLoop() {
        if (isLoopRunning)
            throw new UnsupportedOperationException("Cannot start ClientConnection that is already started");
        threadLoop = new Thread(() -> {
            packetListener.onConnected();
            String dcReason = null;
            while (true) {
                if (socket.isClosed()) {
                    LOGGER.fatal(READLOOP, "Socket closed");
                    dcReason = "Socket closed";
                    break;
                }
                int id;
                try {
                    id = input.readInt();
                } catch (IOException e) {
                    LOGGER.fatal(READLOOP, "Error while reading packet ID", e);
                    dcReason = "Error while reading packet ID";
                    break;
                }
                Packet<?> packet = networkProtocol.createPacket(id);
                if (packet == null) continue;
                if (packet.getType().matches(readType)) {
                    try {
                        packet.read(input);
                        byte[] b = new byte[DEBUG_STR.length()];
                        input.readFully(b);
                        if (!new String(b).equals(DEBUG_STR)) {
                            dcReason = "Invalid DEBUG_STR";
                            LOGGER.fatal(READLOOP, "Expected <{}>", DEBUG_STR);
                            LOGGER.fatal(READLOOP, "Received <{}>", new String(b));
                            break;
                        }
                    } catch (IOException e) {
                        dcReason = "Error while reading packet data";
                        LOGGER.fatal(READLOOP, "Error while reading packet data", e);
                        break;
                    }
                    Packet.apply(packet, packetListener);
                } else {
                    break;
                }
            }
            if (MinecraftServer.INSTANCE != null)
                MinecraftServer.INSTANCE.removeConnection(player.getId());
            if (packetListener instanceof ClientPacketListener) packetListener.onDisconnected(String.valueOf(dcReason));
            isLoopRunning = false;
        });
        isLoopRunning = true;
        threadLoop.start();
    }

    private void disconnect() {
        isLoopRunning = false;
        if (packetListener instanceof ClientPacketListener) packetListener.onDisconnected("Socket closed");
        threadLoop.interrupt();
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException ioException) {
            System.out.println("Error closing socket: " + ioException.getMessage());
        }
    }

    public void write(Packet<?> packet) { // TODO make this a packet queue? speed gains?
        if (!isLoopRunning || socket.isClosed()) return;
        if (packet.getType().matches(writeType)) {
            try {
                output.writeInt(networkProtocol.getId(packet));
            } catch (IOException e) {
                disconnect();
                LOGGER.fatal(WRITE, "Error writing packet ID", e);
                e.printStackTrace();
                return;
            }
            try {
                packet.write(output);
                output.writeBytes(DEBUG_STR);
            } catch (IOException e) {
                if (packet.isWriteErrorSkippable()) return;
                LOGGER.fatal(WRITE, "Error writing packet data", e);
                disconnect();
            }
        } else {
            disconnect();
        }
    }

}
