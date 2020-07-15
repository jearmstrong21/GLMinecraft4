package p0nki.glmc4.network.packet.clientbound;

import p0nki.glmc4.block.Blocks;
import p0nki.glmc4.entity.EntityTypes;
import p0nki.glmc4.network.PacketReadBuf;
import p0nki.glmc4.network.PacketWriteBuf;
import p0nki.glmc4.server.ServerPlayer;
import p0nki.glmc4.tag.CompoundTag;
import p0nki.glmc4.tag.ListTag;
import p0nki.glmc4.utils.TagUtils;

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
    public void read(PacketReadBuf input) {
        yourPlayer = new ServerPlayer().fromTag(CompoundTag.READER.read(input));
        allPlayers = TagUtils.fromList(ListTag.READER.read(input), ServerPlayer::new);
        Blocks.REGISTRY.verify(input);
        EntityTypes.REGISTRY.verify(input);
    }

    @Override
    public void write(PacketWriteBuf output) {
        yourPlayer.toTag().write(output);
        TagUtils.toList(allPlayers).write(output);
        Blocks.REGISTRY.write(output);
        EntityTypes.REGISTRY.write(output);
    }

    @Override
    public void apply(ClientPacketListener listener) {
        listener.onHello(this);
    }
}
