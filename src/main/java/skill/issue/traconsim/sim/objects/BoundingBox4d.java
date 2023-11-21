package skill.issue.traconsim.sim.objects;

import org.joml.Vector2d;

public record BoundingBox4d(Vector2d minXMinY, Vector2d maxXMinY, Vector2d maxXMaxY, Vector2d minXMaxY) {
    public boolean doesCollide(Vector2d pos) {
        return pos.x >= minXMinY.x && pos.x <= maxXMinY.x && pos.y >= minXMinY.y && pos.y <= minXMaxY.y;
    }
}
