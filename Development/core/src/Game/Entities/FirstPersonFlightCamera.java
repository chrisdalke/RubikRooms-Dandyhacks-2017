////////////////////////////////////////////////
// Development
// Chris Dalke
////////////////////////////////////////////////
// Module: FirstPersonFlightCamera
////////////////////////////////////////////////

package Game.Entities;

import Engine.Game.Entity.Types.Camera3d;
import Engine.Input.Input;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;

public class FirstPersonFlightCamera extends Camera3d {
   
   private Camera camera;
   private int STRAFE_LEFT = com.badlogic.gdx.Input.Keys.A;
   private int STRAFE_RIGHT = com.badlogic.gdx.Input.Keys.D;
   private int FORWARD = com.badlogic.gdx.Input.Keys.W;
   private int BACKWARD = com.badlogic.gdx.Input.Keys.S;
   private int UP = com.badlogic.gdx.Input.Keys.Q;
   private int DOWN = com.badlogic.gdx.Input.Keys.E;
   private float velocity = 50;
   private float degreesPerPixel = 0.5f;
   private final Vector3 tmp = new Vector3();

   private float vLeft = 0;
   private float vRight = 0;
   private float vForward = 0;
   private float vBackward = 0;


   public void setVelocity (float velocity) {
      this.velocity = velocity;
   }
   public void setDegreesPerPixel (float degreesPerPixel) {
      this.degreesPerPixel = degreesPerPixel;
   }


   @Override
   public void init() {
      super.init();

      camera = getCam();

   }
   
   @Override
   public void update() {

      float deltaTime = 0.01f;

      float deltaX = -Gdx.input.getDeltaX() * degreesPerPixel;
      float deltaY = -Gdx.input.getDeltaY() * degreesPerPixel;
      camera.direction.rotate(camera.up, deltaX);
      tmp.set(camera.direction).crs(camera.up).nor();
      camera.direction.rotate(tmp, deltaY);
// camera.up.rotate(tmp, deltaY);

      float decay = 0.85f;
      float rampUp = 0.05f;
      if (Input.getKey(FORWARD)) {
         vForward = Math.min(vForward+rampUp,1.0f);
      } else {
         vForward *= decay;
      }

      tmp.set(camera.direction).nor().scl(deltaTime * velocity * vForward);
      camera.position.add(tmp);


      if (Input.getKey(BACKWARD)) {
         vBackward = Math.min(vBackward+rampUp,1.0f);
      } else {
         vBackward *= decay;
      }
      tmp.set(camera.direction).nor().scl(-deltaTime * velocity * vBackward);
      camera.position.add(tmp);

      if (Input.getKey(STRAFE_LEFT)) {
         vLeft = Math.min(vLeft+rampUp,1.0f);
      } else {
         vLeft *= decay;
      }

      tmp.set(camera.direction).crs(camera.up).nor().scl(-deltaTime * velocity * vLeft);
      camera.position.add(tmp);

      if (Input.getKey(STRAFE_RIGHT)) {
         vRight = Math.min(vRight+rampUp,1.0f);
      } else {
         vRight *= decay;
      }
      tmp.set(camera.direction).crs(camera.up).nor().scl(deltaTime * velocity * vRight);
      camera.position.add(tmp);

      if (Input.getKey(UP)) {
         tmp.set(camera.up).nor().scl(deltaTime * velocity);
         camera.position.add(tmp);
      }
      if (Input.getKey(DOWN)) {
         tmp.set(camera.up).nor().scl(-deltaTime * velocity);
         camera.position.add(tmp);
      }
      camera.update(true);

   }
   
   @Override
   public void dispose(){

   }
}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////