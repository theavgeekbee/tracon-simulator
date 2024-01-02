package skill.issue.traconsim.sim.fsd;

import skill.issue.traconsim.sim.objects.DataBlock;
import skill.issue.traconsim.sim.objects.Position;

public interface IFSD {
    /* SUGGESTION FOR CUSTOM FSD: Don't use DataBlock.update(). Instead, dynamically update position by accessing the variable */
    DataBlock[] getDataBlocks();
    Position[] getOnlinePositions();
    void doTick(TickContext ctx);
}
