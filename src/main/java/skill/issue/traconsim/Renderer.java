package skill.issue.traconsim;

import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector3d;
import skill.issue.dim2d.VertexBufferBuilder;
import skill.issue.dim2d.VertexBuilder;
import skill.issue.dim2d.text.TextRenderer;
import skill.issue.traconsim.sim.fsd.FSDPipelineSupplier;
import skill.issue.traconsim.sim.objects.DataBlock;
import skill.issue.traconsim.sim.utils.Colors;

import static org.lwjgl.opengl.GL11.*;
import static skill.issue.dim2d.Superimposition.*;

public class Renderer {
    private static final double DB_SIZE = 2;
    public static void doRenderTick() {
        DataBlock[] dataBlocks = FSDPipelineSupplier.getDataBlocks();
        for (DataBlock db : dataBlocks) {
            renderDataBlock(db);
        }
    }
    public static void renderDataBlock(DataBlock db) {
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

        Vector3d color = Colors.NULL.getColor();
        switch (db.status) {

            case OWNED -> {
                color = Colors.OWNED.getColor();
            }
            case UNOWNED -> {
                color = Colors.UNOWNED.getColor();
            }
            case PO -> {
                color = Colors.PO.getColor();
            }
            case HO -> {
                if (db.hoTicks % 50 < 25) {
                    color = Colors.HO_BLINK.getColor();
                } else {
                    color = Colors.OWNED.getColor();
                }
            }
        }
        TextRenderer.renderText(
                db.callsign,
                new Vector2d(db.x,db.y).add(5,5).div(100),
                new Vector2d(0.05,0.05).mul(new Vector2d(db.callsign.length(), 1)),
                color
        );
    }
}
