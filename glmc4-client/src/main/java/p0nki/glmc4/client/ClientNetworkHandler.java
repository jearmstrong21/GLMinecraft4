package p0nki.glmc4.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import p0nki.glmc4.network.packet.NetworkConnection;
import p0nki.glmc4.network.packet.Packet;
import p0nki.glmc4.network.packet.clientbound.ClientPacketListener;

public class ClientNetworkHandler extends SimpleChannelInboundHandler<Packet<ClientPacketListener>> {

    private final ClientPacketListener packetListener;

    public ClientNetworkHandler(ClientPacketListener packetListener) {
        this.packetListener = packetListener;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        packetListener.setConnection(new NetworkConnection<>(ctx));
        packetListener.onConnected();
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) {
        packetListener.onDisconnected("No disconnect reason supported");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet<ClientPacketListener> msg) {
        msg.apply(packetListener);
    }
}
