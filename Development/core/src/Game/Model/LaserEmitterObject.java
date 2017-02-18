package Game.Model;

/**
 * Created by Nate on 2/17/17.
 */
public class LaserEmitterObject {

    float[] worldCoords;

    public LaserEmitterObject(float x, float y, float z) {
        this.worldCoords = new float[]{x, y, z};
    }

    public float[] getWorldCoords() {
        return worldCoords;
    }
}
