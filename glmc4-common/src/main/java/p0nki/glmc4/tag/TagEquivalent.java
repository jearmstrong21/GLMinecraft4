package p0nki.glmc4.tag;

public interface TagEquivalent<R extends TagEquivalent<R, T>, T extends Tag<T>> extends ToTag<T>, FromTag<R, T> {

}
