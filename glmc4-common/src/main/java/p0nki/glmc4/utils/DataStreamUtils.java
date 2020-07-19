package p0nki.glmc4.utils;

import p0nki.glmc4.network.PacketByteBuf;
import p0nki.glmc4.tag.Tag;

import javax.annotation.Nullable;

public class DataStreamUtils {

    private DataStreamUtils() {

    }

    @Nullable
    public static Tag readTag(PacketByteBuf buf) {
        byte type = buf.readByte();
        if (Tag.isTagId(type)) {
            return Tag.getReader(type).read(buf);
        } else {
            return null;
        }
    }

    public static void writeTag(PacketByteBuf buf, Tag tag) {
        buf.writeByte(tag.type());
        buf.writeTag(tag);
    }

}
