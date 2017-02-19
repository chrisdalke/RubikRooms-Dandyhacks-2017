package Game.Model;

import com.badlogic.gdx.math.Vector3;

/**
 * Created by Nate on 2/18/17.
 */
public class MirrorObject {

    Vector3 worldCoords;
    LevelDataObject.PLANE plane;
    int orientation = 90;

    public MirrorObject(float x, float y, float z, LevelDataObject.PLANE plane) {
        this.worldCoords = new Vector3(x, y, z);
        this.plane = plane;
    }

    public Vector3 getWorldCoords() {
        return worldCoords;
    }

    public LevelDataObject.PLANE getPlane() {
        return plane;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }
}
