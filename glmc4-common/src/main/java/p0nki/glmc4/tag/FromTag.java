package p0nki.glmc4.tag;

public interface FromTag<R extends FromTag<R, T>, T extends Tag<T>> {

    R fromTag(T tag);

}
