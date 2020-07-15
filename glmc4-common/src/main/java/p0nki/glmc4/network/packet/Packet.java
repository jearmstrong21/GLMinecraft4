package p0nki.glmc4.network.packet;

import p0nki.glmc4.network.PacketReadBuf;
import p0nki.glmc4.network.PacketWriteBuf;

public abstract class Packet<L extends PacketListener<L>> {

    private final PacketDirection direction;
    private final PacketType<?> type;

    public Packet(PacketDirection direction, PacketType<?> type) {
        this.direction = direction;
        this.type = type;
    }

    @SuppressWarnings("unchecked")
    public static <L extends PacketListener<L>> void apply(Packet<?> packet, L listener) {
        ((Packet<L>) packet).apply(listener);
    }

    public PacketType<?> getType() {
        return type;
    }

    public abstract void read(PacketReadBuf input);

    public abstract void write(PacketWriteBuf output);

    public abstract void apply(L listener);

    public PacketDirection getDirection() {
        return direction;
    }

}
