package p0nki.glmc4.client.assets;

import org.joml.Vector3f;

public final class TextureLayer {

    public final AtlasPosition atlasPosition;
    public final Vector3f color;

    public TextureLayer(AtlasPosition atlasPosition, Vector3f color) {
        this.atlasPosition = atlasPosition;
        this.color = color;
    }

    public TextureQuad quad() {
        return new TextureQuad(this);
    }

}
