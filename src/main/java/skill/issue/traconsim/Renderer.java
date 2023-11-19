package skill.issue.traconsim;

import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector3d;
import skill.issue.dim2d.VertexBufferBuilder;
import skill.issue.dim2d.VertexBuilder;
import skill.issue.dim2d.text.TextRenderer;
import skill.issue.traconsim.sim.fsd.FSDSupplier;
import skill.issue.traconsim.sim.fsd.TickContext;
import skill.issue.traconsim.sim.objects.DataBlock;
import skill.issue.traconsim.sim.ticking.DataType;
import skill.issue.traconsim.sim.ticking.TickData;
import skill.issue.traconsim.sim.ticking.TickingDataBlockInformation;
import skill.issue.traconsim.sim.utils.Colors;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static skill.issue.dim2d.Superimposition.*;

public class Renderer {
    private static long tick = 0;
    private static final double DB_SIZE = 4;
    public static void doRenderTick() {
        facilityRender();
        DataBlock[] dataBlocks = FSDSupplier.getDataBlocks();
        for (DataBlock db : dataBlocks) {
            renderDataBlock(db);
        }
        TickingDataBlockInformation[] dbInfo = new TickingDataBlockInformation[dataBlocks.length];
        for (int i = 0; i < dataBlocks.length; i++) {
            dbInfo[i] = new TickingDataBlockInformation(dataBlocks[i].callsign, new TickData[]{
                    new TickData(DataType.NO_UPDATE, null)
            });
        }
        TickContext ctx = new TickContext(tick++, new ArrayList<>(List.of(dbInfo)));
        FSDSupplier.doTick(ctx);
    }
    public static void facilityRender() {
        pushMatrix();
        setLineWidth(0.1f);
        VertexBufferBuilder builder = getBufferBuilder(RenderType.CIRCLE);
        builder.append(
                VertexBuilder.alloc().pos(0,0).color(0.5,0.5,0.5).endVertex()
        );
        superimposeBuffer(builder.end(), 50d);

        builder = getBufferBuilder(RenderType.CIRCLE);
        builder.append(
                VertexBuilder.alloc().pos(0,0).color(0.5,0.5,0.5).endVertex()
        );
        superimposeBuffer(builder.end(), 100d);

        builder = getBufferBuilder(RenderType.CIRCLE);
        builder.append(
                VertexBuilder.alloc().pos(0,0).color(0.5,0.5,0.5).endVertex()
        );
        superimposeBuffer(builder.end(), 150d);

        popMatrix();

        pushMatrix();
        rotate$d(80);

        builder = getBufferBuilder(RenderType.LINE);
        builder.append(
                VertexBuilder.alloc().pos(5,0).color(0.5,0.5,0.5).endVertex()
        );
        builder.append(
                VertexBuilder.alloc().pos(5, 10).color(0.5,0.5,0.5).endVertex()
        );
        superimposeBuffer(builder.end());

        builder = getBufferBuilder(RenderType.LINE);
        builder.append(
                VertexBuilder.alloc().pos(5,20).color(0.5,0.5,0.5).endVertex()
        );
        builder.append(
                VertexBuilder.alloc().pos(5, 30).color(0.5,0.5,0.5).endVertex()
        );
        superimposeBuffer(builder.end());

        builder = getBufferBuilder(RenderType.LINE);
        builder.append(
                VertexBuilder.alloc().pos(5,40).color(0.5,0.5,0.5).endVertex()
        );
        builder.append(
                VertexBuilder.alloc().pos(5, 50).color(0.5,0.5,0.5).endVertex()
        );
        superimposeBuffer(builder.end());

        builder = getBufferBuilder(RenderType.LINE);
        builder.append(
                VertexBuilder.alloc().pos(5,60).color(0.5,0.5,0.5).endVertex()
        );
        builder.append(
                VertexBuilder.alloc().pos(5, 70).color(0.5,0.5,0.5).endVertex()
        );
        superimposeBuffer(builder.end());

        builder = getBufferBuilder(RenderType.LINE);
        builder.append(
                VertexBuilder.alloc().pos(10,70).color(0.5,0.5,0.5).endVertex()
        );
        builder.append(
                VertexBuilder.alloc().pos(0, 70).color(0.5,0.5,0.5).endVertex()
        );
        superimposeBuffer(builder.end());

        popMatrix();

        pushMatrix();
        rotate$d(80);

        builder = getBufferBuilder(RenderType.LINE);
        builder.append(
                VertexBuilder.alloc().pos(-5,0).color(0.5,0.5,0.5).endVertex()
        );
        builder.append(
                VertexBuilder.alloc().pos(-5, 10).color(0.5,0.5,0.5).endVertex()
        );
        superimposeBuffer(builder.end());

        builder = getBufferBuilder(RenderType.LINE);
        builder.append(
                VertexBuilder.alloc().pos(-5,20).color(0.5,0.5,0.5).endVertex()
        );
        builder.append(
                VertexBuilder.alloc().pos(-5, 30).color(0.5,0.5,0.5).endVertex()
        );
        superimposeBuffer(builder.end());

        builder = getBufferBuilder(RenderType.LINE);
        builder.append(
                VertexBuilder.alloc().pos(-5,40).color(0.5,0.5,0.5).endVertex()
        );
        builder.append(
                VertexBuilder.alloc().pos(-5, 50).color(0.5,0.5,0.5).endVertex()
        );
        superimposeBuffer(builder.end());

        builder = getBufferBuilder(RenderType.LINE);
        builder.append(
                VertexBuilder.alloc().pos(-5,60).color(0.5,0.5,0.5).endVertex()
        );
        builder.append(
                VertexBuilder.alloc().pos(-5, 70).color(0.5,0.5,0.5).endVertex()
        );
        superimposeBuffer(builder.end());

        builder = getBufferBuilder(RenderType.LINE);
        builder.append(
                VertexBuilder.alloc().pos(0,70).color(0.5,0.5,0.5).endVertex()
        );
        builder.append(
                VertexBuilder.alloc().pos(-10, 70).color(0.5,0.5,0.5).endVertex()
        );
        superimposeBuffer(builder.end());

        popMatrix();
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
                VertexBuilder.alloc().pos(0, 15).color(1,1,1).endVertex()
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
        Vector2d start = new Vector2d(db.x, db.y).div(200);
        Vector2d end = new Vector2d(db.x, db.y).add(5,5).div(200);
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
                new Vector2d(db.x,db.y).add(5,5).div(200),
                new Vector2d(0.03,0.03).mul(new Vector2d(db.callsign.length(), 1)),
                color
        );
    }
}
