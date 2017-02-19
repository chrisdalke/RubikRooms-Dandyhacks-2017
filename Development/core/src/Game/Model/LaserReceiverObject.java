package Game.Model;

import com.badlogic.gdx.math.Vector3;

/**
 * Created by Nate on 2/18/17.
 */

public class LaserReceiverObject {

    Vector3 worldCoords;
    boolean enabled;
    Receiver_Direction direction;

    enum Receiver_Direction {
        NEG_X,
        POS_X,
        NEG_Y,
        POS_Y,
        NEG_Z,
        POS_Z
    }

    public LaserReceiverObject(float x, float y, float z, Receiver_Direction direction) {
        this.worldCoords = new Vector3(x, y, z);
        this.enabled = false;
        this.direction = direction;
    }

    public Vector3 getWorldCoords() {
        return worldCoords;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Receiver_Direction getDirection() {
        return direction;
    }

    public void checkLaserPoint(Vector3 vector, double threshold) {
        if(LevelDataObject.getVectorDistance(vector, worldCoords) <= threshold) {
            enabled = true;
        }
        else{
            enabled = false;
        }
    }
}
