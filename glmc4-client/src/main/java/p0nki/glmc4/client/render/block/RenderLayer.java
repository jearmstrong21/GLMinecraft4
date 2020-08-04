package p0nki.glmc4.client.render.block;

public enum RenderLayer {

    MAIN,
    TRANSPARENT;

    public boolean showsTo(RenderLayer renderLayer) {
        switch (this) {
            case MAIN:
                return false;
            case TRANSPARENT:
                switch (renderLayer) {
                    case MAIN:
                        return true;
                    case TRANSPARENT:
                        return false;
                }
        }
        throw new IllegalArgumentException(String.format("%s %s", this, renderLayer));
    }
}
