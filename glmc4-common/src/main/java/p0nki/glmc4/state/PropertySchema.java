package p0nki.glmc4.state;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Consumer;

public class PropertySchema {

    private final List<Property<?>> properties;
    private final Map<Property<?>, Integer> bitStart;
    private int currentUsedBits = 0;

    public PropertySchema() {
        properties = new ArrayList<>();
        bitStart = new HashMap<>();
    }

    public <T> void addProperty(Property<T> property) {
        properties.add(property);
        bitStart.put(property, currentUsedBits);
        currentUsedBits += property.bits();
        if (currentUsedBits >= 32)
            throw new IllegalArgumentException(String.format("Invalid property %s", property.getName()));
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean hasProperty(Property<?> property) {
        return properties.contains(property);
    }

    private int mask(Property<?> property) {
        return (~(1 << property.bits()) << (32 - property.bits())) >>> bitStart.get(property);
    }

    public <T> int set(int state, Property<T> property, T value) {
        if (!hasProperty(property))
            throw new IllegalArgumentException(String.format("Invalid property %s", property.getName()));
        if (!property.supportsValue(value))
            throw new IllegalArgumentException(String.format("Invalid value %s", value));
        int mask = mask(property);
        state &= ~mask;
        state |= mask & property.getIndex(value) << 32 - bitStart.get(property) - property.bits();
        return state;
    }

    @Nonnull
    public <T> T get(int state, Property<T> property) {
        if (!hasProperty(property))
            throw new IllegalArgumentException(String.format("Invalid property %s", property.getName()));
        int mask = mask(property);
        int index = (state & mask) >>> 32 - bitStart.get(property) - property.bits();
        if (index < 0 || index >= property.values().size())
            throw new IllegalArgumentException(String.format("Invalid index %s", index));
        return Objects.requireNonNull(property.values().get(index));
    }

    public List<Property<?>> getProperties() {
        return Collections.unmodifiableList(properties);
    }

    public String toString(int state) {
        if (properties.size() == 0) return "{}";
        StringBuilder builder = new StringBuilder("{");
        builder.append(properties.get(0).getName()).append(": ").append(get(state, properties.get(0)));
        for (int i = 1; i < properties.size(); i++) {
            builder.append(", ").append(properties.get(i).getName()).append(": ").append(get(state, properties.get(i)));
        }
        builder.append("}");
        return builder.toString();
    }

    private void forEach(Consumer<List<Object>> consumer, List<Object> currentValues) {
        if (currentValues.size() == properties.size()) {
            consumer.accept(currentValues);
        } else {
            Property<?> nextProperty = properties.get(currentValues.size());
            for (Object value : nextProperty.values()) {
                List<Object> newCurrentValues = new ArrayList<>(currentValues);
                newCurrentValues.add(value);
                forEach(consumer, newCurrentValues);
            }
        }
    }

    public void forEach(Consumer<List<Object>> consumer) {
        forEach(consumer, new ArrayList<>());
    }
}
