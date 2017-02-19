package Game.Model;

import Engine.System.Logging.Logger;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Nate on 2/15/17.
 *
 * Class to represent a room in the level
 */

@JsonIgnoreProperties(value = {"worldTransform", "transform","orientation","orientationX","orientationY","orientationZ"})
public class Room {

    public Wall ceiling = new Wall(Wall.WALL_TYPE.WALL_GLASS);
    public Wall floor = new Wall(Wall.WALL_TYPE.WALL_GLASS);
    public Wall north = new Wall(Wall.WALL_TYPE.WALL_GLASS);
    public Wall south = new Wall(Wall.WALL_TYPE.WALL_GLASS);
    public Wall east = new Wall(Wall.WALL_TYPE.WALL_GLASS);
    public Wall west = new Wall(Wall.WALL_TYPE.WALL_GLASS);
    public String pos;
    public Quaternion orientation;

    public int orientationX;
    public int orientationY;
    public int orientationZ;

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public Matrix4 transform;

    public void setWorldTransform(Matrix4 transform){
        this.transform = transform;
    }

    public Matrix4 getWorldTransform() {
        return transform;
    }

    public Room() {
        transform = new Matrix4();
        orientation = new Quaternion();
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
                    floor = south;
                    south = ceiling;
                    ceiling = north;
                    north = temp;
                    orientationX = (orientationX + 90) % 360;
                    orientation.add(new Quaternion(new Vector3(1,0,0),180.0f)).nor();
                    break;
                case Y:
                    temp = north;
                    north = east;
                    east = south;
                    south = west;
                    west = temp;
                    orientationY = (orientationY + 90) % 360;
                    orientation.add(new Quaternion(new Vector3(0,1,0),180.0f));
                    break;
                case Z:
                    temp = ceiling;
                    ceiling = east;
                    east = floor;
                    floor = west;
                    west = temp;
                    orientationZ = (orientationZ + 90) % 360;
                    orientation.add(new Quaternion(new Vector3(0,0,1),180.0f));
                    break;
            }
        }

        Logger.log("O: "+orientationX+" "+orientationY+" "+orientationZ);
        Logger.log(orientation+" ");

    }
}

