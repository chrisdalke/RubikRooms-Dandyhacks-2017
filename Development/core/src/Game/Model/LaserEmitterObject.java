package Game.Model;

/**
 * Created by Nate on 2/17/17.
 */
public class LaserEmitterObject {

    double[] worldCoords;
    Laser_Direction direction;

    enum Laser_Direction {
        NEG_X,
        POS_X,
        NEG_Y,
        POS_Y,
        NEG_Z,
        POS_Z
    }

    public LaserEmitterObject(double x, double y, double z, Laser_Direction direction) {
        this.worldCoords = new double[]{x, y, z};
        this.direction = direction;
    }

    public double[] getWorldCoords() {
        return worldCoords;
    }

    public Laser_Direction getDirection() {
        return direction;
    }
}
