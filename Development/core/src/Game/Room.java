package Game;

/**
 * Created by Nate on 2/15/17.
 *
 * Class to represent a room in the level
 */

public class Room {

    boolean mirror;

    // way to represent mirror orientation
    // perhaps 1 = 45 degrees, 2 = 90 degrees, 3 = 135 degrees, ...
    // -1 if no mirror in the room
    int mirrorOrientation;

    Wall[] walls = new Wall[6];

    public Room(boolean mirror, Wall[] walls) {
        this.mirror = mirror;
        this.walls = walls;

        if(mirror == true) {
            mirrorOrientation = 1;
        }
        else {
            mirrorOrientation = -1;
        }
    }

    public boolean isMirror() {
        return mirror;
    }

    public int getMirrorOrientation() {
        return mirrorOrientation;
    }

    public void setMirrorOrientation(int mirrorOrientation) {
        this.mirrorOrientation = mirrorOrientation;
    }

    t
}

