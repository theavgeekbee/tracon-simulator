package skill.issue.traconsim.sim.fsd;

import skill.issue.traconsim.sim.objects.DataBlock;
import skill.issue.traconsim.sim.ticking.TickingDataBlockInformation;

import java.util.ArrayList;
import java.util.List;

public record TickContext(long tick, ArrayList<TickingDataBlockInformation> info) {
    public void checkPresence(List<DataBlock> dbs) {
        List<String> callSigns = dbs.stream().map(x -> x.callsign).toList();
        info.stream().map(TickingDataBlockInformation::dbRef).forEach(x -> {
            if (!callSigns.contains(x)) throw new IllegalStateException("Data block " + x + " not found in FSD data!");
        });
    }
    public TickingDataBlockInformation getDataBlock(String dbRef) {
        for (TickingDataBlockInformation db : info) {
            if (db.dbRef().equals(dbRef)) return db;
        }
        throw new IllegalStateException("Data block " + dbRef + " not found in FSD data!");
    }
}
