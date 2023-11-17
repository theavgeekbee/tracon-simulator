package skill.issue.traconsim.sim.fsd;

import skill.issue.traconsim.sim.objects.DataBlock;

public interface IFSD {
    DataBlock[] getDataBlocks();
    void doTick(TickContext ctx);
}
