package skill.issue.traconsim.sim.fsd;

//todo : implement
public class TickContext {
    public record TickData(String callsign, int deltaAltitude, int newHeading, int newSpeed) {

    }
    public static TickData[] getTickData() {
        return new TickData[0];
    }
}
