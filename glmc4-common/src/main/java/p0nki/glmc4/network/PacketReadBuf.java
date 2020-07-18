package p0nki.glmc4.network;

import java.nio.ByteBuffer;
import java.util.UUID;

public class PacketReadBuf {

    private final ByteBuffer buffer;

    public PacketReadBuf(byte[] bytes) {
        buffer = ByteBuffer.wrap(bytes);
    }

    public <T extends ByteBufEquivalent> T readEquivalent(T value) {
        value.read(this);
        return value;
    }

    public UUID readUuid() {
        return new UUID(readLong(), readLong());
    }

    public byte readByte() {
        return buffer.get();
    }

    public short readShort() {
        return buffer.getShort();
    }

    public int readInt() {
        return buffer.getInt();
    }

    public long readLong() {
        return buffer.getLong();
    }

    public float readFloat() {
        return buffer.getFloat();
    }

    public double readDouble() {
        return buffer.getDouble();
    }

    public char readChar() {
        return buffer.getChar();
    }

    public boolean readBoolean() {
        return readByte() == 1;
    }

    public String readString() {
        int length = readInt();
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < length; i++) {
            str.append(readChar());
        }
        return str.toString();
    }

    public byte[] readBytes(int count) {
        byte[] dst = new byte[count];
        buffer.get(dst);
        return dst;
    }

    public int size() {
        return buffer.remaining();
    }

}
