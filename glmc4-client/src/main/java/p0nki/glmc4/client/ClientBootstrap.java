package p0nki.glmc4.client;

import p0nki.glmc4.CommonBootstrap;
import p0nki.glmc4.client.assets.TextureAssembler;
import p0nki.glmc4.client.render.block.BlockRenderers;
import p0nki.glmc4.client.render.entity.EntityRenderers;
import p0nki.glmc4.utils.Identifier;

public class ClientBootstrap {

    public static void initialize() {
        CommonBootstrap.initialize();
        TextureAssembler.get(new Identifier("minecraft:block"));
        BlockRenderers.initialize();
        EntityRenderers.initialize();
    }

}
