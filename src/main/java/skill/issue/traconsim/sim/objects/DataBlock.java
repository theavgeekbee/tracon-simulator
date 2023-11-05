package skill.issue.traconsim.sim.objects;

import javax.xml.crypto.Data;

public class DataBlock {
    public String callsign;
    public int altitude;
    public double heading;
    public int speed;
    public double x;
    public double y;
    public int assignedAltitude;
    public double assignedHeading;

    public DataBlock(String callsign, int altitude, double heading, int speed, double x, double y) {
        this.callsign = callsign;
        this.altitude = altitude;
        this.heading = heading;
        this.speed = speed;
        this.x = x;
        this.y = y;

        assignedAltitude = altitude;
        assignedHeading = heading;
    }

    public void setHeading(double heading) {
        this.assignedHeading = heading;
    }
    public void update() {
        heading += (assignedHeading - heading) / 50;
        if (assignedHeading-heading < 2) heading = assignedHeading;

        x += speed * Math.sin(Math.toRadians(heading)) * 0.0001;
        y += speed * Math.cos(Math.toRadians(heading)) * 0.0001;
    }
}
