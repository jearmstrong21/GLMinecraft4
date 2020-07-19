package p0nki.glmc4.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import p0nki.glmc4.network.packet.NetworkConnection;
import p0nki.glmc4.network.packet.Packet;
import p0nki.glmc4.network.packet.PacketListener;

public class NetworkHandler<L extends PacketListener<L>> extends SimpleChannelInboundHandler<Packet<L>> {

    private final L packetListener;

    public NetworkHandler(L packetListener) {
        this.packetListener = packetListener;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        packetListener.setConnection(new NetworkConnection<>(ctx));
        packetListener.onConnected();
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) {
        packetListener.onDisconnected("No reason currently supported");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet<L> msg) {
        msg.apply(packetListener);
    }
}
