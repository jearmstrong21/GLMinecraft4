package p0nki.glmc4.tag;

import java.util.AbstractList;

public abstract class AbstractListTag<T extends Tag> extends AbstractList<T> implements Tag {

    @Override
    public abstract T set(int index, T element);

    @Override
    public abstract void add(int index, T element);

    @Override
    public abstract T remove(int index);

    @Override
    public abstract T get(int index);

    @Override
    public abstract int size();

}
