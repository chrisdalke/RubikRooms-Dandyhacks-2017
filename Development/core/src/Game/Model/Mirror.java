package Game.Model;

import com.badlogic.gdx.math.Vector3;

/**
 * Created by Nate on 2/18/17.
 */
public class Mirror {

    LevelDataObject.PLANE plane;
    int rotation;
    Vector3 worldCoords;


    public Mirror(LevelDataObject.PLANE plane, int rotation, Vector3 worldCoords) {
        this.plane = plane;
        this.rotation = rotation;
        this.worldCoords = worldCoords;
    }

    public int getRotation() {
        return rotation;
    }

    public LevelDataObject.PLANE getPlane() {
        return plane;
    }

    public Vector3 getWorldCoords() {
        return worldCoords;
    }
}
