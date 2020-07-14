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

    //    private final static String DEBUG_STR = "Aa0Aa1Aa2Aa3Aa4Aa5Aa6Aa7Aa8Aa9Ab0Ab1Ab2Ab3Ab4Ab5Ab6Ab7Ab8Ab9Ac0Ac1Ac2Ac3Ac4Ac5Ac6Ac7Ac8Ac9Ad0Ad1Ad2A";
    private final static Logger LOGGER = LogManager.getLogger();
    private final static Marker READ = MarkerManager.getMarker("READ");
    private final static Marker WRITE = MarkerManager.getMarker("WRITE");
    private static int COUNTER = 0;
    private final Socket socket;
    private final DataOutput output;
    private final DataInput input;
    private final NetworkProtocol networkProtocol;
    private final PacketType readType;
    private final PacketType writeType;
    private L packetListener;
    private boolean isLoopRunning = false;
    private ServerPlayer player = null;
    private Thread threadLoop = null;

    public ClientConnection(Socket socket, NetworkProtocol networkProtocol, PacketType readType, PacketType writeType) throws IOException {
        this.socket = socket;
        output = new DataOutputStream(socket.getOutputStream());
        input = new DataInputStream(socket.getInputStream());
        this.networkProtocol = networkProtocol;
        this.readType = readType;
        this.writeType = writeType;
    }

    public L getPacketListener() {
        return packetListener;
    }

    public void setPacketListener(L packetListener) {
        this.packetListener = packetListener;
    }

    public ServerPlayer getPlayer() {
        return player;
    }

    public void setPlayer(ServerPlayer player) {
        this.player = player;
    }

    public void startLoop() {
        if (isLoopRunning)
            throw new UnsupportedOperationException("Cannot start ClientConnection that is already started");
        threadLoop = new Thread(() -> {
            packetListener.onConnected();
            String dcReason = null;
            while (true) {
                if (socket.isClosed()) {
                    LOGGER.fatal(READ, "Socket closed");
                    dcReason = "Socket closed";
                    break;
                }
                int totalLength;
                byte[] totalPacket;
                try {
                    totalLength = input.readInt();
                    //TODO add totalLength size check so clients can't crash the server with out of memory error
                    totalPacket = new byte[totalLength];
                    input.readFully(totalPacket);
                } catch (IOException ioException) {
                    LOGGER.fatal(READ, "Exception while reading packet", ioException);
                    break;
                }
                PacketReadBuf readBuf = new PacketReadBuf(totalPacket);
                int id = readBuf.readInt();
                Packet<?> packet = networkProtocol.createPacket(id);
                if (packet == null) {
                    LOGGER.warn(READ, "Null packet created by protocol for ID {}", id);
                    continue;
                }
                if (packet.getType() == readType) {
                    packet.read(readBuf);
                    Packet.apply(packet, packetListener);
                } else {
                    LOGGER.fatal(READ, "Invalid packet type sent with ID {}", id);
                    break;
                }
            }
            if (MinecraftServer.INSTANCE != null)
                MinecraftServer.INSTANCE.removeConnection(player.getId());
            if (packetListener instanceof ClientPacketListener) packetListener.onDisconnected(String.valueOf(dcReason));
            isLoopRunning = false;
        }, "Connection-" + (COUNTER++));
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
            LOGGER.fatal("Error closing socket. This can probably be ignored.", ioException);
        }
    }

    public void write(Packet<?> packet) { // TODO make this a packet queue? speed gains?
        if (!isLoopRunning || socket.isClosed()) return;
        if (packet.getType() == writeType) {
            PacketWriteBuf writeBuf = new PacketWriteBuf();
            int id = networkProtocol.getId(packet);
            writeBuf.writeInt(id);
            packet.write(writeBuf);
            int totalLength = writeBuf.size();
            try {
                output.writeInt(totalLength);
                output.write(writeBuf.array());
            } catch (IOException ioException) {
                LOGGER.fatal(WRITE, "Error writing packet data", ioException);
                disconnect();
            }
        } else {
            disconnect();
        }
    }

}
