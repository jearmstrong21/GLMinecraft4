package p0nki.glmc4.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import p0nki.glmc4.network.packet.NetworkConnection;
import p0nki.glmc4.network.packet.Packet;
import p0nki.glmc4.network.packet.serverbound.ServerPacketListener;

public class ServerNetworkHandler extends SimpleChannelInboundHandler<Packet<ServerPacketListener>> {

    private final ServerPacketListener packetListener;

    public ServerNetworkHandler(ServerPacketListener packetListener) {
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
    protected void channelRead0(ChannelHandlerContext ctx, Packet<ServerPacketListener> msg) {
        msg.apply(packetListener);
    }
}
