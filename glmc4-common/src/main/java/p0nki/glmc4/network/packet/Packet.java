package p0nki.glmc4.network.packet;

import java.io.*;

public interface Packet<L extends PacketListener<L>> {

    void read(DataInput input) throws IOException;

    void write(DataOutput output) throws IOException;

    void apply(L listener);

    PacketType getType();

    default boolean isWriteErrorSkippable() {
        return false;
    }

}
