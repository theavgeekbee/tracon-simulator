package skill.issue.dim2d;

public record Vertex(double x, double y, double r, double g, double b) {
    boolean isInitialized() {
        return r == -1 || g == -1 || b == -1;
    }
}
