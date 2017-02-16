package Game;

/**
 * Created by Nate on 2/16/17.
 *
 * A class to represent a "turnable" plane of rooms
 */

public class Plane {

    Type type;
    Room[] rooms;

    public Plane(Type type, Room[] rooms) {
        this.type = type;
        this.rooms = rooms;
    }

    public void update() {
        // plane must be updated after each turn
    }

    public enum Type {
        VERTICAL, HORIZONTAL
    }
}
