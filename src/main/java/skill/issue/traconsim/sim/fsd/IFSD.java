package skill.issue.traconsim.sim.fsd;

import skill.issue.traconsim.sim.objects.DataBlock;

public interface IFSD {
    void initialize();
    void terminate();

    DataBlock[] getDataBlocks();
}
