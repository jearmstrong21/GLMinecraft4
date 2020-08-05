package p0nki.glmc4.world.gen.feature;

import org.joml.Vector3i;
import p0nki.glmc4.block.BlockState;
import p0nki.glmc4.block.Blocks;
import p0nki.glmc4.world.gen.ctx.ReadWriteWorldContext;
import p0nki.glmc4.world.gen.decorator.CountHeightMapDecorator;
import p0nki.glmc4.world.gen.decorator.Decorator;

import java.util.Random;

public class FlowerFeature extends Feature {

    public static final Decorator DECORATOR = new CountHeightMapDecorator(20);

    public static final Feature INSTANCE = new FlowerFeature();

    private final BlockState[] blockStates;

    private FlowerFeature() {
        blockStates = new BlockState[]{
                Blocks.RED_TULIP.getDefaultState(),
                Blocks.ORANGE_TULIP.getDefaultState(),
                Blocks.WHITE_TULIP.getDefaultState(),
                Blocks.PINK_TULIP.getDefaultState()
        };
    }

    @Override
    public void generate(ReadWriteWorldContext world, Vector3i position, Random random) {
        if (world.get(position).getBlock() == Blocks.GRASS) {
            world.set(position.x, position.y + 1, position.z, blockStates[random.nextInt(blockStates.length)]);
        }
    }
}
