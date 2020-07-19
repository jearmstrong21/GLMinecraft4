package p0nki.glmc4.network.packet.clientbound;

import p0nki.glmc4.network.PacketByteBuf;
import p0nki.glmc4.network.packet.Packet;
import p0nki.glmc4.network.packet.PacketTypes;
import p0nki.glmc4.server.ServerPlayer;

public class PacketS2CPlayerJoin extends Packet<ClientPacketListener> {

    private ServerPlayer player;

    public PacketS2CPlayerJoin() {
        super(PacketTypes.S2C_PLAYER_JOIN);
    }

    public PacketS2CPlayerJoin(ServerPlayer player) {
        super(PacketTypes.S2C_PLAYER_JOIN);
        this.player = player;
    }

    public ServerPlayer getPlayer() {
        return player;
    }

    @Override
    public void read(PacketByteBuf buf) {
        player = new ServerPlayer().fromTag(buf.readCompoundTag());
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeTag(player.toTag());
    }

    @Override
    public void apply(ClientPacketListener listener) {
        listener.onPlayerJoin(this);
    }
}
