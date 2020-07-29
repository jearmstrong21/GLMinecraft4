package p0nki.glmc4.network.packet;

import p0nki.glmc4.registry.Registrable;
import p0nki.glmc4.registry.Registry;

import java.util.function.Supplier;

public class PacketType<P extends Packet<?>> extends Registrable<PacketType<?>> {

    private final Supplier<P> supplier;

    public PacketType(Supplier<P> supplier) {
        this.supplier = supplier;
    }

    public P create() {
        return supplier.get();
    }

    @Override
    public Registry<PacketType<?>> getRegistry() {
        return PacketTypes.REGISTRY;
    }

    @Override
    public PacketType<?> getValue() {
        return this;
    }
}
