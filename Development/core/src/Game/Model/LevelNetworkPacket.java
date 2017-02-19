////////////////////////////////////////////////
// Development
// Chris Dalke
////////////////////////////////////////////////
// Module: LevelNetworkPacket
////////////////////////////////////////////////

package Game.Model;

public class LevelNetworkPacket {

    public LevelNetworkPacket(){

    }

    public int plane;
    public int planeId;
    public int planeRotation;
    public float delta;

    public LevelNetworkPacket(int plane, int planeId, int planeRotation, float delta) {
        this.plane = plane;
        this.planeId = planeId;
        this.planeRotation = planeRotation;
        this.delta = delta;
    }

}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////