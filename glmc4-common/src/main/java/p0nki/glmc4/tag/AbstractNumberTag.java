package p0nki.glmc4.tag;

public abstract class AbstractNumberTag implements Tag {

    public abstract byte byteValue();

    public abstract short shortValue();

    public abstract int intValue();

    public abstract long longValue();

    public abstract float floatValue();

    public abstract double doubleValue();

    public Number asNumber() {
        return new Number() {
            @Override
            public byte byteValue() {
                return AbstractNumberTag.this.byteValue();
            }

            @Override
            public short shortValue() {
                return AbstractNumberTag.this.shortValue();
            }

            @Override
            public int intValue() {
                return AbstractNumberTag.this.intValue();
            }

            @Override
            public long longValue() {
                return AbstractNumberTag.this.longValue();
            }

            @Override
            public float floatValue() {
                return AbstractNumberTag.this.floatValue();
            }

            @Override
            public double doubleValue() {
                return AbstractNumberTag.this.doubleValue();
            }
        };
    }

}
