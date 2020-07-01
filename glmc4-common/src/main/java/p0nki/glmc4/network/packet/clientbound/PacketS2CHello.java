package p0nki.glmc4.network.packet.clientbound;

import p0nki.glmc4.player.ServerPlayer;
import p0nki.glmc4.tag.CompoundTag;
import p0nki.glmc4.tag.ListTag;
import p0nki.glmc4.utils.DataStreamUtils;
import p0nki.glmc4.utils.TagUtils;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.List;

public class PacketS2CHello extends PacketS2C {

    private ServerPlayer yourPlayer;
    private List<ServerPlayer> allPlayers;

    public PacketS2CHello() {

    }

    public PacketS2CHello(ServerPlayer yourPlayer, List<ServerPlayer> allPlayers) {
        this.yourPlayer = yourPlayer;
        this.allPlayers = allPlayers;
    }

    public ServerPlayer getYourPlayer() {
        return yourPlayer;
    }

    public List<ServerPlayer> getAllPlayers() {
        return allPlayers;
    }

    @Override
    public void read(DataInput input) throws IOException {
        yourPlayer = new ServerPlayer().fromCompoundTag(CompoundTag.READER.read(input));
        allPlayers = TagUtils.fromList(ListTag.READER.read(input), ServerPlayer::new);
    }

    @Override
    public void write(DataOutput output) throws IOException {
        yourPlayer.toCompoundTag().write(output);
        TagUtils.toList(allPlayers).write(output);
    }

    @Override
    public void apply(ClientPacketListener listener) {
        listener.onHello(this);
    }
}
