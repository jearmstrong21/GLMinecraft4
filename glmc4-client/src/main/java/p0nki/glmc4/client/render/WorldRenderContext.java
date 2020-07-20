package p0nki.glmc4.client.render;

import org.joml.Matrix4f;

public class WorldRenderContext {

    private final Matrix4f perspective;
    private final Matrix4f view;

    public WorldRenderContext(Matrix4f perspective, Matrix4f view) {
        this.perspective = perspective;
        this.view = view;
    }

    public Matrix4f getPerspective() {
        return perspective;
    }

    public Matrix4f getView() {
        return view;
    }

}
