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
import skill.issue.traconsim.sim.ticking.EventStackItem;
import skill.issue.traconsim.sim.utils.DBStatus;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Main {
    public static final Owner POSITION = Owner.APP;
    public static final String POSITION_STRING = "I90_D_APP";
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

        /*
        vSTARS keybinds:
        F3: Initiate track
        F4: Terminate Track
        F6: View flight plan
        Type target controller id in box, then slew track: handoff
        Slew track: accept handoff
        Slew track: acknowledge pointout
        U then slew track: refuse handoff
        Type target controller id, then *: send pointout
        type plus, then three numbers, then slew track: enter target altitude
        type 2 pluses, then three numbers, then slew track: enter target heading
        middle click: highlight owned track
         */
        glfwSetMouseButtonCallback(win, (win, button, action, mods) -> {
            if (button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_PRESS) {
                double[] x = new double[1];
                double[] y = new double[1];
                glfwGetCursorPos(win, x, y);
                double x2 = x[0] / WIDTH * 2 - 1;
                double y2 = y[0] / HEIGHT * 2 - 1;
                Renderer.eventStack.push(new EventStackItem(new Vector2d(x2, y2), "SLEW", -1));
            }
            if (button == GLFW_MOUSE_BUTTON_MIDDLE && action == GLFW_PRESS) {
                double[] x = new double[1];
                double[] y = new double[1];
                glfwGetCursorPos(win, x, y);
                double x2 = x[0] / WIDTH * 2 - 1;
                double y2 = y[0] / HEIGHT * 2 - 1;
                Renderer.eventStack.push(new EventStackItem(new Vector2d(x2, y2), "HIGHLIGHT", -1));
            }
        });
        glfwSetKeyCallback(win, (win, key, scancode, action, mods) -> {
            if (action == GLFW_PRESS) {
                Renderer.eventStack.push(new EventStackItem(new Vector2d(0,0), "KEY", key));
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
