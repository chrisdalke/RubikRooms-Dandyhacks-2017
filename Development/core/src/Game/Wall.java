package Game;

/**
 * Created by Nate on 2/15/17.
 *
 * Class to represent one wall of a room
 */

public class Wall {

    boolean opaque; // is the wall opaque?
    boolean laser; // does a laser come out of the wall?
    Type type;

    public Wall(boolean opaque, boolean laser, Type type) {
        this.opaque = opaque;
        this.laser = laser;
        this.type = type;
    }

    public enum Type {
        CEILING, FLOOR, NORTH, SOUTH, EAST, WEST
    }
}
