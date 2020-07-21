package p0nki.glmc4.utils.math;

import org.joml.Vector3i;

public class BlockPos {

    private int x;
    private int y;
    private int z;

    public BlockPos() {
        this(0, 0, 0);
    }

    public BlockPos(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public BlockPos add(Vector3i other) {
        return new BlockPos(x + other.x, y + other.y, z + other.z);
    }

    public BlockPos sub(Vector3i other) {
        return new BlockPos(x - other.x, y - other.y, z - other.z);
    }

    public BlockPos offset(Direction direction) {
        return new BlockPos(x + direction.getX(), y + direction.getY(), z + direction.getZ());
    }

    public BlockPos offset(Direction direction, int amount) {
        return new BlockPos(x + direction.getX() * amount, y + direction.getY() * amount, z + direction.getZ() * amount);
    }


    public BlockPos move(Vector3i other) {
        x += other.x;
        y += other.y;
        z += other.z;
        return this;
    }

    public BlockPos move(Direction direction) {
        x += direction.getX();
        y += direction.getY();
        z += direction.getZ();
        return this;
    }

    public BlockPos move(Direction direction, int amount) {
        x += direction.getX() * amount;
        y += direction.getY() * amount;
        z += direction.getZ() * amount;
        return this;
    }


    public boolean isValidY() {
        return y >= 0 && y < 256;
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
