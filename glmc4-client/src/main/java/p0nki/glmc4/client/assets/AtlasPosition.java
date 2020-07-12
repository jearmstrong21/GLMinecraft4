package p0nki.glmc4.client.assets;

public class AtlasPosition {

    public float x;
    public float y;
    public float w;
    public float h;

    public AtlasPosition(int x, int y, int w, int h, int tw, int th) {
        this.x = (float) x / tw;
        this.y = (float) y / th;
        this.w = (float) w / tw;
        this.h = (float) h / th;
    }

}
