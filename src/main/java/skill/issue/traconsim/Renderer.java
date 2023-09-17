package skill.issue.traconsim;

import org.joml.Vector2f;
import skill.issue.dim2d.VertexBufferBuilder;
import skill.issue.dim2d.VertexBuilder;
import skill.issue.traconsim.sim.objects.DataBlock;

import static skill.issue.dim2d.Superimposition.*;
import static org.lwjgl.opengl.GL11.*;

public class Renderer {
    private static long ticks = 0;
    private static final double DB_SIZE = 1.5;
    private static DataBlock db = new DataBlock("AAL123", 10000, 90, 250, 50,50);
    public static void renderTick() {
        beforeRenderTick();
        ticks++;
        atRenderTick();
    }

    public static void beforeRenderTick() {
        renderAirport();
    }
    public static void atRenderTick() {
        db.setHeading(180);
        db.update();
        renderDataBlock(db);
    }
    public static void renderAirport() {
        pushMatrix();
        setLineWidth(1);

        VertexBufferBuilder buf = getBufferBuilder(RenderType.LINE);
        buf.append(VertexBuilder.alloc().pos(-3,5).color(0.5,0.5,0.5).endVertex());
        buf.append(VertexBuilder.alloc().pos(3,5).color(0.5,0.5,0.5).endVertex());
        superimposeBuffer(buf.end());
        buf = getBufferBuilder(RenderType.LINE);
        buf.append(VertexBuilder.alloc().pos(-3,-5).color(0.5,0.5,0.5).endVertex());
        buf.append(VertexBuilder.alloc().pos(3,-5).color(0.5,0.5,0.5).endVertex());
        superimposeBuffer(buf.end());

        buf = getBufferBuilder(RenderType.LINE);
        buf.append(VertexBuilder.alloc().pos(-8,5).color(1,1,1).endVertex());
        buf.append(VertexBuilder.alloc().pos(-13,5).color(1,1,1).endVertex());
        superimposeBuffer(buf.end());
        buf = getBufferBuilder(RenderType.LINE);
        buf.append(VertexBuilder.alloc().pos(-18,5).color(1,1,1).endVertex());
        buf.append(VertexBuilder.alloc().pos(-23,5).color(1,1,1).endVertex());
        superimposeBuffer(buf.end());
        buf = getBufferBuilder(RenderType.LINE);
        buf.append(VertexBuilder.alloc().pos(-28,5).color(1,1,1).endVertex());
        buf.append(VertexBuilder.alloc().pos(-33,5).color(1,1,1).endVertex());
        superimposeBuffer(buf.end());

        buf = getBufferBuilder(RenderType.LINE);
        buf.append(VertexBuilder.alloc().pos(-33,3).color(1,1,1).endVertex());
        buf.append(VertexBuilder.alloc().pos(-33,7).color(1,1,1).endVertex());
        superimposeBuffer(buf.end());

        buf = getBufferBuilder(RenderType.LINE);
        buf.append(VertexBuilder.alloc().pos(-8,-5).color(1,1,1).endVertex());
        buf.append(VertexBuilder.alloc().pos(-13,-5).color(1,1,1).endVertex());
        superimposeBuffer(buf.end());
        buf = getBufferBuilder(RenderType.LINE);
        buf.append(VertexBuilder.alloc().pos(-18,-5).color(1,1,1).endVertex());
        buf.append(VertexBuilder.alloc().pos(-23,-5).color(1,1,1).endVertex());
        superimposeBuffer(buf.end());
        buf = getBufferBuilder(RenderType.LINE);
        buf.append(VertexBuilder.alloc().pos(-28,-5).color(1,1,1).endVertex());
        buf.append(VertexBuilder.alloc().pos(-33,-5).color(1,1,1).endVertex());
        superimposeBuffer(buf.end());

        buf = getBufferBuilder(RenderType.LINE);
        buf.append(VertexBuilder.alloc().pos(-33,-3).color(1,1,1).endVertex());
        buf.append(VertexBuilder.alloc().pos(-33,-7).color(1,1,1).endVertex());
        superimposeBuffer(buf.end());

        popMatrix();
    }
    public static void renderDataBlock(DataBlock db) {
        pushMatrix();
        translate$f(new Vector2f((float) db.x, (float) db.y));
        rotate$d(-db.heading);

        VertexBufferBuilder buf = getBufferBuilder(RenderType.LINE);
        buf.append(VertexBuilder.alloc().pos(0,0).color(0.5,0.5,0.5).endVertex());
        buf.append(VertexBuilder.alloc().pos(0, 10).color(0.5,0.5,0.5).endVertex());
        superimposeBuffer(buf.end());

        popMatrix();
        pushMatrix();
        translate$f(new Vector2f((float) db.x, (float) db.y));
        rotate$d(-db.heading);

        buf = getBufferBuilder(RenderType.QUAD);
        buf.append(VertexBuilder.alloc().pos(DB_SIZE,DB_SIZE).color(0,0,1).endVertex());
        buf.append(VertexBuilder.alloc().pos(-DB_SIZE, DB_SIZE).color(0,0,1).endVertex());
        buf.append(VertexBuilder.alloc().pos(-DB_SIZE, -DB_SIZE).color(0,0,1).endVertex());
        buf.append(VertexBuilder.alloc().pos(DB_SIZE,-DB_SIZE).color(0,0,1).endVertex());
        superimposeBuffer(buf.end());

        popMatrix();
    }
}
