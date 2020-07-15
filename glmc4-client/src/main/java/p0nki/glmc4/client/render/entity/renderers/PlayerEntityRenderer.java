package p0nki.glmc4.client.render.entity.renderers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import p0nki.glmc4.client.gl.WorldRenderContext;
import p0nki.glmc4.client.render.entity.EntityRenderer;
import p0nki.glmc4.entity.PlayerEntity;

public class PlayerEntityRenderer extends EntityRenderer<PlayerEntity> {

    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void initialize() {
        LOGGER.fatal("Cannot initialize player renderer yet");
    }

    @Override
    protected void renderType(WorldRenderContext context, PlayerEntity entity) {
        LOGGER.fatal("Cannot render player entity");
    }

}
