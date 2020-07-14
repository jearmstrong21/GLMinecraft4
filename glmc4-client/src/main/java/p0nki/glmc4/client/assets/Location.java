package p0nki.glmc4.client.assets;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public interface Location {

    File asFile();

    default String loadText() {
        try {
            return Files.readString(asFile().toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
