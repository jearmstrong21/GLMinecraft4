package p0nki.glmc4.utils;

public class MathUtils {

    private MathUtils() {

    }

    public static long pack(int a, int b) {
        return (((long) a) << 32) | (b & 0xffffffffL);
    }

    public static int unpackFirst(long l) {
        return (int) (l >> 32);
    }

    public static int unpackSecond(long l) {
        return (int) l;
    }

}
