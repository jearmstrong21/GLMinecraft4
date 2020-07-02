package p0nki.glmc4.client.assets;

import java.io.File;

public class LocalLocation implements Location {

    private final String path;

    public LocalLocation(String path) {
        this.path = path;
    }

    @Override
    public File asFile() {
        return new File("run/" + path);
    }

    @Override
    public String toString() {
        return "LocalLocation[" + path + "]";
    }
}
