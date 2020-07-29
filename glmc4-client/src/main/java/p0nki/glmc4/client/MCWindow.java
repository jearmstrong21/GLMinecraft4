package p0nki.glmc4.client;

import org.apache.commons.lang3.SystemUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import java.util.function.BiConsumer;
import java.util.function.IntConsumer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class MCWindow {

    private final static Logger LOGGER = LogManager.getLogger();
    private final static Marker OPENGL = MarkerManager.getMarker("OPENGL");
    private static long ptr;
    private static Runnable initializeCallback;
    private static IntConsumer frameCallback;
    private static BiConsumer<Double, Double> mouseMoveCallback;
    private static Runnable endCallback;
    private static double fps;

    private MCWindow() {

    }

    public static void setInitializeCallback(Runnable initializeCallback) {
        MCWindow.initializeCallback = initializeCallback;
    }

    public static void setMouseMoveCallback(BiConsumer<Double, Double> mouseMoveCallback) {
        MCWindow.mouseMoveCallback = mouseMoveCallback;
    }

    public static void setFrameCallback(IntConsumer frameCallback) {
        MCWindow.frameCallback = frameCallback;
    }

    public static void setEndCallback(Runnable endCallback) {
        MCWindow.endCallback = endCallback;
    }


    public static void start() {
        if (!glfwInit()) {
            throw new UnsupportedOperationException("GLFW could not initialize");
        }
        glfwSetErrorCallback(GLFWErrorCallback.createPrint());
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        if (SystemUtils.IS_OS_MAC_OSX) {
            glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        }
        ptr = glfwCreateWindow(750, 750, "Minecraft", 0, 0);
        glfwSetCursorPosCallback(ptr, new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double x, double y) {
                mouseMoveCallback.accept(x, y);
            }
        });
        glfwMakeContextCurrent(ptr);
        glfwSwapInterval(0);
        glfwShowWindow(ptr);
        GL.createCapabilities();
        LOGGER.info(OPENGL, "GLFW Version: {}", glfwGetVersionString());
        LOGGER.info(OPENGL, "OpenGL Vendor: {}", glGetString(GL_VENDOR));
        LOGGER.info(OPENGL, "OpenGL Version: {}", glGetString(GL_VERSION));
        LOGGER.info(OPENGL, "OpenGL Renderer: {}", glGetString(GL_RENDERER));
        int frameCount = 0;
        initializeCallback.run();
        double lastTime = 0;
        int totalFrameCount = 0;
        while (!glfwWindowShouldClose(ptr)) {
            double currentTime = time();
            double delta = currentTime - lastTime;
            frameCount++;
            if (delta >= 1.0F) {
                fps = frameCount / delta;
                frameCount = 0;
                lastTime = currentTime;
            }
            glViewport(0, 0, getWidth(), getHeight());
            glClearColor(0, 0, 0, 1);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glEnable(GL_DEPTH_TEST);
            glDepthFunc(GL_LESS);

            frameCallback.accept(totalFrameCount++);

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

    public static void captureMouse() {
        glfwSetInputMode(ptr, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
    }

    public static void releaseMouse() {
        glfwSetInputMode(ptr, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
    }

    public static boolean getKey(char ch) {
        return glfwGetKey(ptr, ch) == GLFW_PRESS;
    }

    public static float time() {
        return (float) glfwGetTime();
    }

}
