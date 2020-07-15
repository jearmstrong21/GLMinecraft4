package p0nki.glmc4.network;

import org.apache.commons.lang3.ArrayUtils;

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class PacketWriteBuf {

    private final List<Byte> bytes = new ArrayList<>();

    public PacketWriteBuf() {
    }

    public void write(ByteBufEquivalent value) {
        value.write(this);
    }

    public void writeInt(int value) {
        writeBytes(ByteBuffer.allocate(Integer.BYTES).putInt(value).array());
    }

    public void writeLong(long value) {
        writeBytes(ByteBuffer.allocate(Long.BYTES).putLong(value).array());
    }

    public void writeChar(char value) {
        writeBytes(ByteBuffer.allocate(Character.BYTES).putChar(value).array());
    }

    public void writeString(String value) {
        writeInt(value.length());
        for (int i = 0; i < value.length(); i++) {
            writeChar(value.charAt(i));
        }
    }

    public void writeByte(byte value) {
        bytes.add(value);
        if (bytes.size() >= ClientConnection.MAX_PACKET_SIZE) {
            throw new BufferOverflowException();
        }
    }

    public void writeBytes(byte[] value) {
        for (byte b : value) {
            writeByte(b);
        }
    }

    public int size() {
        return bytes.size();
    }

    public byte[] array() {
        return ArrayUtils.toPrimitive(bytes.toArray(new Byte[0]));
    }

}
