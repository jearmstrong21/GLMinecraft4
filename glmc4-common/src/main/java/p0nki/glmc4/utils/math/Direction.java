package p0nki.glmc4.utils.math;

public enum Direction {

    XMI(Axis.X, -1, 0, 0),
    XPL(Axis.X, 1, 0, 0),
    YMI(Axis.Y, 0, -1, 0),
    YPL(Axis.Y, 0, 1, 0),
    ZMI(Axis.Z, 0, 0, -1),
    ZPL(Axis.Z, 0, 0, 1);

    private final Axis axis;
    private final int x;
    private final int y;
    private final int z;

    Direction(Axis axis, int x, int y, int z) {
        this.axis = axis;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Axis getAxis() {
        return axis;
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
