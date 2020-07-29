package p0nki.glmc4.network.packet;

import p0nki.glmc4.server.ServerPlayer;

public abstract class PacketListener<L extends PacketListener<L>> {

    private NetworkConnection<L> connection = null;
    private ServerPlayer player;

    public abstract void onConnected();

    public abstract void tick();

    public abstract void onDisconnected(String reason);

    public final NetworkConnection<L> getConnection() {
        return connection;
    }

    public void setConnection(NetworkConnection<L> connection) {
        this.connection = connection;
    }

    public ServerPlayer getPlayer() {
        return player;
    }

    public void setPlayer(ServerPlayer player) {
        this.player = player;
    }
}
