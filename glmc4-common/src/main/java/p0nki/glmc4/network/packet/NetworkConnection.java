package p0nki.glmc4.network.packet;

import io.netty.channel.ChannelHandlerContext;

public final class NetworkConnection<L extends PacketListener<L>> {

    private final ChannelHandlerContext ctx;

    public NetworkConnection(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    public void write(Packet<?> packet) {
        ctx.writeAndFlush(packet);
    }

    public void close() {
        ctx.close();
    }

    public boolean isDead() {
        return ctx.isRemoved();
    }

}
