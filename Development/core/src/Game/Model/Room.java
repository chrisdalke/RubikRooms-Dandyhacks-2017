package Game.Model;

import com.badlogic.gdx.math.Matrix4;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Nate on 2/15/17.
 *
 * Class to represent a room in the level
 */

@JsonIgnoreProperties(value = {"transform"})
public class Room {

    public Matrix4 transform;

    public void setWorldTransform(Matrix4 transform){
        this.transform = transform;
    }

    public Matrix4 getWorldTransform() {
        return transform;
    }

    Wall[] walls = new Wall[6];

    public Room(Wall[] walls) {
        this.walls = walls;
        transform = new Matrix4();
    }
}

