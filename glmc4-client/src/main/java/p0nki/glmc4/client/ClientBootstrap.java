package p0nki.glmc4.client;

import p0nki.glmc4.Bootstrap;
import p0nki.glmc4.client.assets.TextureAssembler;
import p0nki.glmc4.client.render.block.BlockRenderers;
import p0nki.glmc4.utils.Identifier;

public class ClientBootstrap {

    public static void initialize() {
        Bootstrap.initialize();
        TextureAssembler.get(new Identifier("minecraft:block"));
        BlockRenderers.initialize();
    }

}
