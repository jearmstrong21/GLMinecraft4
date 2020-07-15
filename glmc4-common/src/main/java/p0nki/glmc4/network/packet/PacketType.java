package p0nki.glmc4.network.packet;

import java.util.function.Supplier;

public class PacketType<P extends Packet<?>> {

    private final Supplier<P> supplier;

    public PacketType(Supplier<P> supplier) {
        this.supplier = supplier;
    }

    public P create() {
        return supplier.get();
    }

}
