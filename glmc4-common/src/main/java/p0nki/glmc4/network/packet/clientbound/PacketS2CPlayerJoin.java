package p0nki.glmc4.network.packet.clientbound;

import p0nki.glmc4.server.ServerPlayer;
import p0nki.glmc4.tag.CompoundTag;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class PacketS2CPlayerJoin extends PacketS2C {

    private ServerPlayer player;

    public PacketS2CPlayerJoin() {

    }

    public PacketS2CPlayerJoin(ServerPlayer player) {
        this.player = player;
    }

    public ServerPlayer getPlayer() {
        return player;
    }

    @Override
    public void read(DataInput input) throws IOException {
        player = new ServerPlayer().fromTag(CompoundTag.READER.read(input));
    }

    @Override
    public void write(DataOutput output) throws IOException {
        player.toTag().write(output);
    }

    @Override
    public void apply(ClientPacketListener listener) {
        listener.onPlayerJoin(this);
    }
}
