package p0nki.glmc4.network.packet;

public abstract class PacketListener<L extends PacketListener<L>> {

    private NetworkConnection<L> connection = null;

    public abstract void onConnected();

    public abstract void tick();

    public abstract void onDisconnected(String reason);

    public final NetworkConnection<L> getConnection() {
        return connection;
    }

    public void setConnection(NetworkConnection<L> connection) {
        this.connection = connection;
    }

}
