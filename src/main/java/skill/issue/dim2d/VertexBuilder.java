package skill.issue.dim2d;

public class VertexBuilder {
    private Vertex curr = new Vertex(-1,-1,-1,-1,-1);
    public static VertexBuilder alloc() {
        return new VertexBuilder();
    }
    private void verifyVertex() {
        if (curr == null) throw new IllegalStateException("VertexBuilder has not been initialized");
    }
    public VertexBuilder pos(double x, double y) {
        verifyVertex();
        curr = new Vertex(x, y, curr.r(), curr.g(), curr.b());
        return this;
    }
    public VertexBuilder color(double r, double g, double b) {
        verifyVertex();
        curr = new Vertex(curr.x(), curr.y(), r, g, b);
        return this;
    }
    public FinishedVertex endVertex() {
        if (curr.isInitialized()) throw new IllegalStateException("Attempted to end vertex that is not complete");
        FinishedVertex finished = new FinishedVertex(curr);
        curr = null;
        return finished;
    }
    public static class FinishedVertex {
        private final Vertex vertex;
        private FinishedVertex(Vertex vertex) {
            this.vertex = vertex;
        }
        protected Vertex vertex() {
            return vertex;
        }
    }
}
