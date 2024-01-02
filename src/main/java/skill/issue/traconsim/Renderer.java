package skill.issue.traconsim;

import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector3d;
import org.lwjgl.glfw.GLFW;
import skill.issue.dim2d.VertexBufferBuilder;
import skill.issue.dim2d.VertexBuilder;
import skill.issue.dim2d.text.TextRenderer;
import skill.issue.traconsim.sim.fsd.FSDSupplier;
import skill.issue.traconsim.sim.fsd.TickContext;
import skill.issue.traconsim.sim.objects.DataBlock;
import skill.issue.traconsim.sim.objects.Position;
import skill.issue.traconsim.sim.ticking.DataType;
import skill.issue.traconsim.sim.ticking.EventStackItem;
import skill.issue.traconsim.sim.ticking.TickData;
import skill.issue.traconsim.sim.ticking.TickingDataBlockInformation;
import skill.issue.traconsim.sim.utils.Colors;
import skill.issue.traconsim.sim.utils.DBStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static org.lwjgl.opengl.GL11.*;
import static skill.issue.dim2d.Superimposition.*;

public class Renderer {
    private static long tick = 0;
    public static final double DB_SIZE = 5;
    public static Stack<EventStackItem> eventStack = new Stack<>();
    private static String typedText = "";
    public static void doRenderTick() {
        facilityRender();
        drawStrings();
        DataBlock[] dataBlocks = FSDSupplier.getDataBlocks();
        for (DataBlock db : dataBlocks) {
            renderDataBlock(db);
        }
        ArrayList<EventStackItem> events = new ArrayList<>();
        for (int i = 0; i < eventStack.size(); i++) {
            events.add(eventStack.pop());
        }
        TickingDataBlockInformation[] dbInfo = new TickingDataBlockInformation[dataBlocks.length];
        for (int i = 0; i < dataBlocks.length; i++) {
            ArrayList<TickData> tickData = new ArrayList<>();
            int finalI = i;
            if (events.stream().anyMatch(x -> dataBlocks[finalI].bb.doesCollide(x.position()))) {
                List<EventStackItem> items = events.stream().filter(x -> dataBlocks[finalI].bb.doesCollide(x.position())).toList();
                for (EventStackItem item : items) {
                    if (item.data().equals("HIGHLIGHT")) {
                        tickData.add(new TickData(DataType.HIGHLIGHT, null));
                    }
                    if (item.data().equals("SLEW")) {
                        if (dataBlocks[i].status == DBStatus.HO && dataBlocks[i].owner != Position.CURRENT.pos()) {
                            tickData.add(new TickData(DataType.HANDOFF_ACCEPT, null));
                        } else {
                            typedText += dataBlocks[i].callsign;
                        }
                    }
                    if (item.data().equals("KEY")) {
                        int key = item.key();
                        if (key == GLFW.GLFW_KEY_BACKSPACE) {
                            if (!typedText.isEmpty()) typedText = typedText.substring(0, typedText.length() - 1);
                        }
                        else if (key == GLFW.GLFW_KEY_ENTER) {
                            if (typedText.startsWith("IC ")) {
                                tickData.add(new TickData(DataType.TRACK_INITIATE, null));
                            }
                            typedText = "";
                        }
                        else if (key == GLFW.GLFW_KEY_F3) {
                            typedText += "IC ";
                        }
                        else if (key == GLFW.GLFW_KEY_F4) {
                            typedText += "TC ";
                        }
                        else {
                            typedText += (char) key;
                        }
                    }
                }
            }
            else
                tickData.add(new TickData(DataType.NO_UPDATE, null));
            dbInfo[i] = new TickingDataBlockInformation(dataBlocks[i].callsign, tickData.toArray(new TickData[0]));
        }
        TickContext ctx = new TickContext(tick++, new ArrayList<>(List.of(dbInfo)));
        FSDSupplier.doTick(ctx);
    }
    public static void drawStrings() {
        Vector2d mat = new Vector2d(-1,1).sub(0,0.03);
        TextRenderer.renderText(typedText, mat, new Vector2d(0.03 * typedText.length(),0.03), new Vector3d(1,1,1));
        mat.sub(0,0.1);
        TextRenderer.renderText("Online Positions", mat, new Vector2d(0.51,0.03), new Vector3d(1,1,1));
        mat.sub(0,0.04);
        String[] onlinePositions = Position.supplierToString(FSDSupplier::getOnlinePositions);
        for (String s : onlinePositions) {
            TextRenderer.renderText(s, mat, new Vector2d(0.03 * s.length(),0.03), s.substring(3).equals(Position.CURRENT.name()) ? new Vector3d(0,1,0) : new Vector3d(1,1,1));
            mat.sub(0,0.04);
        }

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
        glColor3f(1,1,1);
        Vector2d start = new Vector2d(db.x, db.y).div(200);
        Vector2d end = new Vector2d(db.x, db.y).add(5,5).div(200);
        glBegin(GL_LINES);
        glVertex2d(start.x, start.y);
        glVertex2d(end.x, end.y);
        glEnd();
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

        Vector3d color = Colors.NULL.getColor();
        switch (db.status) {

            case OWNED -> {
                color = Colors.OWNED.getColor();
                if (db.highlighted) color = Colors.HIGHLIGHT.getColor();
            }
            case UNOWNED -> color = Colors.UNOWNED.getColor();
            case PO -> color = Colors.PO.getColor();
            case HO -> {
                if (db.hoTicks % 50 < 25) {
                    color = Colors.HO_BLINK.getColor();
                } else {
                    color = Colors.OWNED.getColor();
                }
            }
        }
        switch (db.owner) {
            case NONE -> TextRenderer.renderText(
                    "*",
                    new Vector2d((db.x - DB_SIZE/2 - 0.03), (db.y - DB_SIZE/2 - 0.03)).div(200),
                    new Vector2d(0.03,0.03),
                    new Vector3d(1,1,1)
            );
            case CTR -> TextRenderer.renderText(
                    "C",
                    new Vector2d((db.x - DB_SIZE/2 - 0.03), (db.y - DB_SIZE/2 - 0.03)).div(200),
                    new Vector2d(0.03,0.03),
                    new Vector3d(1,1,1)
            );
            case TWR -> TextRenderer.renderText(
                    "T",
                    new Vector2d((db.x - DB_SIZE/2 - 0.03), (db.y - DB_SIZE/2 - 0.03)).div(200),
                    new Vector2d(0.03,0.03),
                    new Vector3d(1,1,1)
            );
            case APP -> TextRenderer.renderText(
                    "A",
                    new Vector2d((db.x - DB_SIZE/2 - 0.03), (db.y - DB_SIZE/2 - 0.03)).div(200),
                    new Vector2d(0.03,0.03),
                    new Vector3d(1,1,1)
            );
            case DEP -> TextRenderer.renderText(
                    "D",
                    new Vector2d((db.x - DB_SIZE/2 - 0.03), (db.y - DB_SIZE/2 - 0.03)).div(200),
                    new Vector2d(0.03,0.03),
                    new Vector3d(1,1,1)
            );
        }
        TextRenderer.renderText(
                db.callsign,
                new Vector2d(db.x,db.y).add(5,5).div(200),
                new Vector2d(0.03,0.03).mul(new Vector2d(db.callsign.length(), 1)),
                color
        );
        String str;
        if (tick % 200 <= 100) {
            str = String.valueOf(db.altitude).substring(0, 3) + " " + String.valueOf(db.speed).substring(0, 2);
        } else {
            str = db.destination + " " + db.type;
        }
        TextRenderer.renderText(
                str,
                new Vector2d(db.x,db.y).add(5,0).div(200),
                new Vector2d(0.03,0.03).mul(new Vector2d(str.length(), 1)),
                color
        );
    }
}
