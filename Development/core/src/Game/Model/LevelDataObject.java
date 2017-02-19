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
import com.badlogic.gdx.ai.steer.utils.paths.LinePath;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import static Game.Model.LevelDataObject.PLANE.X;
import static Game.Model.LevelDataObject.PLANE_ROTATION.*;

////////////////////////////////////////////////
// Level Data Object class
////////////////////////////////////////////////

@JsonIgnoreProperties(value = {""})
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

    public LevelDataObject(){

    }

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
                    rooms[x][y][z] = new Room();
                    rooms[x][y][z].setPos(x+","+y+","+z);
                    solution[x][y][z] = rooms[x][y][z];

                    rooms[x][y][z].transform = new Matrix4().translate(x,y,z);
                }
            }
        }

        plane = X;
        planeId = 0;
        planeRotation = 0;
        planeHasRotation = false;
        moves = new Stack<Move>();
        undoneMoves = new Stack<Move>();

    }



    ////////////////////////////////////////////////
    // Properties
    ////////////////////////////////////////////////

    //General level properties
    int size;
    int levelNumber;
    String name;
    String description;

    // Stores the moves made by the user
    Stack<Move> moves;
    Stack<Move> undoneMoves;

    //Rotation properties
    //Stores the current plane of rotation, ID of rotation, and float indicating rotation angle
    PLANE plane;
    int planeId;
    float planeRotation;
    Quaternion planeRotationQ;
    Vector3 planeRotationVector;
    boolean planeHasRotation;

    //Room object contains data about what a room contains
    //as well as information about the room's orientation, which this class modifies
    Room[][][] rooms; //Stores shifted rooms
    Room[][][] solution; //Stores unshifted rooms (solution)

    // Laser properties
    double receiverRadiusThreshold;

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
        switch (plane){
            case X:{
                planeRotationVector = new Vector3(1,0,0);
                planeRotationQ = new Quaternion(planeRotationVector,angle);
                break;
            }
            case Y:{
                planeRotationVector = new Vector3(0,1,0);
                planeRotationQ = new Quaternion(planeRotationVector,angle);
                break;
            }
            case Z:{
                planeRotationVector = new Vector3(0,0,1);
                planeRotationQ = new Quaternion(planeRotationVector,angle);
                break;
            }
        }
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
            if (delta > 0 & smoothRotateTargetAngle >= planeRotation){
                planeRotation += delta;
                //planeRotationQ.add(new Quaternion(planeRotationVector,delta));
            } else if (delta < 0 & -(360.0f-smoothRotateTargetAngle) <= planeRotation){
                planeRotation += delta;
                //planeRotationQ.add(new Quaternion(planeRotationVector,delta));
            } else {
                //turn rotation off and snap the plane into place.
                Logger.log("SNAPPED ROTATION");
                snapPlane();
                smoothRotateActive = false;
            }
            calculateRoomPositions();
        }
    }

    public void setDelta(float deltaNew){
        delta = deltaNew;
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
            Move move = new Move(plane, planeId, direction);
            moves.push(move);
            shiftPlane(move);
            // clear undoneMoves (so that the user can not redo -- moves have been overwritten)
            undoneMoves.clear();
        }
        planeRotation = 0;
        planeHasRotation = false;
    }

    //Repositions the rooms on a given plane.
    //Rearranges the array structure and resets rotation for the given plane.
    //Also shifts the orientation stored in each Room object
    public void shiftPlane(Move move){
        PLANE rotatePlane = move.getRotatePlane();
        int rotatePlaneId = move.getRotatePlaneId();
        PLANE_ROTATION angle = move.getAngle();

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

            // convert to 2D array
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

            // convert back to 3D array
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

    public void undoMove() {
        if(!moves.isEmpty()) {
            Move move = moves.pop();
            undoneMoves.push(move);
            Move undoMove = null;
            switch (move.getAngle()) {
                case NINETY:
                    undoMove = new Move(move.getRotatePlane(), move.getRotatePlaneId(), TWO_SEVENTY);
                    break;
                case ONE_EIGHTY:
                    undoMove = new Move(move.getRotatePlane(), move.getRotatePlaneId(), ONE_EIGHTY);
                    break;
                case TWO_SEVENTY:
                    undoMove = new Move(move.getRotatePlane(), move.getRotatePlaneId(), NINETY);
                    break;
            }
            shiftPlane(undoMove);
        }
    }

    public void redoMove() {
        // make sure undoneMoves stack is not empty
        if(!undoneMoves.isEmpty()) {
            // pop from undone moves stack, push to moves stack
            Move move = undoneMoves.pop();
            moves.push(move);
            // perform move
            shiftPlane(move);
        }
    }

    public boolean positionIsRotating(int x, int y, int z) {
        if(planeHasRotation) {
            switch(plane) {
                case X:
                    if(planeId == x) {
                        return true;
                    }
                    break;
                case Y:
                    if(planeId == y) {
                        return true;
                    }
                    break;
                case Z:
                    if(planeId == z) {
                        return true;
                    }
                    break;
            }
        }
        return false;
    }

    public ArrayList<LaserEmitterObject> getLaserEmitterPositions(){
        ArrayList<LaserEmitterObject> emitters = new ArrayList<LaserEmitterObject>();
        for (int i=0; i<size; i++) {
            for (int j=0; j<size; j++) {
                for (int k=0; k<size; k++) {
                    if(!positionIsRotating(i,j,k)) {
                        if (rooms[i][j][k].west.wallType == Wall.WALL_TYPE.LASER_EMITTER) {
                            emitters.add(new LaserEmitterObject((float) (i - 0.5), j, k, LaserEmitterObject.Emitter_Direction.POS_X));
                        }
                        if (rooms[i][j][k].east.wallType == Wall.WALL_TYPE.LASER_EMITTER) {
                            emitters.add(new LaserEmitterObject((float) (i + 0.5), j, k, LaserEmitterObject.Emitter_Direction.NEG_X));
                        }
                        if (rooms[i][j][k].floor.wallType == Wall.WALL_TYPE.LASER_EMITTER) {
                            emitters.add(new LaserEmitterObject(i, (float) (j - 0.5), k, LaserEmitterObject.Emitter_Direction.POS_Y));
                        }
                        if (rooms[i][j][k].ceiling.wallType == Wall.WALL_TYPE.LASER_EMITTER) {
                            emitters.add(new LaserEmitterObject(i, (float) (j + 0.5), k, LaserEmitterObject.Emitter_Direction.NEG_Y));
                        }
                        if (rooms[i][j][k].south.wallType == Wall.WALL_TYPE.LASER_EMITTER) {
                            emitters.add(new LaserEmitterObject(i, j, (float) (k - 0.5), LaserEmitterObject.Emitter_Direction.POS_Z));
                        }
                        if (rooms[i][j][k].west.wallType == Wall.WALL_TYPE.LASER_EMITTER) {
                            emitters.add(new LaserEmitterObject(i, j, (float) (k + 0.5), LaserEmitterObject.Emitter_Direction.NEG_Z));
                        }
                    }
                }
            }
        }
        return emitters;
    }

    public ArrayList<LaserReceiverObject> getLaserReceiverPositions(){
        ArrayList<LaserReceiverObject> receivers = new ArrayList<LaserReceiverObject>();
        for (int i=0; i<size; i++) {
            for (int j=0; j<size; j++) {
                for (int k=0; k<size; k++) {
                    if(!positionIsRotating(i,j,k)) {
                        if (rooms[i][j][k].west.wallType == Wall.WALL_TYPE.LASER_RECEIVER) {
                            receivers.add(new LaserReceiverObject((float) (i - 0.5), j, k, LaserReceiverObject.Receiver_Direction.POS_X));
                        }
                        if (rooms[i][j][k].east.wallType == Wall.WALL_TYPE.LASER_RECEIVER) {
                            receivers.add(new LaserReceiverObject((float) (i + 0.5), j, k, LaserReceiverObject.Receiver_Direction.NEG_X));
                        }
                        if (rooms[i][j][k].floor.wallType == Wall.WALL_TYPE.LASER_RECEIVER) {
                            receivers.add(new LaserReceiverObject(i, (float) (j - 0.5), k, LaserReceiverObject.Receiver_Direction.POS_Y));
                        }
                        if (rooms[i][j][k].ceiling.wallType == Wall.WALL_TYPE.LASER_RECEIVER) {
                            receivers.add(new LaserReceiverObject(i, (float) (j + 0.5), k, LaserReceiverObject.Receiver_Direction.NEG_Y));
                        }
                        if (rooms[i][j][k].south.wallType == Wall.WALL_TYPE.LASER_RECEIVER) {
                            receivers.add(new LaserReceiverObject(i, j, (float) (k - 0.5), LaserReceiverObject.Receiver_Direction.POS_Z));
                        }
                        if (rooms[i][j][k].west.wallType == Wall.WALL_TYPE.LASER_RECEIVER) {
                            receivers.add(new LaserReceiverObject(i, j, (float) (k + 0.5), LaserReceiverObject.Receiver_Direction.NEG_Z));
                        }
                    }
                }
            }
        }
        return receivers;
    }

    public void propagateLasers() {
        for(int i=0; i<size; i++) {
            for (int j=0; j<size; j++) {
                for (int k=0; k<size; k++) {
                    for (int l=0; l<6; l++) {
                        /*
                        if (rooms[i][j][k].walls[l].wallType == Wall.WALL_TYPE.NONE ||
                                rooms[i][j][k].walls[l].wallType == Wall.WALL_TYPE.WALL_GLASS) {
                            // will continue later
                        }
                        */
                    }
                }
            }
        }
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
                            //rooms[planeId][i][j].transform.rotate(planeRotationQ);
                            break;
                        }
                        case Y:{
                            //Rotate around the Y axis, uses X,Z
                            rooms[i][planeId][j].transform.rotate(0,1,0,planeRotation);
                            //rooms[planeId][i][j].transform.rotate(planeRotationQ);
                            break;
                        }
                        case Z:{
                            //Rotate around the Z axis, uses X,Y
                            rooms[i][j][planeId].transform.rotate(0,0,1,planeRotation);
                            //rooms[planeId][i][j].transform.rotate(planeRotationQ);
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
    public static LevelDataObject load(File file){
        //Load a level data object from JSON file
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        try {
            LevelDataObject obj = mapper.readValue(file, LevelDataObject.class);
            return obj;
        } catch (Exception e){
            System.out.println("Failed to load level file " + file.getName() + " in load()");
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<LevelDataObject> getListOfLevels() {
        ArrayList<LevelDataObject> levelsList = new ArrayList<LevelDataObject>();
        File[] files = new File("Assets/Levels").listFiles();
        for(File file: files) {

            // get file extension
            String extension = "";
            int i = file.getName().lastIndexOf('.');
            if(i > 0) {
                extension = file.getName().substring(i+1);
            }

            // if the file is a .txt
            if(extension.equals("txt")) {
                System.out.println("Loading file " + file.getName());
                try{
                    levelsList.add(load(file));
                }
                catch (Exception e){
                    System.out.println("Failed to load level file " + file.getName() + " in getListOfLevels()");
                }
            }
        }
        return levelsList;
    }

    public static void test() {
        LevelDataObject test = new LevelDataObject(2);
        save(test, "Assets/Levels/2x2.txt");

        getListOfLevels();
    }

    public static void main(String[] args){
//        System.out.println("Starting level serialization test...");
//        LevelDataObject lvl = new LevelDataObject(2);
//
//        LevelDataObject.save(lvl,"Assets/Levels/test.txt");
//        System.out.println("Done.");

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

    public static float getVectorDistance(Vector3 vec1, Vector3 vec2){
        Array<Vector3> vecPath = new Array<>(true,2);
        vecPath.add(vec1);
        vecPath.add(vec2);
        return new LinePath<Vector3>(vecPath).getLength();
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