package p0nki.glmc4.network.packet.clientbound;

import p0nki.glmc4.block.Blocks;
import p0nki.glmc4.entity.Entity;
import p0nki.glmc4.entity.EntityType;
import p0nki.glmc4.entity.EntityTypes;
import p0nki.glmc4.network.PacketByteBuf;
import p0nki.glmc4.network.packet.Packet;
import p0nki.glmc4.network.packet.PacketTypes;
import p0nki.glmc4.server.ServerPlayer;
import p0nki.glmc4.tag.CompoundTag;
import p0nki.glmc4.tag.ListTag;
import p0nki.glmc4.tag.Tag;
import p0nki.glmc4.utils.TagUtils;

import java.util.ArrayList;
import java.util.List;

public class PacketS2CHello extends Packet<ClientPacketListener> {

    private ServerPlayer yourPlayer;
    private List<ServerPlayer> allPlayers;
    private List<Entity> allEntities;

    public PacketS2CHello() {
        super(PacketTypes.S2C_HELLO);
    }

    public PacketS2CHello(ServerPlayer yourPlayer, List<ServerPlayer> allPlayers, List<Entity> allEntities) {
        super(PacketTypes.S2C_HELLO);
        this.yourPlayer = yourPlayer;
        this.allPlayers = allPlayers;
        this.allEntities = allEntities;
    }

    public ServerPlayer getYourPlayer() {
        return yourPlayer;
    }

    public List<ServerPlayer> getAllPlayers() {
        return allPlayers;
    }

    public List<Entity> getAllEntities() {
        return allEntities;
    }

    @Override
    public void read(PacketByteBuf buf) {
        PacketTypes.REGISTRY.verify(buf);

        yourPlayer = new ServerPlayer().fromTag(buf.readCompoundTag());

        allPlayers = TagUtils.fromList(buf.readListTag(), ServerPlayer::new);

        ListTag entitiesTag = buf.readListTag();
        allEntities = new ArrayList<>();
        for (Tag tag : entitiesTag) {
            allEntities.add(EntityType.from((CompoundTag) tag));
        }

        Blocks.REGISTRY.verify(buf);

        EntityTypes.REGISTRY.verify(buf);
    }

    @Override
    public void write(PacketByteBuf buf) {
        PacketTypes.REGISTRY.write(buf);

        buf.writeTag(yourPlayer.toTag());

        buf.writeTag(TagUtils.toList(allPlayers));

        ListTag entitiesTag = ListTag.of();
        for (Entity entity : allEntities) {
            entitiesTag.add(entity.toTag());
        }
        buf.writeTag(entitiesTag);

        Blocks.REGISTRY.write(buf);

        EntityTypes.REGISTRY.write(buf);
    }

    @Override
    public void apply(ClientPacketListener listener) {
        listener.onHello(this);
    }
}
