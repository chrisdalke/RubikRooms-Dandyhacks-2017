////////////////////////////////////////////////
// Development
// Chris Dalke
////////////////////////////////////////////////
// Module: ControllerInstance
////////////////////////////////////////////////

package Game.Instances;

import Engine.Game.Instance.AbstractGameInstance;
import Engine.Input.Input;
import Engine.System.Config.Configuration;

public class ControllerInstance extends AbstractGameInstance {
   
   @Override
   public void init(Configuration config) {
      super.init(config);
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