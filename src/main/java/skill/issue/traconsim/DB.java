package skill.issue.traconsim;

public record DB(double x, double y, double heading, int speed, int alt, int vs, boolean owned, Main.Owner owner, int xpdr, boolean ho, int tgtAlt, int tgtSpd, boolean app, boolean departure, Main.Owner hoTgt) {
    public int nextVS() {
        if (alt+vs/10 <= tgtAlt && vs < 0) return 0;
        else if (alt+vs/10 >= tgtAlt && vs > 0) return 0;
        else if (alt != tgtAlt && vs == 0) return  ((tgtAlt - alt) / Math.abs(tgtAlt - alt))*10;
        else return vs;
    }
    public int nextSpd() {
        if (x >= -0.01 && x <= 0.01 && y >= -0.01 && y <= 0.01 && !departure) return 0;
        if (speed > tgtSpd) return speed-1;
        else if (speed < tgtSpd) return speed+1;
        else return speed;
    }
}