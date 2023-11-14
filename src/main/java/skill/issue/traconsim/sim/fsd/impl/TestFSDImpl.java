package skill.issue.traconsim.sim.fsd.impl;

import skill.issue.traconsim.sim.fsd.IFSD;
import skill.issue.traconsim.sim.objects.DataBlock;

import java.util.ArrayList;

public class TestFSDImpl implements IFSD {
    ArrayList<DataBlock> dataBlocks = new ArrayList<>();
    @Override
    public void initialize() {
        dataBlocks.add(new DataBlock("TEST", 10000, 0, 250, 0, 0));
    }

    @Override
    public void terminate() {

    }

    @Override
    public DataBlock[] getDataBlocks() {
        return new DataBlock[0];
    }
}
