package p0nki.glmc4;

import p0nki.glmc4.block.Blocks;
import p0nki.glmc4.entity.EntityTypes;
import p0nki.glmc4.network.packet.PacketTypes;
import p0nki.glmc4.world.gen.Biomes;

import java.io.PrintStream;

public class CommonBootstrap {

    public static final PrintStream STDOUT;
    public static final PrintStream STDERR;

    static {
        STDOUT = System.out;
        STDERR = System.err;
    }

    public static void initialize() {
        System.setOut(new LoggerPrintStream("STDOUT", STDOUT));
        System.setErr(new LoggerPrintStream("STDERR", STDERR));

        PacketTypes.initialize();
        EntityTypes.initialize();
        Blocks.initialize();
        Biomes.initialize();
    }

}
