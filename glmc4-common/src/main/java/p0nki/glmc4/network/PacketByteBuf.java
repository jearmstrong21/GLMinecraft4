package p0nki.glmc4.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.util.ByteProcessor;
import p0nki.glmc4.tag.CompoundTag;
import p0nki.glmc4.tag.ListTag;
import p0nki.glmc4.tag.Tag;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.charset.Charset;
import java.util.UUID;

public class PacketByteBuf extends ByteBuf {

    private final ByteBuf parent;

    public PacketByteBuf(ByteBuf parent) {
        this.parent = parent;
    }

    public void writeTag(Tag value) {
        value.write(this);
    }

    public void writeUuid(UUID value) {
        writeLong(value.getMostSignificantBits());
        writeLong(value.getLeastSignificantBits());
    }

    public void writeString(String value) {
        writeInt(value.length());
        writeBytes(value.getBytes());
    }

    public void writeEquivalent(Equivalent equivalent) {
        equivalent.write(this);
    }

    public CompoundTag readCompoundTag() {
        return CompoundTag.READER.read(this);
    }

    public ListTag readListTag() {
        return ListTag.READER.read(this);
    }

    public <T extends Equivalent> T readEquivalent(T equivalent) {
        equivalent.read(this);
        return equivalent;
    }

    public String readString() {
        byte[] dst = new byte[readInt()];
        readBytes(dst);
        return new String(dst);
    }

    public UUID readUuid() {
        return new UUID(readLong(), readLong());
    }

    @Override
    public int capacity() {
        return parent.capacity();
    }

    @Override
    public ByteBuf capacity(int newCapacity) {
        return parent.capacity(newCapacity);
    }

    @Override
    public int maxCapacity() {
        return parent.maxCapacity();
    }

    @Override
    public ByteBufAllocator alloc() {
        return parent.alloc();
    }

    @Override
    public ByteOrder order() {
        //noinspection deprecation
        return parent.order();
    }

    @Override
    public ByteBuf order(ByteOrder endianness) {
        //noinspection deprecation
        return parent.order(endianness);
    }

    @Override
    public ByteBuf unwrap() {
        return parent.unwrap();
    }

    @Override
    public boolean isDirect() {
        return parent.isDirect();
    }

    @Override
    public boolean isReadOnly() {
        return parent.isReadOnly();
    }

    @Override
    public ByteBuf asReadOnly() {
        return parent.asReadOnly();
    }

    @Override
    public int readerIndex() {
        return parent.readerIndex();
    }

    @Override
    public ByteBuf readerIndex(int readerIndex) {
        return parent.readerIndex(readerIndex);
    }

    @Override
    public int writerIndex() {
        return parent.writerIndex();
    }

    @Override
    public ByteBuf writerIndex(int writerIndex) {
        return parent.writerIndex(writerIndex);
    }

    @Override
    public ByteBuf setIndex(int readerIndex, int writerIndex) {
        return parent.setIndex(readerIndex, writerIndex);
    }

    @Override
    public int readableBytes() {
        return parent.readableBytes();
    }

    @Override
    public int writableBytes() {
        return parent.writableBytes();
    }

    @Override
    public int maxWritableBytes() {
        return parent.maxWritableBytes();
    }

    @Override
    public boolean isReadable() {
        return parent.isReadable();
    }

    @Override
    public boolean isReadable(int size) {
        return parent.isReadable(size);
    }

    @Override
    public boolean isWritable() {
        return parent.isWritable();
    }

    @Override
    public boolean isWritable(int size) {
        return parent.isWritable(size);
    }

    @Override
    public ByteBuf clear() {
        return parent.clear();
    }

    @Override
    public ByteBuf markReaderIndex() {
        return parent.markReaderIndex();
    }

    @Override
    public ByteBuf resetReaderIndex() {
        return parent.resetReaderIndex();
    }

    @Override
    public ByteBuf markWriterIndex() {
        return parent.markWriterIndex();
    }

    @Override
    public ByteBuf resetWriterIndex() {
        return parent.resetWriterIndex();
    }

    @Override
    public ByteBuf discardReadBytes() {
        return parent.discardReadBytes();
    }

    @Override
    public ByteBuf discardSomeReadBytes() {
        return parent.discardSomeReadBytes();
    }

    @Override
    public ByteBuf ensureWritable(int minWritableBytes) {
        return parent.ensureWritable(minWritableBytes);
    }

    @Override
    public int ensureWritable(int minWritableBytes, boolean force) {
        return parent.ensureWritable(minWritableBytes, force);
    }

    @Override
    public boolean getBoolean(int index) {
        return parent.getBoolean(index);
    }

    @Override
    public byte getByte(int index) {
        return parent.getByte(index);
    }

    @Override
    public short getUnsignedByte(int index) {
        return parent.getUnsignedByte(index);
    }

    @Override
    public short getShort(int index) {
        return parent.getShort(index);
    }

    @Override
    public short getShortLE(int index) {
        return parent.getShortLE(index);
    }

    @Override
    public int getUnsignedShort(int index) {
        return parent.getUnsignedShort(index);
    }

    @Override
    public int getUnsignedShortLE(int index) {
        return parent.getUnsignedShortLE(index);
    }

    @Override
    public int getMedium(int index) {
        return parent.getMedium(index);
    }

    @Override
    public int getMediumLE(int index) {
        return parent.getMediumLE(index);
    }

    @Override
    public int getUnsignedMedium(int index) {
        return parent.getUnsignedMedium(index);
    }

    @Override
    public int getUnsignedMediumLE(int index) {
        return parent.getUnsignedMediumLE(index);
    }

    @Override
    public int getInt(int index) {
        return parent.getInt(index);
    }

    @Override
    public int getIntLE(int index) {
        return parent.getIntLE(index);
    }

    @Override
    public long getUnsignedInt(int index) {
        return parent.getUnsignedInt(index);
    }

    @Override
    public long getUnsignedIntLE(int index) {
        return parent.getUnsignedIntLE(index);
    }

    @Override
    public long getLong(int index) {
        return parent.getLong(index);
    }

    @Override
    public long getLongLE(int index) {
        return parent.getLongLE(index);
    }

    @Override
    public char getChar(int index) {
        return parent.getChar(index);
    }

    @Override
    public float getFloat(int index) {
        return parent.getFloat(index);
    }

    @Override
    public double getDouble(int index) {
        return parent.getDouble(index);
    }

    @Override
    public ByteBuf getBytes(int index, ByteBuf dst) {
        return parent.getBytes(index, dst);
    }

    @Override
    public ByteBuf getBytes(int index, ByteBuf dst, int length) {
        return parent.getBytes(index, dst, length);
    }

    @Override
    public ByteBuf getBytes(int index, ByteBuf dst, int dstIndex, int length) {
        return parent.getBytes(index, dst, dstIndex, length);
    }

    @Override
    public ByteBuf getBytes(int index, byte[] dst) {
        return parent.getBytes(index, dst);
    }

    @Override
    public ByteBuf getBytes(int index, byte[] dst, int dstIndex, int length) {
        return parent.getBytes(index, dst, dstIndex, length);
    }

    @Override
    public ByteBuf getBytes(int index, ByteBuffer dst) {
        return parent.getBytes(index, dst);
    }

    @Override
    public ByteBuf getBytes(int index, OutputStream out, int length) throws IOException {
        return parent.getBytes(index, out, length);
    }

    @Override
    public int getBytes(int index, GatheringByteChannel out, int length) throws IOException {
        return parent.getBytes(index, out, length);
    }

    @Override
    public int getBytes(int index, FileChannel out, long position, int length) throws IOException {
        return parent.getBytes(index, out, position, length);
    }

    @Override
    public CharSequence getCharSequence(int index, int length, Charset charset) {
        return parent.getCharSequence(index, length, charset);
    }

    @Override
    public ByteBuf setBoolean(int index, boolean value) {
        return parent.setBoolean(index, value);
    }

    @Override
    public ByteBuf setByte(int index, int value) {
        return parent.setByte(index, value);
    }

    @Override
    public ByteBuf setShort(int index, int value) {
        return parent.setShort(index, value);
    }

    @Override
    public ByteBuf setShortLE(int index, int value) {
        return parent.setShortLE(index, value);
    }

    @Override
    public ByteBuf setMedium(int index, int value) {
        return parent.setMedium(index, value);
    }

    @Override
    public ByteBuf setMediumLE(int index, int value) {
        return parent.setMediumLE(index, value);
    }

    @Override
    public ByteBuf setInt(int index, int value) {
        return parent.setInt(index, value);
    }

    @Override
    public ByteBuf setIntLE(int index, int value) {
        return parent.setIntLE(index, value);
    }

    @Override
    public ByteBuf setLong(int index, long value) {
        return parent.setLong(index, value);
    }

    @Override
    public ByteBuf setLongLE(int index, long value) {
        return parent.setLongLE(index, value);
    }

    @Override
    public ByteBuf setChar(int index, int value) {
        return parent.setChar(index, value);
    }

    @Override
    public ByteBuf setFloat(int index, float value) {
        return parent.setFloat(index, value);
    }

    @Override
    public ByteBuf setDouble(int index, double value) {
        return parent.setDouble(index, value);
    }

    @Override
    public ByteBuf setBytes(int index, ByteBuf src) {
        return parent.setBytes(index, src);
    }

    @Override
    public ByteBuf setBytes(int index, ByteBuf src, int length) {
        return parent.setBytes(index, src, length);
    }

    @Override
    public ByteBuf setBytes(int index, ByteBuf src, int srcIndex, int length) {
        return parent.setBytes(index, src, srcIndex, length);
    }

    @Override
    public ByteBuf setBytes(int index, byte[] src) {
        return parent.setBytes(index, src);
    }

    @Override
    public ByteBuf setBytes(int index, byte[] src, int srcIndex, int length) {
        return parent.setBytes(index, src, srcIndex, length);
    }

    @Override
    public ByteBuf setBytes(int index, ByteBuffer src) {
        return parent.setBytes(index, src);
    }

    @Override
    public int setBytes(int index, InputStream in, int length) throws IOException {
        return parent.setBytes(index, in, length);
    }

    @Override
    public int setBytes(int index, ScatteringByteChannel in, int length) throws IOException {
        return parent.setBytes(index, in, length);
    }

    @Override
    public int setBytes(int index, FileChannel in, long position, int length) throws IOException {
        return parent.setBytes(index, in, position, length);
    }

    @Override
    public ByteBuf setZero(int index, int length) {
        return parent.setZero(index, length);
    }

    @Override
    public int setCharSequence(int index, CharSequence sequence, Charset charset) {
        return parent.setCharSequence(index, sequence, charset);
    }

    @Override
    public boolean readBoolean() {
        return parent.readBoolean();
    }

    @Override
    public byte readByte() {
        return parent.readByte();
    }

    @Override
    public short readUnsignedByte() {
        return parent.readUnsignedByte();
    }

    @Override
    public short readShort() {
        return parent.readShort();
    }

    @Override
    public short readShortLE() {
        return parent.readShortLE();
    }

    @Override
    public int readUnsignedShort() {
        return parent.readUnsignedShort();
    }

    @Override
    public int readUnsignedShortLE() {
        return parent.readUnsignedShortLE();
    }

    @Override
    public int readMedium() {
        return parent.readMedium();
    }

    @Override
    public int readMediumLE() {
        return parent.readMediumLE();
    }

    @Override
    public int readUnsignedMedium() {
        return parent.readUnsignedMedium();
    }

    @Override
    public int readUnsignedMediumLE() {
        return parent.readUnsignedMediumLE();
    }

    @Override
    public int readInt() {
        return parent.readInt();
    }

    @Override
    public int readIntLE() {
        return parent.readIntLE();
    }

    @Override
    public long readUnsignedInt() {
        return parent.readUnsignedInt();
    }

    @Override
    public long readUnsignedIntLE() {
        return parent.readUnsignedIntLE();
    }

    @Override
    public long readLong() {
        return parent.readLong();
    }

    @Override
    public long readLongLE() {
        return parent.readLongLE();
    }

    @Override
    public char readChar() {
        return parent.readChar();
    }

    @Override
    public float readFloat() {
        return parent.readFloat();
    }

    @Override
    public double readDouble() {
        return parent.readDouble();
    }

    @Override
    public ByteBuf readBytes(int length) {
        return parent.readBytes(length);
    }

    @Override
    public ByteBuf readSlice(int length) {
        return parent.readSlice(length);
    }

    @Override
    public ByteBuf readRetainedSlice(int length) {
        return parent.readRetainedSlice(length);
    }

    @Override
    public ByteBuf readBytes(ByteBuf dst) {
        return parent.readBytes(dst);
    }

    @Override
    public ByteBuf readBytes(ByteBuf dst, int length) {
        return parent.readBytes(dst, length);
    }

    @Override
    public ByteBuf readBytes(ByteBuf dst, int dstIndex, int length) {
        return parent.readBytes(dst, dstIndex, length);
    }

    @Override
    public ByteBuf readBytes(byte[] dst) {
        return parent.readBytes(dst);
    }

    @Override
    public ByteBuf readBytes(byte[] dst, int dstIndex, int length) {
        return parent.readBytes(dst, dstIndex, length);
    }

    @Override
    public ByteBuf readBytes(ByteBuffer dst) {
        return parent.readBytes(dst);
    }

    @Override
    public ByteBuf readBytes(OutputStream out, int length) throws IOException {
        return parent.readBytes(out, length);
    }

    @Override
    public int readBytes(GatheringByteChannel out, int length) throws IOException {
        return parent.readBytes(out, length);
    }

    @Override
    public CharSequence readCharSequence(int length, Charset charset) {
        return parent.readCharSequence(length, charset);
    }

    @Override
    public int readBytes(FileChannel out, long position, int length) throws IOException {
        return parent.readBytes(out, position, length);
    }

    @Override
    public ByteBuf skipBytes(int length) {
        return parent.skipBytes(length);
    }

    @Override
    public ByteBuf writeBoolean(boolean value) {
        return parent.writeBoolean(value);
    }

    @Override
    public ByteBuf writeByte(int value) {
        return parent.writeByte(value);
    }

    @Override
    public ByteBuf writeShort(int value) {
        return parent.writeShort(value);
    }

    @Override
    public ByteBuf writeShortLE(int value) {
        return parent.writeShortLE(value);
    }

    @Override
    public ByteBuf writeMedium(int value) {
        return parent.writeMedium(value);
    }

    @Override
    public ByteBuf writeMediumLE(int value) {
        return parent.writeMediumLE(value);
    }

    @Override
    public ByteBuf writeInt(int value) {
        return parent.writeInt(value);
    }

    @Override
    public ByteBuf writeIntLE(int value) {
        return parent.writeIntLE(value);
    }

    @Override
    public ByteBuf writeLong(long value) {
        return parent.writeLong(value);
    }

    @Override
    public ByteBuf writeLongLE(long value) {
        return parent.writeLongLE(value);
    }

    @Override
    public ByteBuf writeChar(int value) {
        return parent.writeChar(value);
    }

    @Override
    public ByteBuf writeFloat(float value) {
        return parent.writeFloat(value);
    }

    @Override
    public ByteBuf writeDouble(double value) {
        return parent.writeDouble(value);
    }

    @Override
    public ByteBuf writeBytes(ByteBuf src) {
        return parent.writeBytes(src);
    }

    @Override
    public ByteBuf writeBytes(ByteBuf src, int length) {
        return parent.writeBytes(src, length);
    }

    @Override
    public ByteBuf writeBytes(ByteBuf src, int srcIndex, int length) {
        return parent.writeBytes(src, srcIndex, length);
    }

    @Override
    public ByteBuf writeBytes(byte[] src) {
        return parent.writeBytes(src);
    }

    @Override
    public ByteBuf writeBytes(byte[] src, int srcIndex, int length) {
        return parent.writeBytes(src, srcIndex, length);
    }

    @Override
    public ByteBuf writeBytes(ByteBuffer src) {
        return parent.writeBytes(src);
    }

    @Override
    public int writeBytes(InputStream in, int length) throws IOException {
        return parent.writeBytes(in, length);
    }

    @Override
    public int writeBytes(ScatteringByteChannel in, int length) throws IOException {
        return parent.writeBytes(in, length);
    }

    @Override
    public int writeBytes(FileChannel in, long position, int length) throws IOException {
        return parent.writeBytes(in, position, length);
    }

    @Override
    public ByteBuf writeZero(int length) {
        return parent.writeZero(length);
    }

    @Override
    public int writeCharSequence(CharSequence sequence, Charset charset) {
        return parent.writeCharSequence(sequence, charset);
    }

    @Override
    public int indexOf(int fromIndex, int toIndex, byte value) {
        return parent.indexOf(fromIndex, toIndex, value);
    }

    @Override
    public int bytesBefore(byte value) {
        return parent.bytesBefore(value);
    }

    @Override
    public int bytesBefore(int length, byte value) {
        return parent.bytesBefore(length, value);
    }

    @Override
    public int bytesBefore(int index, int length, byte value) {
        return parent.bytesBefore(index, length, value);
    }

    @Override
    public int forEachByte(ByteProcessor processor) {
        return parent.forEachByte(processor);
    }

    @Override
    public int forEachByte(int index, int length, ByteProcessor processor) {
        return parent.forEachByte(index, length, processor);
    }

    @Override
    public int forEachByteDesc(ByteProcessor processor) {
        return parent.forEachByteDesc(processor);
    }

    @Override
    public int forEachByteDesc(int index, int length, ByteProcessor processor) {
        return parent.forEachByteDesc(index, length, processor);
    }

    @Override
    public ByteBuf copy() {
        return parent.copy();
    }

    @Override
    public ByteBuf copy(int index, int length) {
        return parent.copy(index, length);
    }

    @Override
    public ByteBuf slice() {
        return parent.slice();
    }

    @Override
    public ByteBuf retainedSlice() {
        return parent.retainedSlice();
    }

    @Override
    public ByteBuf slice(int index, int length) {
        return parent.slice(index, length);
    }

    @Override
    public ByteBuf retainedSlice(int index, int length) {
        return parent.retainedSlice(index, length);
    }

    @Override
    public ByteBuf duplicate() {
        return parent.duplicate();
    }

    @Override
    public ByteBuf retainedDuplicate() {
        return parent.retainedDuplicate();
    }

    @Override
    public int nioBufferCount() {
        return parent.nioBufferCount();
    }

    @Override
    public ByteBuffer nioBuffer() {
        return parent.nioBuffer();
    }

    @Override
    public ByteBuffer nioBuffer(int index, int length) {
        return parent.nioBuffer(index, length);
    }

    @Override
    public ByteBuffer internalNioBuffer(int index, int length) {
        return parent.internalNioBuffer(index, length);
    }

    @Override
    public ByteBuffer[] nioBuffers() {
        return parent.nioBuffers();
    }

    @Override
    public ByteBuffer[] nioBuffers(int index, int length) {
        return parent.nioBuffers();
    }

    @Override
    public boolean hasArray() {
        return parent.hasArray();
    }

    @Override
    public byte[] array() {
        return parent.array();
    }

    @Override
    public int arrayOffset() {
        return parent.arrayOffset();
    }

    @Override
    public boolean hasMemoryAddress() {
        return parent.hasMemoryAddress();
    }

    @Override
    public long memoryAddress() {
        return parent.memoryAddress();
    }

    @Override
    public String toString(Charset charset) {
        return parent.toString(charset);
    }

    @Override
    public String toString(int index, int length, Charset charset) {
        return parent.toString(index, length, charset);
    }

    @Override
    public int hashCode() {
        return parent.hashCode();
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(Object obj) {
        return parent.equals(obj);
    }

    @Override
    public int compareTo(ByteBuf buffer) {
        return parent.compareTo(buffer);
    }

    @Override
    public String toString() {
        return parent.toString();
    }

    @Override
    public ByteBuf retain(int increment) {
        return parent.retain(increment);
    }

    @Override
    public int refCnt() {
        return parent.refCnt();
    }

    @Override
    public ByteBuf retain() {
        return parent.retain();
    }

    @Override
    public ByteBuf touch() {
        return parent.touch();
    }

    @Override
    public ByteBuf touch(Object hint) {
        return parent.touch(hint);
    }

    @Override
    public boolean release() {
        return parent.release();
    }

    @Override
    public boolean release(int decrement) {
        return parent.release(decrement);
    }

    public interface Equivalent {
        void write(PacketByteBuf buf);

        void read(PacketByteBuf buf);
    }
}
