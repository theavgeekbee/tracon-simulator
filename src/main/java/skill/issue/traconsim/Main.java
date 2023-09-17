package skill.issue.traconsim;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import skill.issue.dim2d.Superimposition;
import skill.issue.dim2d.text.TextRenderer;

import java.io.File;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Main {
    private static long win;
    public static void init() {
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit()) throw new IllegalStateException("Unable to initialize GLFW");
        win = glfwCreateWindow(800,600, "theaviationbee's TRACON Simulator", 0, 0);
        glfwMakeContextCurrent(win);
        glfwSwapInterval(1);
        glfwShowWindow(win);

        Superimposition.init(100);
    }
    public static void loop() {
        GL.createCapabilities();
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        while (!glfwWindowShouldClose(win)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            Renderer.renderTick();

            TextRenderer textRenderer = new TextRenderer();
            textRenderer.init("/VRC.ttf");
            textRenderer.renderText("Hello World!", 0, 0, 1);

            glfwSwapBuffers(win);
            glfwPollEvents();
        }
    }
    public static void shutdown() {
        glfwTerminate();
        glfwSetErrorCallback(null);
    }
    public static void main(String[] args) {
        init();
        loop();
        shutdown();
    }
}
