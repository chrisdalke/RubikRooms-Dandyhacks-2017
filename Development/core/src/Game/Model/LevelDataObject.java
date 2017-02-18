////////////////////////////////////////////////
// Development
// Chris Dalke
////////////////////////////////////////////////
// Module: LevelDataObject
////////////////////////////////////////////////

package Game.Model;

////////////////////////////////////////////////
// Package Imports
////////////////////////////////////////////////

import Engine.System.Logging.Logger;
import com.badlogic.gdx.math.Matrix4;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;

import static Game.Model.LevelDataObject.PLANE.X;
import static Game.Model.LevelDataObject.PLANE_ROTATION.*;

////////////////////////////////////////////////
// Level Data Object class
////////////////////////////////////////////////

public class LevelDataObject {

    ////////////////////////////////////////////////
    // Enums
    ////////////////////////////////////////////////

    public enum PLANE {
        X, Y, Z
    };

    public enum PLANE_ROTATION {
        ZERO, NINETY, ONE_EIGHTY, TWO_SEVENTY
    }

    ////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////

    public LevelDataObject(int size) {
        this.size = size;

        levelNumber = 1;
        name = "Untitled";
        description = "Untitled Level";

        rooms = new Room[size][size][size];
        solution = new Room[size][size][size];

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                for (int z = 0; z < size; z++) {
                    rooms[x][y][z] = new Room(null);
                    solution[x][y][z] = rooms[x][y][z];
                    rooms[x][y][z].transform = new Matrix4().translate(x,y,z);
                }
            }
        }

        plane = X;
        planeId = 0;
        planeRotation = 0;
        planeHasRotation = false;

    }

    ////////////////////////////////////////////////
    // Properties
    ////////////////////////////////////////////////

    //General level properties
    int size;
    int levelNumber;
    String name;
    String description;

    //Rotation properties
    //Stores the current plane of rotation, ID of rotation, and float indicating rotation angle
    PLANE plane;
    int planeId;
    float planeRotation;
    boolean planeHasRotation;

    //Room object contains data about what a room contains
    //as well as information about the room's orientation, which this class modifies
    Room[][][] rooms; //Stores shifted rooms
    Room[][][] solution; //Stores unshifted rooms (solution)

    ////////////////////////////////////////////////
    // Methods
    ////////////////////////////////////////////////

    //Visually rotates the rooms on a given plane at a smooth angle.
    //Does not reposition the room array structure (only visual)
    public void rotatePlane(PLANE plane, int planeId, float angle){
        //Override existing rotation if one exists - this might look glitchy
        //Potentially snap grid if angle isn't zero?
        this.plane = plane;
        this.planeId = planeId;
        planeRotation = angle % 360.0f;
        planeHasRotation = true;
        calculateRoomPositions(); //Trigger calculation of room world positions
    }

    boolean smoothRotateActive;
    float smoothRotateTargetAngle;
    float delta;

    //Triggers a smooth rotation which snaps into place once it reaches its target.
    public void triggerRotatePlane(PLANE plane, int planeId, PLANE_ROTATION targetAngle, float delta){
        if (!smoothRotateActive) {
            this.delta = delta;
            this.plane = plane;
            this.planeId = planeId;
            planeRotation = 0;
            planeHasRotation = true;
            smoothRotateActive = true;
            switch (targetAngle) {
                case NINETY:
                    smoothRotateTargetAngle = 90;
                    break;
                case ONE_EIGHTY:
                    smoothRotateTargetAngle = 180;
                    break;
                case TWO_SEVENTY:
                    smoothRotateTargetAngle = 270;
                    break;
                default:
                    smoothRotateTargetAngle = 0;
            }
        }
    }

    public void update(){
        if (smoothRotateActive){
            if (delta > 0 & smoothRotateTargetAngle > planeRotation){
                planeRotation += delta;
            } else if (delta < 0 & -(360-smoothRotateTargetAngle) < planeRotation){
                planeRotation += delta;
            } else {
                //turn rotation off and snap the plane into place.
                snapPlane();
                smoothRotateActive = false;
            }
            calculateRoomPositions();
        }
    }

    //Triggers the plane to "snap" into place, converting the smooth angle into a shifted plane.
    public void snapPlane(){
        //Cap the rotation to 360 degrees
        planeRotation = planeRotation % 360.0f;
        //Figure out which rotation angle is the closest
        int planeRotationRounded = Math.round(Math.round(planeRotation / 90) * 90);
        PLANE_ROTATION direction = ZERO;
        switch (planeRotationRounded){
            case 0:{ direction = ZERO; break;}
            case 90:{ direction = NINETY; break;}
            case 180:{ direction = ONE_EIGHTY; break;}
            case 270:{ direction = TWO_SEVENTY; break;}
            default: {
                Logger.log("Invalid snap angle: "+planeRotationRounded);
                break;
            }
        }
        //Perform rotation if the direction isn't zero
        if (direction != ZERO){
            shiftPlane(plane,planeId,direction);
        }
        planeRotation = 0;
        planeHasRotation = false;
    }

    //Repositions the rooms on a given plane.
    //Rearranges the array structure and resets rotation for the given plane.
    //Also shifts the orientation stored in each Room object
    public void shiftPlane(PLANE rotatePlane, int rotatePlaneId, PLANE_ROTATION angle){
        int loop;
        switch (angle) {
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
        }

        for(int loopId=0; loopId<loop; loopId++) {

            Room[][] tempArray = new Room[size][size];

            for(int i=0; i<size; i++) {
                for (int j=0; j<size; j++) {
                    switch (rotatePlane) {
                        case X:
                            tempArray[i][j] = rooms[rotatePlaneId][i][j];
                            break;
                        case Y:
                            tempArray[i][j] = rooms[i][rotatePlaneId][j];
                            break;
                        case Z:
                            tempArray[i][j] = rooms[i][j][rotatePlaneId];
                            break;
                    }
                }
            }

            // rotate over 2D plane
            tempArray = rotate90Degrees(tempArray);


            for(int i=0; i<size; i++) {
                for (int j=0; j<size; j++) {
                    switch (rotatePlane) {
                        case X:
                            rooms[rotatePlaneId][i][j] = tempArray[i][j];
                            break;
                        case Y:
                            rooms[i][rotatePlaneId][j] = tempArray[i][j];
                            break;
                        case Z:
                            rooms[i][j][rotatePlaneId] = tempArray[i][j];
                            break;
                    }
                }
            }
        }
    }

    public Room[][] rotate90Degrees(Room[][] array){
        Room[][] result = new Room[array.length][array.length];

        // transpose matrix
        for(int i=0; i<array.length; i++) {
            for(int j=0; j<array.length; j++) {
                result[i][j] = array[j][i];
            }
        }

        // flip rows
        for(int i=0; i<result.length; i++) {
            for(int j=0; j<result.length/2; j++) {
                Room temp = result[i][j];
                result[i][j] = result[i][result.length - j - 1];
                result[i][result.length - j - 1] = temp;
            }
        }

        return result;
    }

    //Assumes that there is only one angle in the rotation array that isn't zero
    public void calculateRoomPositions(){
        //Calculates the world positions of all of the rooms
        //and stores it in the room's member variables.
        //assumes rooms have a size of 1 (will be scaled later in path)
        //Does this based on smooth room angle.

        //Calculate center value
        float center = (size-1) / 2.0f;

        //Reset all transforms to identity matrix
        for (int x = 0; x < size; x++){
            for (int y = 0; y < size; y++){
                for (int z = 0; z < size; z++){
                    rooms[x][y][z].transform = new Matrix4();

                    //Set transform to center position for given plane of rotation
                    switch (plane){
                        case X:{
                            //Translate this object to center its rotation
                            rooms[x][y][z].transform.translate(0,center,center);
                            break;
                        }
                        case Y:{
                            //Translate this object to center its rotation
                            rooms[x][y][z].transform.translate(center,0,center);
                            break;
                        }
                        case Z:{
                            //Translate this object to center its rotation
                            rooms[x][y][z].transform.translate(center,center,0);
                            break;
                        }
                    }
                }
            }
        }


        //Calculate transforms for the rotated rooms, if one exists
        if (planeHasRotation){
            for (int i = 0; i < size; i++){
                for (int j = 0; j < size; j++){
                    switch (plane){
                        case X:{
                            //Rotate around the X axis, uses Y,Z
                            rooms[planeId][i][j].transform.rotate(1,0,0,planeRotation);
                            break;
                        }
                        case Y:{
                            //Rotate around the Y axis, uses X,Z
                            rooms[i][planeId][j].transform.rotate(0,1,0,planeRotation);
                            break;
                        }
                        case Z:{
                            //Rotate around the Z axis, uses X,Y
                            rooms[i][j][planeId].transform.rotate(0,0,1,planeRotation);
                            break;
                        }
                    }

                }
            }
        }

        //Calculate transforms for the non rotated rooms
        //Subtract center so that the cube ends up being back in correct position
        for (int x = 0; x < size; x++){
            for (int y = 0; y < size; y++){
                for (int z = 0; z < size; z++){
                    switch (plane){
                        case X:{
                            rooms[x][y][z].transform.translate(x,y-center,z-center);
                            break;
                        }
                        case Y:{
                            rooms[x][y][z].transform.translate(x-center,y,z-center);
                            break;
                        }
                        case Z:{
                            rooms[x][y][z].transform.translate(x-center,y-center,z);
                            break;
                        }
                    }
                }
            }
        }

    }

    public boolean isSolutionPosition(int x, int y, int z){
        //Check if a given room position in the cube contains the correct room
        //In other words, check if a room is in a solution position
        try {
            return rooms[x][y][z] == solution[x][y][z];
        } catch (Exception e){
            //Arrays out of bounds so this position is invalid
            return false;
        }
    }

    ////////////////////////////////////////////////
    // Level Serialization and Tests
    ////////////////////////////////////////////////

    public static void save(LevelDataObject level, String filename){
        //Save a level data object to JSON file
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        try {
            mapper.writeValue(new File(filename), level);
        } catch (IOException e){
            System.out.println("Failed to save level file!");
        }
    }
    public static LevelDataObject load(String filename){
        //Load a level data object from JSON file
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        try {
            LevelDataObject obj = mapper.readValue(new File(filename), LevelDataObject.class);
            return obj;
        } catch (Exception e){
            System.out.println("Failed to load level file!");
        }
        return null;
    }

    public static void main(String[] args){
        System.out.println("Starting level serialization test...");
        LevelDataObject lvl = new LevelDataObject(2);

        LevelDataObject.save(lvl,"Assets/Levels/test.txt");
        System.out.println("Done.");

        //int[][] test = new int[][]{{1,2,3},{4,5,6},{7,8,9}};
        //test = lvl.rotate90Degrees(test);

    }
    ////////////////////////////////////////////////
    // Getters / Setters
    ////////////////////////////////////////////////

    public void setRoom(int x, int y, int z, Room room){
        rooms[x][y][z] = room;
    }

    public Room getRoom(int x, int y, int z){
        try {
            return rooms[x][y][z];
        } catch (Exception e){
            return null;
        }
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Room[][][] getRooms() {
        return rooms;
    }

    public void setRooms(Room[][][] rooms) {
        this.rooms = rooms;
    }

    public Room[][][] getSolution() {
        return solution;
    }

    public void setSolution(Room[][][] solution) {
        this.solution = solution;
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public void setLevelNumber(int levelNumber) {
        this.levelNumber = levelNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////