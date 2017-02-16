package Game;

/**
 * Created by Nate on 2/15/17.
 *
 * Class to represent a room in the level
 */

public class Room {

    boolean mirror;

    Wall[] walls = new Wall[6];

    public Room(boolean mirror, Wall[] walls) {
        this.mirror = mirror;
        this.walls = walls;
    }
}
