package p0nki.glmc4.world;

public class ChunkGenerationStatus {

    public static final byte UNKNOWN = 0;
    public static final byte HEIGHTMAP = 1;
    public static final byte DECORATED1 = 2;
    public static final byte SURFACE_BUILD = 3;
    public static final byte DECORATED2 = 4;
    public static final byte SUNLIGHT = 5;
    public static final byte LOADED = 6;

    private ChunkGenerationStatus() {

    }

    /*
    if(chunkEntry.getStatus() >= SUNLIGHT)
     */

}
