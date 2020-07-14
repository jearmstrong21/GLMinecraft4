package p0nki.glmc4.utils;

import org.apache.logging.log4j.MarkerManager;
import p0nki.glmc4.tag.StringTag;
import p0nki.glmc4.tag.TagEquivalent;

public class Identifier extends MarkerManager.Log4jMarker implements TagEquivalent<Identifier, StringTag> {

    public static boolean isValidIdentifier(String id) {
        String[] split = id.split(":");
        if (split.length != 2) return false;
        if (!isValidPortion(split[0])) return false;
        return isValidPortion(split[1]);
    }

    public static void assertValidIdentifier(String id) {
        if (!isValidIdentifier(id)) throw new UnsupportedOperationException("Invalid identifier " + id);
    }

    public static boolean isValidPortion(String value) {
        return value.chars().allMatch(x -> (Character.isLowerCase(x) && Character.isAlphabetic(x)) || Character.isDigit(x) || x == '_');
    }

    public static void assertValidPortion(String value) {
        if (!isValidPortion(value)) throw new UnsupportedOperationException("Invalid identifier portion " + value);
    }

    private String namespace;
    private String path;

    public Identifier(String namespace, String path) {
        super(namespace + ":" + path);
        assertValidPortion(namespace);
        assertValidPortion(path);
        this.namespace = namespace;
        this.path = path;
    }

    public Identifier(String id) {
        super(id);
        String[] split = id.split(":");
        if (split.length != 2) throw new IllegalArgumentException(id);
        assertValidPortion(split[0]);
        assertValidPortion(split[1]);
        namespace = split[0];
        path = split[1];
    }

    public String getNamespace() {
        return namespace;
    }

    public String getPath() {
        return path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Identifier that = (Identifier) o;

        if (!namespace.equals(that.namespace)) return false;
        return path.equals(that.path);
    }

    @Override
    public int hashCode() {
        int result = namespace.hashCode();
        result = 31 * result + path.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return namespace + ":" + path;
    }

    @Override
    public Identifier fromTag(StringTag tag) {
        assertValidIdentifier(tag.get());
        String[] split = tag.get().split(":");
        assertValidPortion(split[0]);
        assertValidPortion(split[1]);
        namespace = split[0];
        path = split[1];
        return this;
    }

    @Override
    public StringTag toTag() {
        return new StringTag(toString());
    }
}
