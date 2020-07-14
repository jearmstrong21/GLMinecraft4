package p0nki.glmc4.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import java.util.function.IntConsumer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class MCWindow {

    private final static Logger LOGGER = LogManager.getLogger();
    private final static Marker OPENGL = MarkerManager.getMarker("OpenGL");

    private MCWindow() {

    }

    private static long ptr;
    private static Runnable initializeCallback;
    private static IntConsumer frameCallback;
    private static Runnable endCallback;

    public static void setInitializeCallback(Runnable initializeCallback) {
        MCWindow.initializeCallback = initializeCallback;
    }

    public static void setFrameCallback(IntConsumer frameCallback) {
        MCWindow.frameCallback = frameCallback;
    }

    public static void setEndCallback(Runnable endCallback) {
        MCWindow.endCallback = endCallback;
    }

    private static double fps;

    public static void start() {
        if (!glfwInit()) {
            throw new UnsupportedOperationException("GLFW could not initialize");
        }
        glfwSetErrorCallback(GLFWErrorCallback.createPrint());
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        ptr = glfwCreateWindow(750, 750, "Minecraft", 0, 0);
        glfwMakeContextCurrent(ptr);
        glfwSwapInterval(0);
        glfwShowWindow(ptr);
        GL.createCapabilities();
//        LOGGER.info(OPENGL, "GLFW Version: {}", glfwGetVersionString());
//        LOGGER.info(OPENGL, "OpenGL Vendor: {}", glGetString(GL_VENDOR));
//        LOGGER.info(OPENGL, "OpenGL Version: {}", glGetString(GL_VERSION));
//        LOGGER.info(OPENGL, "OpenGL Renderer: {}", glGetString(GL_RENDERER));
        int framecount = 0;
        initializeCallback.run();
        double lastTime = 0;
        int totalFramecount = 0;
        while (!glfwWindowShouldClose(ptr)) {
            double currentTime = glfwGetTime();
            double delta = currentTime - lastTime;
            framecount++;
            if (delta >= 1.0F) {
                fps = framecount / delta;
                framecount = 0;
                lastTime = currentTime;
            }
            glViewport(0, 0, getWidth(), getHeight());
            glClearColor(0, 0, 0, 1);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glEnable(GL_DEPTH_TEST);
            glDepthFunc(GL_LESS);

            frameCallback.accept(totalFramecount++);

            glfwPollEvents();
            glfwSwapBuffers(ptr);
        }
        endCallback.run();
        glfwTerminate();
    }

    public static double getFps() {
        return fps;
    }

    public static int getWidth() {
        int[] w = new int[1];
        int[] h = new int[1];
        glfwGetFramebufferSize(ptr, w, h);
        return w[0];
    }

    public static int getHeight() {
        int[] w = new int[1];
        int[] h = new int[1];
        glfwGetFramebufferSize(ptr, w, h);
        return h[0];
    }

    public static float time() {
        return (float) glfwGetTime();
    }

}
