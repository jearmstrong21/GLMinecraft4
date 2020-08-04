package p0nki.glmc4.world.gen.decorator;

import org.joml.Vector2i;
import org.joml.Vector3i;
import p0nki.glmc4.world.gen.ctx.ReadWorldContext;

import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CountHeightMapDecorator extends Decorator {

    private final int count;

    public CountHeightMapDecorator(int count) {
        this.count = count;
    }

    @Override
    public Stream<Vector3i> generate(ReadWorldContext world, Vector3i position, Random random) {
        return IntStream.range(0, count).mapToObj(__ -> {
            int x = position.x + random.nextInt(16);
            int z = position.z + random.nextInt(16);
            return new Vector3i(x, world.getGroundHeightMap(new Vector2i(x, z)), z);
        });
    }
}
