package Game.Model;

import com.badlogic.gdx.math.Vector3;

/**
 * Created by Nate on 2/17/17.
 */
public class LaserEmitterObject {

    Vector3 worldCoords;
    Emitter_Direction direction;

    enum Emitter_Direction {
        NEG_X,
        POS_X,
        NEG_Y,
        POS_Y,
        NEG_Z,
        POS_Z
    }

    public LaserEmitterObject(float x, float y, float z, Emitter_Direction direction) {
        this.worldCoords = new Vector3(x, y, z);
        this.direction = direction;
    }

    public Vector3 getWorldCoords() {
        return worldCoords;
    }

    public Emitter_Direction getDirection() {
        return direction;
    }
}
