package p0nki.glmc4.network.packet;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public interface Packet<L extends PacketListener<L>> {

    void read(DataInput input) throws IOException;

    void write(DataOutput output) throws IOException;

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
