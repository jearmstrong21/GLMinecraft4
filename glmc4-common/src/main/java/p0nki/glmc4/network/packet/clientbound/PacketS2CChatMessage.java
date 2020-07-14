package p0nki.glmc4.network.packet.clientbound;

import p0nki.glmc4.network.PacketReadBuf;
import p0nki.glmc4.network.PacketWriteBuf;

public class PacketS2CChatMessage extends PacketS2C {

    private String source;
    private String message;

    public PacketS2CChatMessage() {

    }

    public PacketS2CChatMessage(String source, String message) {
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
    public void read(PacketReadBuf input) {
        source = input.readString();
        message = input.readString();
    }

    @Override
    public void write(PacketWriteBuf output) {
        output.writeString(source);
        output.writeString(message);
    }

    @Override
    public void apply(ClientPacketListener listener) {
        listener.onChatMessage(this);
    }
}
