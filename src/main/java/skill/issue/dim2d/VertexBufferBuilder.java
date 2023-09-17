package skill.issue.dim2d;

public class VertexBufferBuilder {
    private final VertexBuilder.FinishedVertex[] buf;
    private int ptr = 0;
    protected VertexBufferBuilder(int size) {
        buf = new VertexBuilder.FinishedVertex[size];
    }
    public void append(VertexBuilder.FinishedVertex v) {
        buf[ptr++] = v;
    }
    public FinishedVertexBuffer end() {
        return new FinishedVertexBuffer();
    }
    public class FinishedVertexBuffer {
        protected VertexBuilder.FinishedVertex[] getBuffer() {
            return buf;
        }
    }
}
