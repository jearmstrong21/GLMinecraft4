package p0nki.glmc4.utils;

import p0nki.glmc4.tag.CompoundTag;
import p0nki.glmc4.tag.FromCompoundTag;
import p0nki.glmc4.tag.ListTag;
import p0nki.glmc4.tag.ToCompoundTag;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class TagUtils {

    private TagUtils() {

    }

    public static ListTag toList(List<? extends ToCompoundTag> compoundTags) {
        return new ListTag(compoundTags.stream().map(ToCompoundTag::toCompoundTag).collect(Collectors.toList()));
    }

    public static <T extends FromCompoundTag<T>> List<T> fromList(ListTag listTag, Supplier<T> supplier) {
        return listTag.stream().map(tag -> supplier.get().fromCompoundTag((CompoundTag) tag)).collect(Collectors.toList());
    }

}
