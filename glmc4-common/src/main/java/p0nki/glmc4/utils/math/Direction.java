package p0nki.glmc4.utils.math;

public enum Direction {

    XMI(-1, 0, 0),
    XPL(1, 0, 0),
    YMI(0, -1, 0),
    YPL(0, 1, 0),
    ZMI(0, 0, -1),
    ZPL(0, 0, 1);

    private final int x;
    private final int y;
    private final int z;

    Direction(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }
}
