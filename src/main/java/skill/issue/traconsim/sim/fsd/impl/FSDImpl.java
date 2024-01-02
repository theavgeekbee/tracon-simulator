package skill.issue.traconsim.sim.fsd.impl;

import skill.issue.traconsim.sim.fsd.IFSD;
import skill.issue.traconsim.sim.fsd.TickContext;
import skill.issue.traconsim.sim.objects.DataBlock;
import skill.issue.traconsim.sim.objects.Owner;
import skill.issue.traconsim.sim.objects.Position;
import skill.issue.traconsim.sim.ticking.TickData;
import skill.issue.traconsim.sim.ticking.TickingDataBlockInformation;
import skill.issue.traconsim.sim.utils.DBStatus;

import java.util.ArrayList;
import java.util.List;

public class FSDImpl implements IFSD {
    ArrayList<DataBlock> dataBlocks = new ArrayList<>(List.of(new DataBlock("N12345", 10000, 0, 250, 0, 0)));
    @Override
    public DataBlock[] getDataBlocks() {
        return dataBlocks.toArray(new DataBlock[0]);
    }

    @Override
    public Position[] getOnlinePositions() {
        return new Position[] {
            Position.CURRENT,
            new Position(Owner.APP, "HOU_81_CTR", "81"),
            new Position(Owner.TWR, "IAH_E_TWR", "ET")
        };
    }

    @Override
    public void doTick(TickContext ctx) {
        for (DataBlock db : dataBlocks) {
            TickingDataBlockInformation dbInfo = ctx.getDataBlock(db.callsign);
            for (TickData td : dbInfo.tickData()) {
                switch (td.dataType) {

                    case NO_UPDATE -> {}
                    case TGT_HEADING_CHANGE -> db.setHeading((double) td.data);
                    case TGT_ALTITUDE_CHANGE -> db.setAltitude((int) td.data);
                    case TGT_SPEED_CHANGE -> db.setSpeed((int) td.data);
                    case HANDOFF_INITIATE -> {
                        db.status = DBStatus.HO;
                        db.handoffTarget = (Owner) td.data;
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
                        db.pointoutTarget = (Owner) td.data;
                    }
                    case POINTOUT_ACKNOWLEDGE -> {
                        db.status = db.pointoutTarget == Owner.APP ? DBStatus.OWNED : DBStatus.UNOWNED;
                        db.owner = db.pointoutTarget;
                        db.pointoutTarget = Owner.NONE;
                    }
                    case HIGHLIGHT -> db.highlighted = !db.highlighted;
                    case TRACK_INITIATE -> {
                        db.status = DBStatus.OWNED;
                        db.owner = Owner.APP;
                    }
                    case TRACK_TERMINATE -> {
                        db.status = DBStatus.UNOWNED;
                        db.owner = Owner.NONE;
                    }
                }
            }
            db.update();
        }
    }
}
