package skill.issue.dim2d;

import org.joml.Vector2f;

import static org.lwjgl.opengl.GL11.*;

public class Superimposition {
    public enum RenderType {
        LINE(2),QUAD(4);

        final int vertices;
        RenderType(int vertices) {
            this.vertices = vertices;
        }
    }
    private static int dimension = -1;
    private static boolean matrixState = false;
    private static Vector2f matrixTranslation = new Vector2f(0,0);
    private static double matrixRotation = 0;

    public static void init(int dimension) {
        Superimposition.dimension = dimension;
    }

    private static void checkDimension() {
        if (dimension < 0) throw new IllegalStateException("Superimposition has not been initialized");
    }

    protected static Vector2f project(Vector2f v) {
        checkDimension();
        float halfDimension = 1f/dimension;
        return v.mul(halfDimension);
    }
    public static VertexBufferBuilder getBufferBuilder(RenderType type) {
        checkDimension();
        return new VertexBufferBuilder(type.vertices);
    }
    public static void pushMatrix() {
        checkDimension();
        matrixState = true;
    }
    public static void popMatrix() {
        checkDimension();
        matrixState = false;
        matrixTranslation = new Vector2f(0,0);
        matrixRotation = 0;
    }
    public static void translate$f(Vector2f vec) {
        checkDimension();
        if (!matrixState) matrixTranslation.add(vec);
        else matrixTranslation = vec;
    }
    public static void rotate$d(double deg) {
        checkDimension();
        if (!matrixState) matrixRotation += deg;
        else matrixRotation = deg;
    }
    public static void superimposeBuffer(VertexBufferBuilder.FinishedVertexBuffer b) {
        checkDimension();
        VertexBuilder.FinishedVertex[] finishedVertices = b.getBuffer();
        Vertex[] v = new Vertex[finishedVertices.length];
        for (int i = 0; i < finishedVertices.length; i++) {
            v[i] = finishedVertices[i].vertex();
        }
        glPushMatrix();
        Vector2f normTranslation = matrixState ? project(matrixTranslation) : matrixTranslation;
        glTranslatef(normTranslation.x,  normTranslation.y, 0);
        glRotated(matrixRotation,0,0,1);
        if (finishedVertices.length == 2) glBegin(GL_LINES);
        else if (finishedVertices.length == 4) glBegin(GL_QUADS);
        else throw new IllegalArgumentException("Invalid number of vertices");
        for (Vertex vtx : v) {
            if (vtx.isInitialized()) throw new IllegalStateException("Attempted to superimpose buffer with uninitialized vertex");
            Vector2f pos = new Vector2f((float) vtx.x(), (float) vtx.y());
            if (matrixState) pos = project(pos);
            glColor3d(vtx.r(), vtx.g(), vtx.b());
            glVertex2f(pos.x, pos.y);
        }
        glEnd();
        glPopMatrix();
    }
    public static void setLineWidth(float width) {
        checkDimension();
        glLineWidth(width);
    }
}
