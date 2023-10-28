package skill.issue.traconsim;

import org.joml.Vector2d;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import skill.issue.dim2d.Superimposition;
import skill.issue.dim2d.text.TextRenderer;

import java.io.File;
import java.io.IOException;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_MULTISAMPLE;

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
    public static void loop() throws IOException {
        GL.createCapabilities();
        TextRenderer.init();
        glClearColor(0,0,0,1);
        while (!glfwWindowShouldClose(win)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            System.out.println("Before render text");
            TextRenderer.renderText("Hello World!", new Vector2d(0,0), new Vector2d(0.1,0.1));
            System.out.println("After render text");

            glfwSwapBuffers(win);
            glfwPollEvents();
        }
    }
    public static void shutdown() {
        glfwTerminate();
        glfwSetErrorCallback(null);
    }
    public static void main(String[] args) throws IOException {
        init();
        loop();
        shutdown();
    }
}
