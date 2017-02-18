package Game.Model;

/**
 * Created by Nate on 2/18/17.
 */
public class Move {
    LevelDataObject.PLANE rotatePlane;
    int rotatePlaneId;
    LevelDataObject.PLANE_ROTATION angle;

    public Move(LevelDataObject.PLANE rotatePlane, int rotatePlaneId, LevelDataObject.PLANE_ROTATION angle) {
        this.rotatePlane = rotatePlane;
        this.rotatePlaneId = rotatePlaneId;
        this.angle = angle;
    }

    public int getRotatePlaneId() {
        return rotatePlaneId;
    }

    public LevelDataObject.PLANE getRotatePlane() {
        return rotatePlane;
    }

    public LevelDataObject.PLANE_ROTATION getAngle() {
        return angle;
    }
}
