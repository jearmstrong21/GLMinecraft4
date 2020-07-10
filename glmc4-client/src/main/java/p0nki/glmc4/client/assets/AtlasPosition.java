package p0nki.glmc4.client.assets;

public class AtlasPosition {

    public double x;
    public double y;
    public double w;
    public double h;

    public AtlasPosition(int x, int y, int w, int h, int tw, int th) {
        this.x = (double) x / tw;
        this.y = (double) y / th;
        this.w = (double) w / tw;
        this.h = (double) h / th;
    }

}
