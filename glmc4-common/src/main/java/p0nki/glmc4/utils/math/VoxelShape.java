package p0nki.glmc4.utils.math;

import org.joml.AABBf;
import org.joml.Rayf;

import java.util.Arrays;

/**
 * just use box::testAABB to generate one of these from an AABBf
 */
public abstract class VoxelShape {

    public static final VoxelShape EMPTY = new VoxelShape() {
        @Override
        public boolean collidesWith(AABBf aabb) {
            return false;
        }

        @Override
        public boolean collidesWith(Rayf ray) {
            return false;
        }

        @Override
        public String toString() {
            return "empty";
        }
    };

    public static VoxelShape of(AABBf aabb) {
        return of(aabb, aabb.toString());
    }

    public static VoxelShape of(AABBf aabb, String name) {
        return new VoxelShape() {
            @Override
            public boolean collidesWith(AABBf box) {
                return aabb.testAABB(box);
            }

            @Override
            public boolean collidesWith(Rayf ray) {
                return aabb.testRay(ray);
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
            public boolean collidesWith(AABBf aabb) {
                for (VoxelShape box : boxes) {
                    if (box.collidesWith(aabb)) return true;
                }
                return false;
            }

            @Override
            public boolean collidesWith(Rayf ray) {
                for (VoxelShape box : boxes) {
                    if (box.collidesWith(ray)) return true;
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
            public boolean collidesWith(AABBf aabb) {
                for (VoxelShape box : boxes) {
                    if (!box.collidesWith(aabb)) return false;
                }
                return true;
            }

            @Override
            public boolean collidesWith(Rayf ray) {
                for (VoxelShape box : boxes) {
                    if (!box.collidesWith(ray)) return false;
                }
                return true;
            }

            @Override
            public String toString() {
                return "intersection" + Arrays.toString(boxes);
            }
        };
    }

    public abstract boolean collidesWith(AABBf aabb);

    public abstract boolean collidesWith(Rayf ray);

}
