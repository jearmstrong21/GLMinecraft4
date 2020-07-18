package p0nki.glmc4.tag;

public interface TagEquivalent<R extends TagEquivalent<R, T>, T extends Tag> extends ToTag, FromTag<R, T> {

}
