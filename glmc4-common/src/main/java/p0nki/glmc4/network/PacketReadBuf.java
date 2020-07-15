package p0nki.glmc4.network;

import java.nio.ByteBuffer;

public class PacketReadBuf {

    private final ByteBuffer buffer;

    public PacketReadBuf(byte[] bytes) {
        buffer = ByteBuffer.wrap(bytes);
    }

    public <T extends ByteBufEquivalent> T read(T value) {
        value.read(this);
        return value;
    }

    public int readInt() {
        return buffer.getInt();
    }

    public long readLong() {
        return buffer.getLong();
    }

    public char readChar() {
        return buffer.getChar();
    }

    public String readString() {
        int length = readInt();
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < length; i++) {
            str.append(readChar());
        }
        return str.toString();
    }

    public byte readByte() {
        return buffer.get();
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
