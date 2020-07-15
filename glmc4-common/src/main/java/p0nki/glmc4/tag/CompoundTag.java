package p0nki.glmc4.tag;

import org.joml.Vector3f;
import p0nki.glmc4.network.PacketWriteBuf;
import p0nki.glmc4.utils.DataStreamUtils;
import p0nki.glmc4.utils.Identifier;
import p0nki.glmc4.utils.TagUtils;

import javax.annotation.Nonnull;
import java.util.*;

public class CompoundTag extends AbstractMap<String, Tag<?>> implements Tag<CompoundTag>, ToTag<CompoundTag> {

    public static final TagReader<CompoundTag> READER = input -> {
        int size = input.readInt();
        Map<String, Tag<?>> values = new HashMap<>();
        for (int i = 0; i < size; i++) {
            values.put(input.readString(), DataStreamUtils.readTag(input));
        }
        return new CompoundTag(values);
    };

    private final Map<String, Tag<?>> values;

    public CompoundTag() {
        values = new HashMap<>();
    }

    public CompoundTag(Map<String, Tag<?>> values) {
        this.values = new HashMap<>(values);
    }

    public CompoundTag insert(String key, Object value) {
        values.put(key, Tag.of(value));
        return this;
    }

    public CompoundTag insert(String key, Vector3f value) {
        values.put(key, TagUtils.of(value));
        return this;
    }

    public CompoundTag insert(String key, UUID value) {
        values.put(key, TagUtils.of(value));
        return this;
    }

    public ByteArrayTag getByteArray(String key) {
        return (ByteArrayTag) values.get(key);
    }

    public byte getByte(String key) {
        return ((ByteTag) values.get(key)).get();
    }

    public CompoundTag getCompound(String key) {
        return (CompoundTag) values.get(key);
    }

    public IntArrayTag getIntArray(String key) {
        return (IntArrayTag) values.get(key);
    }

    public int getInt(String key) {
        return ((IntTag) values.get(key)).get();
    }

    public ListTag getList(String key) {
        return (ListTag) values.get(key);
    }

    public LongArrayTag getLongArray(String key) {
        return (LongArrayTag) values.get(key);
    }

    public long getLong(String key) {
        return ((LongTag) values.get(key)).get();
    }

    public Vector3f get3f(String key) {
        return TagUtils.from3f(values.get(key));
    }

    public UUID getUUID(String key) {
        return TagUtils.fromUUID(values.get(key));
    }

    public Identifier getIdentifier(String key) {
        return new Identifier(getString(key));
    }

    public String getString(String key) {
        return ((StringTag) values.get(key)).get();
    }

    @Override
    public Tag<?> put(String key, Tag<?> value) {
        Tag<?> original = values.getOrDefault(key, null);
        values.put(key, value);
        return original;
    }

    @Override
    @Nonnull
    public Set<Entry<String, Tag<?>>> entrySet() {
        return values.entrySet();
    }

    @Override
    public void write(PacketWriteBuf output) {
        output.writeInt(values.size());
        for (Entry<String, Tag<?>> entry : values.entrySet()) {
            output.writeString(entry.getKey());
            DataStreamUtils.writeTag(output, entry.getValue());
        }
    }

    @Override
    public TagReader<CompoundTag> reader() {
        return READER;
    }

    @Override
    public byte type() {
        return COMPOUND;
    }

}
