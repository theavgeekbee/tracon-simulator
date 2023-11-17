package skill.issue.traconsim.sim.utils;

import org.joml.Vector3d;

public enum Colors {
    UNOWNED(new Vector3d(0,252/255d,0)),
    OWNED(new Vector3d(1,1,1)),
    PO(new Vector3d(1,1,0)),
    HO_BLINK(new Vector3d(0.5,0.5,0.5)),
    NULL(new Vector3d(0,0,0))
    ;
    Vector3d color;
    Colors(Vector3d color) {
        this.color = color;
    }
    public Vector3d getColor() {
        return color;
    }
}
