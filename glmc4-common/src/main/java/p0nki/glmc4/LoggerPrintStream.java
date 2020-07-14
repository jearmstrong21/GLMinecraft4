package p0nki.glmc4;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import javax.annotation.Nonnull;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Locale;

public class LoggerPrintStream extends PrintStream {

    private static final Logger LOGGER = LogManager.getLogger();
    private final Marker MARKER;

    public LoggerPrintStream(String name, OutputStream out) {
        super(out);
        MARKER = MarkerManager.getMarker(name);
    }

    @Override
    public void print(boolean b) {
        log(b);
    }

    @Override
    public void print(char c) {
        log(c);
    }

    @Override
    public void print(int i) {
        log(i);
    }

    @Override
    public void print(long l) {
        log(l);
    }

    @Override
    public void print(float f) {
        log(f);
    }

    @Override
    public void print(double d) {
        log(d);
    }

    @Override
    public void print(@Nonnull char[] s) {
        log(new String(s));
    }

    @Override
    public void print(String s) {
        log(s);
    }

    @Override
    public void print(Object obj) {
        log(obj);
    }

    @Override
    public void println() {
        log("");
    }

    @Override
    public void println(boolean x) {
        log(x);
    }

    @Override
    public void println(char x) {
        log(x);
    }

    @Override
    public void println(int x) {
        log(x);
    }

    @Override
    public void println(long x) {
        log(x);
    }

    @Override
    public void println(float x) {
        log(x);
    }

    @Override
    public void println(double x) {
        log(x);
    }

    @Override
    public void println(@Nonnull char[] x) {
        log(new String(x));
    }

    @Override
    public void println(String x) {
        log(x);
    }

    @Override
    public void println(Object x) {
        log(x);
    }

    @Override
    public PrintStream printf(@Nonnull String format, Object... args) {
        log(String.format(format, args));
        return this;
    }

    @Override
    public PrintStream printf(Locale l, @Nonnull String format, Object... args) {
        log(String.format(l, format, args));
        return this;
    }

    private void log(Object message) {
        LOGGER.info(MARKER, String.valueOf(message));
    }
}
