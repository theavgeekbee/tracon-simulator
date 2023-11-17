package skill.issue.traconsim.sim.fsd;

import skill.issue.traconsim.sim.objects.DataBlock;

import java.util.ArrayList;
import java.util.List;

public class FSDSupplier {
    private static IFSD fsd;

    public static void setFsd(IFSD fsd) {
        FSDSupplier.fsd = fsd;
    }

    public static DataBlock[] getDataBlocks() {
        if (fsd == null) throw new IllegalStateException("No valid FSD data supplier found!");
        return fsd.getDataBlocks();
    }
    public static void doTick(TickContext ctx) {
        if (fsd == null) throw new IllegalStateException("No valid FSD data supplier found!");
        ctx.checkPresence(List.of(fsd.getDataBlocks()));
        fsd.doTick(ctx);
    }
}
