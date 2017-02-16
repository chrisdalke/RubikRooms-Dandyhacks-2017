package Game;

/**
 * Created by Nate on 2/15/17.
 *
 * Class to represent one wall of a room
 */

public class Wall {

    boolean opaque; // is the wall opaque
    boolean laser; // does a laser come out of the wall
    boolean receiver; // is the wall a laser receiver (goal state)
    Type type;

    public Wall(boolean opaque, boolean laser, boolean receiver, Type type) {
        this.opaque = opaque;
        this.laser = laser;
        this.receiver = receiver;
        this.type = type;
    }

    public boolean isOpaque() {
        return opaque;
    }

    public boolean isLaser() {
        return laser;
    }

    public boolean isReceiver() {
        return receiver;
    }

    public Wall.Type getType() {
        return type;
    }

    public enum Type {
        CEILING, FLOOR, NORTH, SOUTH, EAST, WEST
    }
}
