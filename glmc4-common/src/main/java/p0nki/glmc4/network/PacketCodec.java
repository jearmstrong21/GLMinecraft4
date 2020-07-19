package p0nki.glmc4.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.DecoderException;
import p0nki.glmc4.network.packet.Packet;
import p0nki.glmc4.network.packet.PacketType;
import p0nki.glmc4.network.packet.PacketTypes;

import java.util.List;

public class PacketCodec extends ByteToMessageCodec<Packet<?>> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet<?> msg, ByteBuf out) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeInt(PacketTypes.REGISTRY.get(msg.getType()).getIndex());
        buf.writeEquivalent(msg);
        out.writeInt(buf.readableBytes());
        out.writeBytes(buf);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        if (in.readableBytes() < 4) return;
        in.markReaderIndex();
        int overallSize = in.readInt();
        if (in.readableBytes() < overallSize) {
            in.resetReaderIndex();
            return;
        }
        byte[] decoded = new byte[overallSize];
        in.readBytes(decoded);
        PacketByteBuf buf = new PacketByteBuf(Unpooled.wrappedBuffer(decoded));
        int id = buf.readInt();
        if (!PacketTypes.REGISTRY.hasIndex(id))
            throw new DecoderException(String.format("Invalid packet ID not contained in registry: %s", id));
        PacketType<?> type = PacketTypes.REGISTRY.get(id).getValue();
        Packet<?> packet = type.create();
        packet.read(buf);
        out.add(packet);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("Exception caught");
        ctx.close();
    }
}
