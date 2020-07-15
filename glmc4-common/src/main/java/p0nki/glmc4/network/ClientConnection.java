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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ClientConnection<L extends PacketListener<L>> {

    public final static int MAX_PACKET_SIZE = 600000;

    private final static Logger LOGGER = LogManager.getLogger();
    private final static Marker READ = MarkerManager.getMarker("READ");
    private final static Marker WRITE = MarkerManager.getMarker("WRITE");
    private static int CONNECTION_COUNTER = 0;
    private final Socket socket;
    private final NetworkProtocol networkProtocol;
    private final PacketType readType;
    private final PacketType writeType;
    private L packetListener;
    private boolean isLoopRunning = false;
    private ServerPlayer player = null;
    private final List<Packet<?>> packetsToWrite = new CopyOnWriteArrayList<>();
    private Thread readLoop = null;
    private final DataInputStream inputStream;
    private final DataOutputStream outputStream;

    public ClientConnection(Socket socket, NetworkProtocol networkProtocol, PacketType readType, PacketType writeType) throws IOException {
        this.socket = socket;
        inputStream = new DataInputStream(socket.getInputStream());
        outputStream = new DataOutputStream(socket.getOutputStream());
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

    private Thread writeLoop = null;

    public void startLoop() {
        if (isLoopRunning)
            throw new UnsupportedOperationException("Cannot start ClientConnection that is already started");
        readLoop = new Thread(() -> {
            packetListener.onConnected();
            String dcReason = null;
            while (isLoopRunning && !socket.isClosed()) {
                int totalLength;
                byte[] totalPacket;
                try {
                    totalLength = inputStream.readInt();
                    if (totalLength > MAX_PACKET_SIZE) {
                        LOGGER.fatal(READ, "Packet of size {} > {} received", totalLength, MAX_PACKET_SIZE);
                        dcReason = "Max packet size exceeded";
                        break;
                    }
                    //TODO add totalLength size check so clients can't crash the server with out of memory error
                    totalPacket = new byte[totalLength];
                    int readCount = 0;
                    while (readCount < totalLength) {
                        readCount += inputStream.read(totalPacket, readCount, totalLength - readCount);
                    }
                } catch (IOException ioException) {
                    LOGGER.fatal(READ, "Exception while reading packet", ioException);
                    dcReason = "Error while reading packet data";
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
                    dcReason = "Invalid packet type";
                    break;
                }
            }
            if (MinecraftServer.INSTANCE != null)
                MinecraftServer.INSTANCE.removeConnection(player.getId());
            if (packetListener instanceof ClientPacketListener) packetListener.onDisconnected(String.valueOf(dcReason));
            isLoopRunning = false;
        }, "Connection-Read-" + CONNECTION_COUNTER);
        writeLoop = new Thread(() -> {
            while (isLoopRunning && !socket.isClosed()) {
                if (packetsToWrite.size() > 0) {
                    Packet<?> packet = packetsToWrite.remove(0);
                    PacketWriteBuf writeBuf = new PacketWriteBuf();
                    int id = networkProtocol.getId(packet);
                    writeBuf.writeInt(id);
                    packet.write(writeBuf);
                    int totalLength = writeBuf.size();
                    try {
                        outputStream.writeInt(totalLength);
                        outputStream.write(writeBuf.array());
                    } catch (IOException ioException) {
                        LOGGER.fatal(WRITE, "Error writing packet data", ioException);
                        disconnect();
                    }
                }
            }
        }, "Connection-Write-" + CONNECTION_COUNTER);
        CONNECTION_COUNTER++;
        isLoopRunning = true;
        readLoop.start();
        writeLoop.start();
    }

    private void disconnect() {
        isLoopRunning = false;
        if (packetListener instanceof ClientPacketListener) packetListener.onDisconnected("Socket closed");
        readLoop.interrupt();
        writeLoop.interrupt();
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
            packetsToWrite.add(packet);
        } else {
            disconnect();
        }
    }

}
