////////////////////////////////////////////////
// Development
// Chris Dalke
////////////////////////////////////////////////
// Module: LevelDataObject2
////////////////////////////////////////////////

package Game;

////////////////////////////////////////////////
// Package Imports
////////////////////////////////////////////////

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;

////////////////////////////////////////////////
// Level Data Object class
////////////////////////////////////////////////

public class LevelDataObject2 {

    ////////////////////////////////////////////////
    // Enums
    ////////////////////////////////////////////////

    public enum PLANE {
        X, Y, Z
    };

    public enum PLANE_ROTATION {
        NINETY, ONE_EIGHTY, TWO_SEVENTY, THREE_SIXTY
    }

    ////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////

    public LevelDataObject2(int sizeX, int sizeY, int sizeZ) {
        size = new int[3];
        size[0] = sizeX;
        size[1] = sizeY;
        size[2] = sizeZ;

        levelNumber = 1;
        name = "Untitled";
        description = "Untitled Level";

        rooms = new Room[sizeX][sizeY][sizeZ];
        solution = new Room[sizeX][sizeY][sizeZ];
        rotation = new float[3][Math.max(sizeX,Math.max(sizeY,sizeZ))];
    }

    ////////////////////////////////////////////////
    // Properties
    ////////////////////////////////////////////////

    //General level properties
    int[] size;

    int levelNumber;
    String name;
    String description;

    //Rotation properties
    //2d array in format [axis][vector of rotations];
    float[][] rotation;

    //Room object contains data about what a room contains
    //as well as information about the room's orientation, which this class modifies
    Room[][][] rooms; //Stores shifted rooms
    Room[][][] solution; //Stores unshifted rooms (solution)

    ////////////////////////////////////////////////
    // Methods
    ////////////////////////////////////////////////

    //Visually rotates the rooms on a given plane at a smooth angle.
    //Does not reposition the room array structure (only visual)
    public void rotatePlane(PLANE plane, float angle){

    }

    //Repositions the rooms on a given plane.
    //Rearranges the array structure and resets rotation for the given plane.
    //Also shifts the orientation stored in each Room object
    public void shiftPlane(PLANE plane, PLANE_ROTATION angle){

    }

    public void calculateRoomPositions(){
        //Calculates the world positions of all of the rooms
        //and stores it in the room's member variables.
        //assumes rooms have a size of 1 (will be scaled later in path)
        //Does this based on smooth room angle.

        //Use trigonometry to calculate these transforms!
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

    public static void save(LevelDataObject2 level,String filename){
        //Save a level data object to JSON file
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        try {
            mapper.writeValue(new File(filename), level);
        } catch (IOException e){
            System.out.println("Failed to save level file!");
        }
    }
    public static LevelDataObject2 load(String filename){
        //Load a level data object from JSON file
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        try {
            LevelDataObject2 obj = mapper.readValue(new File(filename), LevelDataObject2.class);
            return obj;
        } catch (Exception e){
            System.out.println("Failed to load level file!");
        }
        return null;
    }

    public static void main(String[] args){
        System.out.println("Starting level serialization test...");
        LevelDataObject2 lvl = new LevelDataObject2(2,2,2);

        LevelDataObject2.save(lvl,"Assets/Levels/test.txt");
        System.out.println("Done.");
    }
    ////////////////////////////////////////////////
    // Getters / Setters
    ////////////////////////////////////////////////


    public int[] getSize() {
        return size;
    }

    public void setSize(int[] size) {
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