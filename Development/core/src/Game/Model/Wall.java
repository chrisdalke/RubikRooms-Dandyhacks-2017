package Game.Model;

/**
 * Created by Nate on 2/15/17.
 *
 * Class to represent one wall of a room
 */

public class Wall {

    public enum WALL_TYPE {
        NONE, WALL, WALL_GLASS, DOOR, LASER_EMITTER, LASER_RECEIVER, LASER_MIRROR
    }

    //TODO Implement orientation changes!!!

    public Wall(){

    }

    public WALL_TYPE wallType;

    public Wall(WALL_TYPE wallType) {
        this.wallType = wallType;
    }
}
