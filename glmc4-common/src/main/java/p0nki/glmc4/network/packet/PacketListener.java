package p0nki.glmc4.network.packet;

import io.netty.channel.ChannelHandlerContext;

public abstract class PacketListener<L extends PacketListener<L>> {

    private ChannelHandlerContext handlerContext = null;

    public abstract void onConnected();

    public abstract void tick();

    public abstract void onDisconnected(String reason);

    public final ChannelHandlerContext getHandlerContext() {
        return handlerContext;
    }

    public void setHandlerContext(ChannelHandlerContext handlerContext) {
        this.handlerContext = handlerContext;
    }

}
