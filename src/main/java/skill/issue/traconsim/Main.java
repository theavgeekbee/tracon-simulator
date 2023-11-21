package skill.issue.traconsim;

import org.joml.Vector2d;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import skill.issue.dim2d.Superimposition;
import skill.issue.dim2d.text.TextRenderer;
import skill.issue.dim2d.text.VRCFont;
import skill.issue.traconsim.sim.fsd.FSDSupplier;
import skill.issue.traconsim.sim.fsd.impl.FSDImpl;
import skill.issue.traconsim.sim.objects.Owner;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Main {
    public static final Owner POSITION = Owner.APP;
    private static long win;
    public static final double ASPECT_RATIO = 4/3d;
    public static final int HEIGHT = 1000;
    public static final int WIDTH = (int) (HEIGHT * ASPECT_RATIO);
    public static void init() throws Exception {
        System.out.println("Initiating GLFW and GL");
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit()) throw new IllegalStateException("Unable to initialize GLFW");
        win = glfwCreateWindow(WIDTH,HEIGHT, "theaviationbee's TRACON Simulator", 0, 0);
        glfwMakeContextCurrent(win);
        glfwSwapInterval(1);
        glfwShowWindow(win);

        System.out.println("Initiating Superimposition rendering engine");
        Superimposition.init(200);
        System.out.println("Generating bitmaps for text rendering");
        TextRenderer.init();
        FSDSupplier.setFsd(new FSDImpl());
    }
    public static void loop() {
        GL.createCapabilities();
        glClearColor(0,0,0,1);

        glfwSetMouseButtonCallback(win, (win, button, action, mods) -> {
            if (button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_PRESS) {
                double[] x = new double[1];
                double[] y = new double[1];
                glfwGetCursorPos(win, x, y);
                double x2 = x[0] / WIDTH * 2 - 1;
                double y2 = y[0] / HEIGHT * 2 - 1;
                Renderer.eventStack.push(new Vector2d(x2, y2));
            }
        });

        while (!glfwWindowShouldClose(win)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            Renderer.doRenderTick();

            glfwSwapBuffers(win);
            glfwPollEvents();
        }
    }
    public static void shutdown() {
        System.out.println("Shutting down GLFW and GL");
        glfwTerminate();
        glfwSetErrorCallback(null);
        VRCFont.cleanupBitmaps();
        TextRenderer.cleanupAlias();
    }
    public static void main(String[] args) throws Exception {
        init();
        loop();
        shutdown();
    }
}
