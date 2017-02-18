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

    Wall ceiling = new Wall(Wall.WALL_TYPE.WALL);
    Wall floor = new Wall(Wall.WALL_TYPE.WALL);
    Wall north = new Wall(Wall.WALL_TYPE.WALL);
    Wall south = new Wall(Wall.WALL_TYPE.WALL);
    Wall east = new Wall(Wall.WALL_TYPE.WALL);
    Wall west = new Wall(Wall.WALL_TYPE.WALL);

    Wall[] walls = new Wall[]{ceiling, floor, north, south, east, west};

    public Matrix4 transform;

    public void setWorldTransform(Matrix4 transform){
        this.transform = transform;
    }

    public Matrix4 getWorldTransform() {
        return transform;
    }

    public Room() {
        transform = new Matrix4();
    }

    public Wall[] getWalls() {
        return walls;
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

