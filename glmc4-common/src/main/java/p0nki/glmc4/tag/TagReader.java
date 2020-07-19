package p0nki.glmc4.tag;

import p0nki.glmc4.network.PacketByteBuf;

public interface TagReader<T extends Tag> {

    T read(PacketByteBuf buf);

}
