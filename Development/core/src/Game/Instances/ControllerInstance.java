////////////////////////////////////////////////
// Development
// Chris Dalke
////////////////////////////////////////////////
// Module: ControllerInstance
////////////////////////////////////////////////

package Game.Instances;

import Engine.Game.Instance.AbstractGameInstance;
import Engine.Input.Input;

public class ControllerInstance extends AbstractGameInstance {
   
   @Override
   public void init() {
      super.init();
   }

   boolean sentPacket;
   
   @Override
   public void render() {

      if (Input.getMouseClicked() == 1){
         if (!sentPacket){
            sentPacket = true;
         }
      } else {
         sentPacket = false;
      }
      
   }
   
   @Override
   public void dispose() {
      
   }
}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////