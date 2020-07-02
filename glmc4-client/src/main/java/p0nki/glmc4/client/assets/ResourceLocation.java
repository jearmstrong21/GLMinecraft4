package p0nki.glmc4.client.assets;

import java.io.File;
import java.net.URL;

public class ResourceLocation implements Location {

    private final String path;

    public ResourceLocation(String path) {
        this.path = path;
    }

    @Override
    public File asFile() {
        URL url = ClassLoader.getSystemClassLoader().getResource(path);
        if (url == null) return null;
        return new File(url.getFile());
    }

    @Override
    public String toString() {
        return "ResourceLocation[" + path + "]";
    }
}
