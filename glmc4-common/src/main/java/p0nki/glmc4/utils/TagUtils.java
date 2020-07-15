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

    public static ListTag toList(List<? extends ToTag<?>> compoundTags) {
        return new ListTag(compoundTags.stream().map(ToTag::toTag).collect(Collectors.toList()));
    }

    public static <T extends FromTag<T, CompoundTag>> List<T> fromList(ListTag listTag, Supplier<T> supplier) {
        return listTag.stream().map(tag -> supplier.get().fromTag((CompoundTag) tag)).collect(Collectors.toList());
    }

    public static Vector3f from3f(Tag<?> tag) {
        ListTag list = (ListTag) tag;
        if (list.size() != 3) throw new IllegalArgumentException();
        return new Vector3f(((FloatTag) list.get(0)).getValue(), ((FloatTag) list.get(1)).getValue(), ((FloatTag) list.get(2)).getValue());
    }

    public static UUID fromUUID(Tag<?> tag) {
        ListTag list = (ListTag) tag;
        if (list.size() != 2) throw new IllegalArgumentException();
        return new UUID(((LongTag) list.get(0)).get(), ((LongTag) list.get(1)).get());
    }

    public static Tag<?> of(Vector3f value) {
        return new ListTag(new FloatTag(value.x), new FloatTag(value.y), new FloatTag(value.z));
    }

    public static Tag<?> of(UUID uuid) {
        return new ListTag(new LongTag(uuid.getMostSignificantBits()), new LongTag(uuid.getLeastSignificantBits()));
    }

}
