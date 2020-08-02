package p0nki.glmc4.world.gen.feature;

import org.joml.Vector3i;
import p0nki.glmc4.world.gen.ctx.ReadWriteWorldContext;

import java.util.Random;

public abstract class Feature {

    public abstract void generate(ReadWriteWorldContext world, Vector3i position, Random random);

}
