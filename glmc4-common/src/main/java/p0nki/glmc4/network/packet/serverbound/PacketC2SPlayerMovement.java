package p0nki.glmc4.network.packet.serverbound;

import p0nki.glmc4.network.PacketReadBuf;
import p0nki.glmc4.network.PacketWriteBuf;
import p0nki.glmc4.network.packet.Packet;
import p0nki.glmc4.network.packet.PacketDirection;
import p0nki.glmc4.network.packet.PacketTypes;

public class PacketC2SPlayerMovement extends Packet<ServerPacketListener> {

    private boolean forward;
    private boolean back;
    private boolean left;
    private boolean right;

    public PacketC2SPlayerMovement() {
        super(PacketDirection.CLIENT_TO_SERVER, PacketTypes.C2S_PLAYER_MOVEMENT);
    }

    public PacketC2SPlayerMovement(boolean forward, boolean back, boolean left, boolean right) {
        super(PacketDirection.CLIENT_TO_SERVER, PacketTypes.C2S_PLAYER_MOVEMENT);
        this.forward = forward;
        this.back = back;
        this.left = left;
        this.right = right;
    }

    @Override
    public void read(PacketReadBuf input) {
        forward = input.readBoolean();
        back = input.readBoolean();
        left = input.readBoolean();
        right = input.readBoolean();
    }

    @Override
    public void write(PacketWriteBuf output) {
        output.writeBoolean(forward);
        output.writeBoolean(back);
        output.writeBoolean(left);
        output.writeBoolean(right);
    }

    @Override
    public void apply(ServerPacketListener listener) {
        listener.onPlayerMovement(this);
    }
}
