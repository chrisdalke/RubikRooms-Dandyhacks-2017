////////////////////////////////////////////////
// Development
// Chris Dalke
////////////////////////////////////////////////
// Module: LevelDataObject
////////////////////////////////////////////////

package Game;

public class LevelDataObject {

   int dimensionLength;
   int startingRoom;
   Room[] rooms;
   Plane[] planes;


   public LevelDataObject(String filename) {
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
}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////