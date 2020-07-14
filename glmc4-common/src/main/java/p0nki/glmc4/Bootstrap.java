package p0nki.glmc4;

import java.io.PrintStream;

public class Bootstrap {

    public static final PrintStream STDOUT;
    public static final PrintStream STDERR;

    static {
        STDOUT = System.out;
        STDERR = System.err;
    }

    public static void initialize() {
        System.setOut(new LoggerPrintStream("STDOUT", STDOUT));
        System.setErr(new LoggerPrintStream("STDERR", STDERR));
    }

}
