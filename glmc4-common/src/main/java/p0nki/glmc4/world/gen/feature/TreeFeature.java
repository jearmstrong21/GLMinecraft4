package p0nki.glmc4.world.gen.feature;

import org.joml.Vector3i;
import p0nki.glmc4.block.Blocks;
import p0nki.glmc4.world.gen.ctx.ReadWriteWorldContext;
import p0nki.glmc4.world.gen.decorator.CountHeightMapDecorator;
import p0nki.glmc4.world.gen.decorator.Decorator;

import java.util.Random;

public class TreeFeature extends Feature {

    public static final Decorator DECORATOR = new CountHeightMapDecorator(3);
    public static final TreeFeature INSTANCE = new TreeFeature();

    private TreeFeature() {

    }

    @Override
    public void generate(ReadWriteWorldContext world, Vector3i position, Random random) {
        int H = 4 + random.nextInt(3);
        for (int h = 0; h < H; h++) {
            world.set(new Vector3i(position.x, position.y + h, position.z), Blocks.OAK_LOG.getDefaultState());
        }
        for (int a = -2; a <= 2; a++) {
            for (int c = -2; c <= 2; c++) {
                for (int b = 0; b <= 2; b++) {
                    world.set(new Vector3i(position.x + a, position.y + H + b, position.z + c), Blocks.OAK_LEAVES.getDefaultState());
                }
            }
        }
    }

}
