package p0nki.glmc4.network.packet.clientbound;

import p0nki.glmc4.network.PacketReadBuf;
import p0nki.glmc4.network.PacketWriteBuf;
import p0nki.glmc4.network.packet.Packet;
import p0nki.glmc4.network.packet.PacketDirection;
import p0nki.glmc4.network.packet.PacketTypes;
import p0nki.glmc4.server.ServerPlayer;
import p0nki.glmc4.tag.CompoundTag;

public class PacketS2CPlayerJoin extends Packet<ClientPacketListener> {

    private ServerPlayer player;

    public PacketS2CPlayerJoin() {
        super(PacketDirection.SERVER_TO_CLIENT, PacketTypes.S2C_PLAYER_JOIN);
    }

    public PacketS2CPlayerJoin(ServerPlayer player) {
        super(PacketDirection.SERVER_TO_CLIENT, PacketTypes.S2C_PLAYER_JOIN);
        this.player = player;
    }

    public ServerPlayer getPlayer() {
        return player;
    }

    @Override
    public void read(PacketReadBuf input) {
        player = new ServerPlayer().fromTag(CompoundTag.READER.read(input));
    }

    @Override
    public void write(PacketWriteBuf output) {
        player.toTag().write(output);
    }

    @Override
    public void apply(ClientPacketListener listener) {
        listener.onPlayerJoin(this);
    }
}
