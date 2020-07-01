package p0nki.glmc4.network.packet;

public enum PacketType {

    SERVERBOUND,
    CLIENTBOUND,
    EITHER;

    public boolean matches(PacketType other) {
        if (this == EITHER) return true;
        return this == other;
    }

}
