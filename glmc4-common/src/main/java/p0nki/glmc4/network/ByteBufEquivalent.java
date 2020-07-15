package p0nki.glmc4.network;

public interface ByteBufEquivalent {

    void write(PacketWriteBuf output);

    void read(PacketReadBuf input);

}
