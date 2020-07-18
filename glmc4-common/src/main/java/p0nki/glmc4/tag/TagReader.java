package p0nki.glmc4.tag;

import p0nki.glmc4.network.PacketReadBuf;

public interface TagReader<T extends Tag> {

    T read(PacketReadBuf input);

}
