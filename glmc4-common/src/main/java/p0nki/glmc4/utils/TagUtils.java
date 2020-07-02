package p0nki.glmc4.utils;

import p0nki.glmc4.tag.CompoundTag;
import p0nki.glmc4.tag.FromTag;
import p0nki.glmc4.tag.ListTag;
import p0nki.glmc4.tag.ToTag;

import java.util.List;
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

}
