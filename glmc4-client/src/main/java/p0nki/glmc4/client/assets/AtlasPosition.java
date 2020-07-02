package p0nki.glmc4.client.assets;

public class AtlasPosition {

    public int x;
    public int y;
    public int w;
    public int h;
    public int tw;
    public int th;

    public double x0;
    public double y0;
    public double x1;
    public double y1;

    public AtlasPosition(int x, int y, int w, int h, int tw, int th) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.tw = tw;
        this.th = th;
        this.x0 = (double) x / tw;
        this.y0 = (double) y / th;
        this.x1 = (double) (x + w) / tw;
        this.y1 = (double) (y + h) / th;
    }

}
