////////////////////////////////////////////////
// Development
// Chris Dalke
////////////////////////////////////////////////
// Module: LevelDataObject
////////////////////////////////////////////////

package Game;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class LevelDataObject {

   int dimensionLength;
   int startingRoom;
   int[] test;
   Room[] rooms;
   Plane[] planes;


   public LevelDataObject() {
   }


   public Room[] getRooms() {
      return rooms;
   }

   public void setRooms(Room[] rooms) {
      this.rooms = rooms;
   }

   public Plane[] getPlanes() {
      return planes;
   }

   public void setPlanes(Plane[] planes) {
      this.planes = planes;
   }

   public int getDimensionLength() {
      return dimensionLength;
   }

   public int getStartingRoom() {
      return startingRoom;
   }

   public Room getRoom(int n) {
      return rooms[n];
   }

   public Plane getPlane(int n) {
      return planes[n];
   }

   public void setDimensionLength(int dimensionLength) {
      this.dimensionLength = dimensionLength;
   }

   public void setStartingRoom(int startingRoom) {
      this.startingRoom = startingRoom;
   }

   public void rotatePlane(Plane plane, Float degrees){
      // rotate specific plane
   }

   public void updatePlanes(){
      // update planes array after a rotation
   }

   public static void save(LevelDataObject level,String filename){
      //Save a level data object to JSON file
      ObjectMapper mapper = new ObjectMapper();
      try {
         mapper.writeValue(new File(filename), level);
      } catch (IOException e){
         System.out.println("Failed to save level file!");
      }
   }

   public static LevelDataObject load(String filename){
      //Load a level data object from JSON file
      ObjectMapper mapper = new ObjectMapper();
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
      LevelDataObject lvl = new LevelDataObject();
      lvl.dimensionLength = 3;
      lvl.startingRoom = 2;
      lvl.rooms = new Room[1];
      lvl.rooms[0] = new Room(false,null);
      lvl.test = new int[10];
      lvl.test[4] = 100;

      LevelDataObject.save(lvl,"Assets/Levels/test.txt");
      System.out.println("Done.");
   }
}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////