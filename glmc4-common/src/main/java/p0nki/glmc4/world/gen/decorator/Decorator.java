package p0nki.glmc4.world.gen.decorator;

import org.joml.Vector3i;
import p0nki.glmc4.world.gen.biomes.Biome;
import p0nki.glmc4.world.gen.ctx.ReadWorldContext;

import java.util.Random;
import java.util.stream.Stream;

public abstract class Decorator {

    public static Decorator filterBiome(Biome biome, Decorator decorator) {
        return new Decorator() {
            @Override
            public Stream<Vector3i> generate(ReadWorldContext world, Vector3i position, Random random) {
                return decorator.generate(world, position, random).filter(p -> biome == world.getBiome(p.x, p.z));
            }
        };
    }

    public abstract Stream<Vector3i> generate(ReadWorldContext world, Vector3i position, Random random);

}
