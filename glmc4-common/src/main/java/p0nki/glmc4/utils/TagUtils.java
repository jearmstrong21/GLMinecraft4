package p0nki.glmc4.utils;

import org.joml.Vector3f;
import p0nki.glmc4.tag.*;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class TagUtils {

    private TagUtils() {

    }

    public static ListTag toList(List<? extends ToTag> compoundTags) {
        return ListTag.of(compoundTags.stream().map(ToTag::toTag));
    }

    public static <T extends FromTag<T, CompoundTag>> List<T> fromList(ListTag listTag, Supplier<T> supplier) {
        return listTag.stream().map(tag -> supplier.get().fromTag((CompoundTag) tag)).collect(Collectors.toList());
    }

    public static Vector3f from3f(Tag tag) {
        ListTag list = (ListTag) tag;
        if (list.size() != 3) throw new IllegalArgumentException();
        return new Vector3f(((FloatTag) list.get(0)).floatValue(), ((FloatTag) list.get(1)).floatValue(), ((FloatTag) list.get(2)).floatValue());
    }

    public static UUID fromUUID(Tag tag) {
        ListTag list = (ListTag) tag;
        if (list.size() != 2) throw new IllegalArgumentException();
        return new UUID(((LongTag) list.get(0)).longValue(), ((LongTag) list.get(1)).longValue());
    }

    public static Tag of(Vector3f value) {
        return ListTag.of(FloatTag.of(value.x), FloatTag.of(value.y), FloatTag.of(value.z));
    }

    public static Tag of(UUID uuid) {
        return ListTag.of(LongTag.of(uuid.getMostSignificantBits()), LongTag.of(uuid.getLeastSignificantBits()));
    }

}
