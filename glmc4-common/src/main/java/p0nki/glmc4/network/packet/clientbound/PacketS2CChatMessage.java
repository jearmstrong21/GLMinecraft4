package p0nki.glmc4.network.packet.clientbound;

import p0nki.glmc4.network.PacketByteBuf;
import p0nki.glmc4.network.packet.Packet;
import p0nki.glmc4.network.packet.PacketTypes;

public class PacketS2CChatMessage extends Packet<ClientPacketListener> {

    private String source;
    private String message;

    public PacketS2CChatMessage() {
        super(PacketTypes.S2C_CHAT_MESSAGE);
    }

    public PacketS2CChatMessage(String source, String message) {
        super(PacketTypes.S2C_CHAT_MESSAGE);
        this.source = source;
        this.message = message;
    }

    public String getSource() {
        return source;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public void read(PacketByteBuf buf) {
        source = buf.readString();
        message = buf.readString();
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeString(source);
        buf.writeString(message);
    }

    @Override
    public void apply(ClientPacketListener listener) {
        listener.onChatMessage(this);
    }
}
