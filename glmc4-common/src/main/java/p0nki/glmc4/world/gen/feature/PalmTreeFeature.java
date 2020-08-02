package p0nki.glmc4.world.gen.feature;

import org.joml.Vector3i;
import p0nki.glmc4.block.Blocks;
import p0nki.glmc4.world.gen.ctx.ReadWriteWorldContext;
import p0nki.glmc4.world.gen.decorator.CountHeightMapDecorator;
import p0nki.glmc4.world.gen.decorator.Decorator;

import java.util.Random;

public class PalmTreeFeature extends Feature {

    public static final Decorator DECORATOR = new CountHeightMapDecorator(2);
    public static final Feature INSTANCE = new PalmTreeFeature();

    private PalmTreeFeature() {

    }

    @Override
    public void generate(ReadWriteWorldContext world, Vector3i position, Random random) {
        int h = 15;
        int l = 4;
        for (int i = 0; i < h; i++) {
            world.set(position.x, position.y + i, position.z, Blocks.OAK_LOG.getDefaultState());
        }
        for (int i = 1; i <= l; i++) {
            world.set(position.x - i, position.y + h, position.z, Blocks.OAK_LEAVES.getDefaultState());
            world.set(position.x + i, position.y + h, position.z, Blocks.OAK_LEAVES.getDefaultState());
            world.set(position.x, position.y + h, position.z - i, Blocks.OAK_LEAVES.getDefaultState());
            world.set(position.x, position.y + h, position.z + i, Blocks.OAK_LEAVES.getDefaultState());
        }
    }
}
