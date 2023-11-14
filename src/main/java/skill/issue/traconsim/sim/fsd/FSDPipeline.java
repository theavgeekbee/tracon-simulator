package skill.issue.traconsim.sim.fsd;

import java.util.ArrayList;

public class FSDPipeline {
    protected static IFSD fsd;
    protected static void setFsd(IFSD fsd) {
        FSDPipeline.fsd = fsd;
    }
    private static void checkFsd() {
        if (fsd == null) {
            throw new IllegalStateException("FSD not initialized");
        }
    }
    public static void loadFsd() {
        checkFsd();
        fsd.initialize();
    }
    public static void unloadFsd() {
        checkFsd();
        fsd.terminate();
    }

}
