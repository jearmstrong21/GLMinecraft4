package p0nki.glmc4.network.packet;

import p0nki.glmc4.network.ClientConnection;

public interface PacketListener<L extends PacketListener<L>> {

    void onConnected();

    void onDisconnected(String reason);

    ClientConnection<L> getConnection();

    // TODO ClientConnection getConnection() or something

}
