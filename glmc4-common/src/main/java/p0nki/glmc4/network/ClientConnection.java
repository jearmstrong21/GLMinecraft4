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
    private static int COUNTER = 0;
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
//        output = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
//        input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        output = new DataOutputStream(socket.getOutputStream());
        input = new DataInputStream(socket.getInputStream());
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
                int totalLength;
                byte[] totalPacket;
                try {
                    totalLength = input.readInt();
                    //TODO add totalLength size check so clients can't crash the server with out of memory error
                    totalPacket = new byte[totalLength];
                    input.readFully(totalPacket);
                } catch (IOException ioException) {
                    LOGGER.fatal(READLOOP, "Exception while reading packet", ioException);
                    break;
                }
                PacketReadBuf readBuf = new PacketReadBuf(totalPacket);
                int id = readBuf.readInt();
                Packet<?> packet = networkProtocol.createPacket(id);
                if (packet == null) {
                    LOGGER.warn(READLOOP, "Null packet created by protocol for ID {}", id);
                    continue;
                }
                if (packet.getType().matches(readType)) {
                    packet.read(readBuf);
                    Packet.apply(packet, packetListener);
                } else {
                    LOGGER.fatal(READLOOP, "Invalid packet type sent with ID {}", id);
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
        if (packet.getType().matches(writeType)) {
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
