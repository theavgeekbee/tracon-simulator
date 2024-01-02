package skill.issue.traconsim.sim.objects;

import java.util.function.Supplier;

public record Position(Owner pos, String name, String id) {
    public static final Position CURRENT = new Position(Owner.APP, "I90_D_APP", "1D");
    public static String[] supplierToString(Supplier<Position[]> p) {
        Position[] pos = p.get();
        String[] ret = new String[pos.length];
        for (int i = 0; i < pos.length; i++) {
            ret[i] = pos[i].id + " " + pos[i].name;
        }
        return ret;
    }
}
