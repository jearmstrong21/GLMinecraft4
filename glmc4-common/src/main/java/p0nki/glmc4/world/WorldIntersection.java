package p0nki.glmc4.world;

import org.joml.Vector3i;

public final class WorldIntersection {

    private final Vector3i hit;
    private final Vector3i normal;

    public WorldIntersection(Vector3i hit, Vector3i normal) {
        this.hit = hit;
        this.normal = normal;
    }

    public Vector3i getHit() {
        return hit;
    }

    public Vector3i getNormal() {
        return normal;
    }
}
