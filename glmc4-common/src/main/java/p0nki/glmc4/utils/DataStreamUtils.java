package p0nki.glmc4.utils;

import p0nki.glmc4.tag.Tag;

import javax.annotation.Nullable;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class DataStreamUtils {

    private DataStreamUtils() {

    }

    public static byte[] readBytes(DataInput input, int count) throws IOException {
        byte[] value = new byte[count];
        input.readFully(value);
        return value;
    }

    public static void writeString(DataOutput output, String value) throws IOException {
        output.writeInt(value.length());
        output.writeBytes(value);
    }

    public static String readString(DataInput input) throws IOException {
        return new String(readBytes(input, input.readInt()));
    }

    @Nullable
    public static Tag<?> readTag(DataInput input) throws IOException {
        byte type = input.readByte();
        if (type < 0 || type >= Tag.READERS.length) return null;
        return Tag.READERS[type].read(input);
    }

    public static void writeTag(DataOutput output, Tag<?> tag) throws IOException {
        output.writeByte(tag.type()); // TODO MAKE THIS A WRITE_BYTE
        tag.write(output);
    }

}
