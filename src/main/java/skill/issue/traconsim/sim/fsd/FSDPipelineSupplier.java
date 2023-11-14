package skill.issue.traconsim.sim.fsd;

import skill.issue.traconsim.sim.objects.DataBlock;

public class FSDPipelineSupplier {
    public static void initializeFSD(IFSD impl) {
        FSDPipeline.setFsd(impl);
        FSDPipeline.loadFsd();
    }
    public static void terminateFSD() {
        FSDPipeline.unloadFsd();
    }
    public static DataBlock[] getDataBlocks() {
        return FSDPipeline.fsd.getDataBlocks();
    }
}
