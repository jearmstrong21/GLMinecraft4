package p0nki.glmc4.world.gen.feature;

import org.joml.Vector3i;
import p0nki.glmc4.block.Blocks;
import p0nki.glmc4.world.gen.ctx.ReadWriteWorldContext;
import p0nki.glmc4.world.gen.decorator.CountHeightMapDecorator;
import p0nki.glmc4.world.gen.decorator.Decorator;

import java.util.Random;

public class LakeFeature extends Feature {

    public static final Decorator DECORATOR = new CountHeightMapDecorator(1);
    public static final LakeFeature FEATURE = new LakeFeature(7);

    private final int size;

    public LakeFeature(int size) {
        this.size = size;
    }

    @Override
    public void generate(ReadWriteWorldContext world, Vector3i position, Random random) {
        for (int x = -size; x <= size; x++) {
            for (int z = -size; z <= size; z++) {
                for (int y = -3; y <= 3; y++) {
                    double r = Math.sqrt(x * x + 0.25F * y * y + z * z);
                    if (r < size - 1) {
                        world.set(position.x + x, position.y + y, position.z + z, (y <= 0 ? Blocks.WATER : Blocks.AIR).getDefaultState());
                    } else if (r < size && y <= 0) {
                        world.set(position.x + x, position.y + y, position.z + z, Blocks.GRAVEL.getDefaultState());
                    }
                }
            }
        }
    }
}
