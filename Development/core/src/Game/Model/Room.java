package Game.Model;

import com.badlogic.gdx.math.Matrix4;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Nate on 2/15/17.
 *
 * Class to represent a room in the level
 */

@JsonIgnoreProperties(value = {"worldTransform", "transform"})
public class Room {

    public Wall ceiling = new Wall(Wall.WALL_TYPE.NONE);
    public Wall floor = new Wall(Wall.WALL_TYPE.NONE);
    public Wall north = new Wall(Wall.WALL_TYPE.NONE);
    public Wall south = new Wall(Wall.WALL_TYPE.NONE);
    public Wall east = new Wall(Wall.WALL_TYPE.NONE);
    public Wall west = new Wall(Wall.WALL_TYPE.NONE);
    public String pos;

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public Matrix4 transform;

    Mirror mirror;

    public void setWorldTransform(Matrix4 transform){
        this.transform = transform;
    }

    public Matrix4 getWorldTransform() {
        return transform;
    }

    public Room() {
        transform = new Matrix4();
        mirror = null;
    }

    public boolean hasMirror() {
        if(mirror != null) {
            return true;
        }
        return false;
    }

    public Mirror getMirror() {
        return mirror;
    }

    public void rotateOrientation(LevelDataObject.PLANE plane, LevelDataObject.PLANE_ROTATION rotation) {
        int loop;
        switch (rotation) {
            case NINETY:
                loop = 1;
                break;
            case ONE_EIGHTY:
                loop = 2;
                break;
            case TWO_SEVENTY:
                loop = 3;
                break;
            default:
                loop = 0;
                break;
        }
        for(int i=0; i<loop; i++) {
            Wall temp;
            switch (plane) {
                case X:
                    temp = floor;
                    floor = north;
                    north = ceiling;
                    ceiling = south;
                    south = temp;
                    break;
                case Y:
                    temp = north;
                    north = east;
                    east = south;
                    south = west;
                    west = temp;
                    break;
                case Z:
                    temp = ceiling;
                    ceiling = east;
                    east = floor;
                    floor = west;
                    west = temp;
                    break;
            }
        }
    }
}

