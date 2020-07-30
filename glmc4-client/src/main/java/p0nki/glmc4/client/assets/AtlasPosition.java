package p0nki.glmc4.client.assets;

import org.joml.Vector3f;
import p0nki.glmc4.utils.Identifier;

public final class AtlasPosition {

    public final float x;
    public final float y;
    public final float w;
    public final float h;

    public AtlasPosition(int x, int y, int w, int h, int tw, int th) {
        this.x = (float) x / tw;
        this.y = (float) y / th;
        this.w = (float) w / tw;
        this.h = (float) h / th;
    }

    public static AtlasPosition block(Identifier identifier) {
        return TextureAssembler.get(new Identifier("minecraft:block")).getTexture(identifier);
    }

    public TextureLayer layer() {
        return new TextureLayer(this, new Vector3f(1));
    }

    public TextureLayer layer(Vector3f color) {
        return new TextureLayer(this, color);
    }

}
