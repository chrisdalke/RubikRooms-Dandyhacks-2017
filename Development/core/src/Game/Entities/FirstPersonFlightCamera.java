////////////////////////////////////////////////
// Development
// Chris Dalke
////////////////////////////////////////////////
// Module: FirstPersonFlightCamera
////////////////////////////////////////////////

package Game.Entities;

import Engine.Game.Entity.Types.Camera3d;
import Engine.Input.Input;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;

public class FirstPersonFlightCamera extends Camera3d {
   
   private FirstPersonCameraController camController;
   
   @Override
   public void init() {
      super.init();
   
   
      camController = new FirstPersonCameraController(getCam());
      Input.addInputHandler(camController);
      camController.setVelocity(100.0f);
   }
   
   @Override
   public void update() {
      
      //Update positions based on user input
      
      
      
      
      
      
      super.update();
   }
   
   @Override
   public void dispose(){
   
      Input.removeInputHandler(camController);
   }
}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////