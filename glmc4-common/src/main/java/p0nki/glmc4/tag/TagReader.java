package p0nki.glmc4.tag;

import java.io.DataInput;
import java.io.IOException;

public interface TagReader<T extends Tag<T>> {

    T read(DataInput input) throws IOException;

}
