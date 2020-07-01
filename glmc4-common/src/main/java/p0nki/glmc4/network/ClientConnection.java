package p0nki.glmc4.network;

import p0nki.glmc4.network.packet.Packet;
import p0nki.glmc4.network.packet.PacketHandler;
import p0nki.glmc4.network.packet.PacketListener;
import p0nki.glmc4.network.packet.PacketType;
import p0nki.glmc4.player.ServerPlayer;
import p0nki.glmc4.server.MinecraftServer;

import java.io.*;
import java.net.Socket;

public class ClientConnection<L extends PacketListener<L>> {

    private final Socket socket;
    private final DataOutput output;
    private final DataInput input;
    private final PacketHandler packetHandler;
    private L packetListener;
    private boolean isLoopRunning = false;
    private final PacketType readType;
    private final PacketType writeType;
    private ServerPlayer player = null;

    public ClientConnection(Socket socket, PacketHandler packetHandler, PacketType readType, PacketType writeType) throws IOException {
        this.socket = socket;
//        outputStream = new DataOutputStream(new GZIPOutputStream(socket.getOutputStream()));
//        inputStream = new DataInputStream(new GZIPInputStream(socket.getInputStream()));
        output = new DataOutputStream(socket.getOutputStream());
        input = new DataInputStream(socket.getInputStream());
        // TODO: make use gzip io streams, instead of new DOS(new GZIPOS(socket.getOS())), use new DOS(new BR(socket.getOS()))
        //  DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(new GZIPInputStream(stream)));
        this.packetHandler = packetHandler;
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
        boolean[] connected = new boolean[]{false}; // TODO check to make sure there are no `connected` race conditions, i.e. onConnected() always fires before first packet read
        threadLoop = new Thread(() -> {
            while (true) {
                if (!connected[0]) continue;
                if (socket.isClosed()) {
                    packetListener.onDisconnected("Socket closed");
                    break;
                } // TODO put into private git
                int id;
                try {
                    id = input.readInt();
                } catch (IOException e) {
                    packetListener.onDisconnected("IOException: " + e.getClass() + ", " + e.getMessage());
                    break;
                }
                Packet<? extends PacketListener<?>> packet = packetHandler.createPacket(id);
                if (packet == null) continue;
                if (packet.getType().matches(readType)) {
                    try {
                        packet.read(input);
                    } catch (IOException e) {
                        packetListener.onDisconnected("IOException: " + e.getClass() + ", " + e.getMessage());
                        break;
                    }
                    ((Packet<L>) packet).apply(packetListener);
                } else {
                    packetListener.onDisconnected("Expected to read" + readType + " packet, instead got " + packet.getType() + " packet");
                    break;
                }
            }
            if (MinecraftServer.INSTANCE != null)
                MinecraftServer.INSTANCE.removeConnection(player.getId());
            isLoopRunning = false;
        });
        isLoopRunning = true;
        threadLoop.start();
        packetListener.onConnected();
        connected[0] = true;
    }

    private void disconnect(String reason) {
        isLoopRunning = false;
        threadLoop.interrupt();
        packetListener.onDisconnected(reason);
    }

    public void write(Packet<?> packet) { // TODO make this a packet queue? speed gains?
        if (!isLoopRunning || socket.isClosed()) {
            MinecraftServer.INSTANCE.removeConnection(player.getId());
            return;
        }
//        if (!isLoopRunning)
//            throw new UnsupportedOperationException("Cannot write ClientConnection that is already ended");
        if (packet.getType().matches(writeType)) {
            try {
                output.writeInt(packetHandler.getId(packet));
            } catch (IOException e) {
                disconnect("IOException: " + e.getClass() + ", " + e.getMessage());
                return;
            }
            try {
                packet.write(output);
            } catch (IOException e) {
                if (packet.isWriteErrorSkippable()) return;
                disconnect("IOException: " + e.getClass() + ", " + e.getMessage());
            }
        } else {
            disconnect("Expected to write " + writeType + " packet, instead got " + packet.getType() + " packet");
        }
    }

}
