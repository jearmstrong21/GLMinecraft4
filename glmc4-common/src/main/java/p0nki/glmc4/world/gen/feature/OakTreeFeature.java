package p0nki.glmc4.world.gen.feature;

import org.joml.Vector3i;
import p0nki.glmc4.block.Block;
import p0nki.glmc4.block.Blocks;
import p0nki.glmc4.world.gen.ctx.ReadWriteWorldContext;
import p0nki.glmc4.world.gen.decorator.CountHeightMapDecorator;
import p0nki.glmc4.world.gen.decorator.Decorator;

import java.util.Random;

public class OakTreeFeature extends Feature {

    public static final Decorator PLAINS = new CountHeightMapDecorator(3);
    public static final Decorator HIGH_PLAINS = new CountHeightMapDecorator(10);
    public static final Feature INSTANCE = new OakTreeFeature();

    private OakTreeFeature() {

    }

    @Override
    public void generate(ReadWriteWorldContext world, Vector3i position, Random random) {
        Block underBlock = world.get(position).getBlock();
        if (underBlock != Blocks.GRASS && underBlock != Blocks.DIRT) return;
        world.set(position, Blocks.DIRT.getDefaultState());
        int H = 4 + random.nextInt(5);
        for (int h = 1; h < H; h++) {
            world.set(new Vector3i(position.x, position.y + h, position.z), Blocks.OAK_LOG.getDefaultState());
        }
        for (int a = -1; a <= 1; a++) {
            for (int c = -1; c <= 1; c++) {
                for (int b = 0; b <= 2; b++) {
                    world.set(new Vector3i(position.x + a, position.y + H + b, position.z + c), Blocks.OAK_LEAVES.getDefaultState());
                }
            }
        }
    }

}
