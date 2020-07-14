package p0nki.glmc4.network.packet;

import p0nki.glmc4.network.PacketReadBuf;
import p0nki.glmc4.network.PacketWriteBuf;

public interface Packet<L extends PacketListener<L>> {

    void read(PacketReadBuf input);

    void write(PacketWriteBuf output);

    void apply(L listener);

    PacketType getType();

    default boolean isWriteErrorSkippable() {
        return false;
    }

    @SuppressWarnings("unchecked")
    static <L extends PacketListener<L>> void apply(Packet<?> packet, L listener) {
        ((Packet<L>) packet).apply(listener);
    }

}
