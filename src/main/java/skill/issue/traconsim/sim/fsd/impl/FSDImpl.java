package skill.issue.traconsim.sim.fsd.impl;

import skill.issue.traconsim.sim.fsd.IFSD;
import skill.issue.traconsim.sim.fsd.TickContext;
import skill.issue.traconsim.sim.objects.DataBlock;
import skill.issue.traconsim.sim.objects.Owner;
import skill.issue.traconsim.sim.ticking.TickData;
import skill.issue.traconsim.sim.ticking.TickingDataBlockInformation;
import skill.issue.traconsim.sim.utils.DBStatus;

public class FSDImpl implements IFSD {
    DataBlock[] dataBlocks = new DataBlock[]{
            new DataBlock("N12345", 10000, 0, 250, 0, 0),
    };
    @Override
    public DataBlock[] getDataBlocks() {
        return dataBlocks;
    }

    @Override
    public void doTick(TickContext ctx) {
        for (int i = 0; i < dataBlocks.length; i++) {
            DataBlock db = dataBlocks[i];
            TickingDataBlockInformation dbInfo = ctx.getDataBlock(db.callsign);
            for (TickData td : dbInfo.tickData()) {
                switch (td.dataType) {

                    case NO_UPDATE -> {}
                    case TGT_HEADING_CHANGE -> db.setHeading((double) td.data);
                    case TGT_ALTITUDE_CHANGE -> db.setAltitude((int) td.data);
                    case TGT_SPEED_CHANGE -> db.setSpeed((int) td.data);
                    case HANDOFF_INITIATE -> {
                        db.status = DBStatus.HO;
                        db.handoffTarget = Owner.valueOf((String) td.data);
                    }
                    case HANDOFF_ACCEPT -> {
                        db.status = db.handoffTarget == Owner.APP ? DBStatus.OWNED : DBStatus.UNOWNED;
                        db.owner = db.handoffTarget;
                        db.handoffTarget = Owner.NONE;
                    }
                    case HANDOFF_REFUSE -> {
                        db.status = db.owner == Owner.APP ? DBStatus.OWNED : DBStatus.UNOWNED;
                        db.handoffTarget = Owner.NONE;
                    }
                    case POINTOUT_INITIATE -> {
                        db.status = DBStatus.PO;
                        db.pointoutTarget = Owner.valueOf((String) td.data);
                    }
                    case POINTOUT_ACKNOWLEDGE -> {
                        db.status = db.pointoutTarget == Owner.APP ? DBStatus.OWNED : DBStatus.UNOWNED;
                        db.owner = db.pointoutTarget;
                        db.pointoutTarget = Owner.NONE;
                    }
                }
            }
            dataBlocks[i] = db.update();
        }
    }
}
