package p0nki.glmc4.network.packet.clientbound;

import p0nki.glmc4.block.Blocks;
import p0nki.glmc4.entity.Entity;
import p0nki.glmc4.entity.EntityTypes;
import p0nki.glmc4.network.PacketReadBuf;
import p0nki.glmc4.network.PacketWriteBuf;
import p0nki.glmc4.network.packet.Packet;
import p0nki.glmc4.network.packet.PacketDirection;
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
        super(PacketDirection.SERVER_TO_CLIENT, PacketTypes.S2C_HELLO);
    }

    public PacketS2CHello(ServerPlayer yourPlayer, List<ServerPlayer> allPlayers, List<Entity> allEntities) {
        super(PacketDirection.SERVER_TO_CLIENT, PacketTypes.S2C_HELLO);
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
    public void read(PacketReadBuf input) {
        PacketTypes.REGISTRY.verify(input);

        yourPlayer = new ServerPlayer().fromTag(CompoundTag.READER.read(input));

        allPlayers = TagUtils.fromList(ListTag.READER.read(input), ServerPlayer::new);

        ListTag entitiesTag = ListTag.READER.read(input);
        allEntities = new ArrayList<>();
        for (Tag<?> tag : entitiesTag) {
            allEntities.add(EntityTypes.from((CompoundTag) tag));
        }

        Blocks.REGISTRY.verify(input);

        EntityTypes.REGISTRY.verify(input);
    }

    @Override
    public void write(PacketWriteBuf output) {
        PacketTypes.REGISTRY.write(output);

        yourPlayer.toTag().write(output);

        TagUtils.toList(allPlayers).write(output);

        ListTag entitiesTag = new ListTag();
        for (Entity entity : allEntities) {
            entitiesTag.add(entity.toTag());
        }
        entitiesTag.write(output);

        Blocks.REGISTRY.write(output);

        EntityTypes.REGISTRY.write(output);
    }

    @Override
    public void apply(ClientPacketListener listener) {
        listener.onHello(this);
    }
}
