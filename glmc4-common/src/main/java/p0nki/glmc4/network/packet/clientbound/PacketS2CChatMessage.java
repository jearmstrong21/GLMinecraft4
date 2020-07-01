package p0nki.glmc4.network.packet.clientbound;

import p0nki.glmc4.utils.DataStreamUtils;

import java.io.*;

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
    public void read(DataInput input) throws IOException {
        source = DataStreamUtils.readString(input);
        message = DataStreamUtils.readString(input);
    }

    @Override
    public void write(DataOutput output) throws IOException {
        DataStreamUtils.writeString(output, source);
        DataStreamUtils.writeString(output, message);
    }

    @Override
    public void apply(ClientPacketListener listener) {
        listener.onChatMessage(this);
    }
}
