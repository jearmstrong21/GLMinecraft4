package p0nki.glmc4.utils;

import p0nki.glmc4.network.PacketReadBuf;
import p0nki.glmc4.network.PacketWriteBuf;
import p0nki.glmc4.tag.Tag;

import javax.annotation.Nullable;

public class DataStreamUtils {

    private DataStreamUtils() {

    }

    @Nullable
    public static Tag<?> readTag(PacketReadBuf input) {
        byte type = input.readByte();
        if (Tag.isTagId(type)) {
            return Tag.getReader(type).read(input);
        } else {
            return null;
        }
    }

    public static void writeTag(PacketWriteBuf output, Tag<?> tag) {
        output.writeByte(tag.type()); // TODO MAKE THIS A WRITE_BYTE
        tag.write(output);
    }

}
