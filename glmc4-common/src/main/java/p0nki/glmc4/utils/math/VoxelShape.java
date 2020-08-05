package p0nki.glmc4.utils.math;

import org.joml.AABBf;

import java.util.Arrays;

/**
 * just use box::testAABB to generate one of these from an AABBf
 */
public abstract class VoxelShape {

    public static final VoxelShape EMPTY = new VoxelShape() {
        @Override
        public boolean collidesWith(AABBf box) {
            return false;
        }

        @Override
        public String toString() {
            return "empty";
        }
    };

    public static VoxelShape of(AABBf box) {
        return of(box, box.toString());
    }

    public static VoxelShape of(AABBf box, String name) {
        return new VoxelShape() {
            @Override
            public boolean collidesWith(AABBf box1) {
                return box.testAABB(box1);
            }

            @Override
            public String toString() {
                return name;
            }
        };
    }

    public static VoxelShape union(VoxelShape... boxes) {
        return new VoxelShape() {
            @Override
            public boolean collidesWith(AABBf box) {
                for (VoxelShape box1 : boxes) {
                    if (box1.collidesWith(box)) return true;
                }
                return false;
            }

            @Override
            public String toString() {
                return "union" + Arrays.toString(boxes);
            }
        };
    }

    public static VoxelShape intersection(VoxelShape... boxes) {
        return new VoxelShape() {
            @Override
            public boolean collidesWith(AABBf box) {
                for (VoxelShape box1 : boxes) {
                    if (!box1.collidesWith(box)) return false;
                }
                return true;
            }

            @Override
            public String toString() {
                return "intersection" + Arrays.toString(boxes);
            }
        };
    }

    public abstract boolean collidesWith(AABBf box);

}
