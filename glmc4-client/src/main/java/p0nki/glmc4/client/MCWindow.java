package p0nki.glmc4.client;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.GL_MAJOR_VERSION;
import static org.lwjgl.opengl.GL30.GL_MINOR_VERSION;

public class MCWindow {

    private MCWindow() {

    }

    private static long ptr;

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
        GL.createCapabilities();
        System.out.println(glfwGetVersionString());
        System.out.println(glGetInteger(GL_MAJOR_VERSION));
        System.out.println(glGetInteger(GL_MINOR_VERSION));
        System.out.println(glGetString(GL_VENDOR));
        System.out.println(glGetString(GL_RENDERER));
        while (!glfwWindowShouldClose(ptr)) {
            glClearColor(0, 0, 0, 1);
            glClear(GL_COLOR_BUFFER_BIT);

            glfwSwapBuffers(ptr);
            glfwPollEvents();
        }
    }

}
