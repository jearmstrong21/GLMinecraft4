package p0nki.glmc4.client.assets;

import java.io.File;

public interface Location {

    File asFile();

    default void makeParentFolder() {
        asFile().getParentFile().mkdirs();
    }

}
