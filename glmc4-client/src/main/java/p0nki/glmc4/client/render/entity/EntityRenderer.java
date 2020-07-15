package p0nki.glmc4.client.render.entity;

import p0nki.glmc4.client.gl.WorldRenderContext;
import p0nki.glmc4.entity.Entity;

public abstract class EntityRenderer<E extends Entity> {

    public abstract void initialize();

    protected abstract void renderType(WorldRenderContext context, E entity);

    @SuppressWarnings("unchecked") // :sunglasses:
    public final void render(WorldRenderContext context, Entity entity) {
        renderType(context, (E) entity);
    }

}
