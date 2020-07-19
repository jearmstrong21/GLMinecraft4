package p0nki.glmc4.network.packet.serverbound;

import p0nki.glmc4.network.PacketByteBuf;
import p0nki.glmc4.network.packet.Packet;
import p0nki.glmc4.network.packet.PacketTypes;

public class PacketC2SPlayerMovement extends Packet<ServerPacketListener> {

    private boolean forward;
    private boolean back;
    private boolean left;
    private boolean right;

    public PacketC2SPlayerMovement() {
        super(PacketTypes.C2S_PLAYER_MOVEMENT);
    }

    public PacketC2SPlayerMovement(boolean forward, boolean back, boolean left, boolean right) {
        super(PacketTypes.C2S_PLAYER_MOVEMENT);
        this.forward = forward;
        this.back = back;
        this.left = left;
        this.right = right;
    }

    public boolean isForward() {
        return forward;
    }

    public boolean isBack() {
        return back;
    }

    public boolean isLeft() {
        return left;
    }

    public boolean isRight() {
        return right;
    }

    @Override
    public void read(PacketByteBuf buf) {
        forward = buf.readBoolean();
        back = buf.readBoolean();
        left = buf.readBoolean();
        right = buf.readBoolean();
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBoolean(forward);
        buf.writeBoolean(back);
        buf.writeBoolean(left);
        buf.writeBoolean(right);
    }

    @Override
    public void apply(ServerPacketListener listener) {
        listener.onPlayerMovement(this);
    }
}
