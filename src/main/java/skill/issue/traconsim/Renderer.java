package skill.issue.traconsim;

import org.joml.Vector2d;
import org.joml.Vector2f;
import skill.issue.dim2d.Superimposition;
import skill.issue.dim2d.Vertex;
import skill.issue.dim2d.VertexBufferBuilder;
import skill.issue.dim2d.VertexBuilder;
import skill.issue.dim2d.text.TextRenderer;
import skill.issue.traconsim.sim.objects.DataBlock;

import java.util.Vector;

import static skill.issue.dim2d.Superimposition.*;
import static org.lwjgl.opengl.GL11.*;

public class Renderer {
    private static final double DB_SIZE = 2;
    private static final DataBlock db = new DataBlock("AAL123", 10000, 90, 250, 50,50);

    public static void doRenderTick() {
        preRenderTick();
        renderTick();
    }

    public static void preRenderTick() {
        db.update();
    }
    public static void renderTick() {
        pushMatrix();
        translate$f(new Vector2f((float) db.x, (float) db.y));
        rotate$d(db.heading);

        VertexBufferBuilder builder = getBufferBuilder(RenderType.LINE);
        builder.append(
                VertexBuilder.alloc().pos(0,0).color(1,1,1).endVertex()
        );
        builder.append(
                VertexBuilder.alloc().pos(0, -15).color(1,1,1).endVertex()
        );
        superimposeBuffer(builder.end());

        popMatrix();
        pushMatrix();
        translate$f(new Vector2f((float) db.x, (float) db.y));
        rotate$d(db.heading);

        builder = getBufferBuilder(RenderType.QUAD);
        builder.append(
                VertexBuilder.alloc().pos(-DB_SIZE/2, -DB_SIZE/2).color(0,0,1).endVertex()
        );
        builder.append(
                VertexBuilder.alloc().pos(DB_SIZE/2, -DB_SIZE/2).color(0,0,1).endVertex()
        );
        builder.append(
                VertexBuilder.alloc().pos(DB_SIZE/2, DB_SIZE/2).color(0,0,1).endVertex()
        );
        builder.append(
                VertexBuilder.alloc().pos(-DB_SIZE/2, DB_SIZE/2).color(0,0,1).endVertex()
        );
        superimposeBuffer(builder.end());

        popMatrix();

        glColor3f(1,1,1);
        Vector2d start = new Vector2d(db.x, db.y).div(100);
        Vector2d end = new Vector2d(db.x, db.y).add(5,5).div(100);
        glBegin(GL_LINES);
        glVertex2d(start.x, start.y);
        glVertex2d(end.x, end.y);
        glEnd();
        TextRenderer.renderText(db.callsign, new Vector2d(db.x,db.y).add(5,5).div(100), new Vector2d(0.05,0.05).mul(new Vector2d(db.callsign.length(), 1)));
    }
}
