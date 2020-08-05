package p0nki.glmc4.client.gl;

import p0nki.glmc4.client.MCWindow;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL41.*;

public class Framebuffer {

    private final int id;
    private final int color;

    public Framebuffer() {
        color = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, color);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, MCWindow.getWidth(), MCWindow.getHeight(), 0, GL_BGRA, GL_UNSIGNED_BYTE, (ByteBuffer) null);
        id = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, id);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, color, 0);
        int rbo = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, rbo);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT24, MCWindow.getWidth(), MCWindow.getHeight());
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, rbo);
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public int getColor() {
        return color;
    }

    public void start() {
        glBindFramebuffer(GL_FRAMEBUFFER, id);
    }

    public void end() {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public void use(int unit) {
        glActiveTexture(GL_TEXTURE0 + unit);
        glBindTexture(GL_TEXTURE_2D, color);
    }
}
