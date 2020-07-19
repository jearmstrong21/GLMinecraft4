package p0nki.glmc4.network.packet;

import p0nki.glmc4.network.PacketByteBuf;

public abstract class Packet<L extends PacketListener<L>> implements PacketByteBuf.Equivalent {

    private final PacketType<?> type;

    public Packet(PacketType<?> type) {
        this.type = type;
    }

    public PacketType<?> getType() {
        return type;
    }

    @Override
    public abstract void read(PacketByteBuf buf);

    @Override
    public abstract void write(PacketByteBuf buf);

    public abstract void apply(L listener);

}
