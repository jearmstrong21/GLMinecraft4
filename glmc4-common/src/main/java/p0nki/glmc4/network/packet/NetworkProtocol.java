package p0nki.glmc4.network.packet;

import p0nki.glmc4.network.packet.clientbound.*;
import p0nki.glmc4.network.packet.serverbound.PacketC2SPingResponse;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class NetworkProtocol {

    private final Map<Class<? extends Packet<?>>, Integer> classToId;
    private final Map<Integer, Supplier<? extends Packet<?>>> idToSupplier;

    public NetworkProtocol() {
        classToId = new HashMap<>();
        idToSupplier = new HashMap<>();

        // S2C
        register(PacketS2CChatMessage.class, PacketS2CChatMessage::new);
        register(PacketS2CHello.class, PacketS2CHello::new);
        register(PacketS2CPingRequest.class, PacketS2CPingRequest::new);
        register(PacketS2CPlayerJoin.class, PacketS2CPlayerJoin::new);
        register(PacketS2CPlayerLeave.class, PacketS2CPlayerLeave::new);
        register(PacketS2CChunkLoad.class, PacketS2CChunkLoad::new);

        //C2S
        register(PacketC2SPingResponse.class, PacketC2SPingResponse::new);

    }

    private <P extends Packet<?>> void register(Class<P> type, Supplier<P> factory) {
        int id = classToId.size() + 1;
        classToId.put(type, id);
        idToSupplier.put(id, factory);
    }

    @Nullable
    public Packet<?> createPacket(int id) {
        if (!idToSupplier.containsKey(id)) return null;
        return idToSupplier.get(id).get();
    }

    public int getId(Packet<?> packet) {
        if (!classToId.containsKey(packet.getClass()))
            throw new UnsupportedOperationException("Cannot write unregistered packet " + packet.getClass());
        return classToId.get(packet.getClass());
    }

}
