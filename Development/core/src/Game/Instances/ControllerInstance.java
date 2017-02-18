////////////////////////////////////////////////
// Development
// Chris Dalke
////////////////////////////////////////////////
// Module: ControllerInstance
////////////////////////////////////////////////

package Game.Instances;

import Engine.Game.Entity.GameObject3d;
import Engine.Game.Entity.SkyBox;
import Engine.Game.Entity.Types.Camera3d;
import Engine.Game.Instance.AbstractGameInstance;
import Engine.Input.Input;
import Engine.Renderer.Textures.TextureLoader;
import Game.Entities.BackgroundCube;
import Game.Entities.ControllerCube;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;

import java.util.concurrent.ThreadLocalRandom;

public class ControllerInstance extends AbstractGameInstance {

   public CameraInputController cameraInputController;
   public SkyBox skybox;

   public ControllerCube[][][] controllerCubes;

   @Override
   public void init() {
      super.init();

      for (int i = 0; i < 20; i++){
         GameObject3d cubeObj = new BackgroundCube();
         float angle = ThreadLocalRandom.current().nextInt(0,360);
         float dist = ThreadLocalRandom.current().nextInt(150,300);
         float x = (float)Math.cos(Math.toRadians(angle)) * dist;
         float y = ThreadLocalRandom.current().nextInt(-100,100);
         float z = (float)Math.sin(Math.toRadians(angle)) * dist;
         float scale = ThreadLocalRandom.current().nextInt(5,40);
         cubeObj.setScale(scale,scale,scale);
         cubeObj.setPosition(x,y,z);
         addObject(cubeObj);
      }

      int testSize = 4;
      controllerCubes = new ControllerCube[4][4][4];
      for (int x = 0; x < testSize; x++){
         for (int y = 0; y < testSize; y++){
            for (int z = 0; z < testSize; z++){
               controllerCubes[x][y][z] = new ControllerCube();
               float wx = (float)(x*2)-((float)testSize);
               float wy = (float)(y*2)-((float)testSize);
               float wz = (float)(z*2)-((float)testSize);
               controllerCubes[x][y][z].setPosition(wx,wy,wz);
               if (y == 1 | y == 2){
                  controllerCubes[x][y][z].setRoom(true);
               } else {
                  controllerCubes[x][y][z].setRoom(false);
               }
               addObject(controllerCubes[x][y][z]);
            }
         }
      }

      Texture skyTexTop = TextureLoader.load("Assets/Textures/skybox/skyboxTop.png").getTex();
      Texture skyTexSide = TextureLoader.load("Assets/Textures/skybox/skyboxSide.png").getTex();
      Texture skyTexBottom = TextureLoader.load("Assets/Textures/skybox/skyboxBottom.png").getTex();
      skybox = new SkyBox(skyTexSide,skyTexSide,skyTexTop,skyTexBottom,skyTexSide,skyTexSide);

      Camera3d cam = new Camera3d();
      setCamera(cam.getCam());
      cameraInputController = new CameraInputController(camera);
      Input.addInputHandler(cameraInputController);
   }

   boolean sentPacket;

   @Override
   public void update() {
      super.update();


      if (Input.getMouseClicked() == 1){
         if (!sentPacket){
            sentPacket = true;
         }
      } else {
         sentPacket = false;
      }
   }

   @Override
   public void render() {
      startWorld();
      skybox.render(camera);
      renderModels();
      endWorld();
      renderShadows();
      
   }
   
   @Override
   public void dispose() {
      
   }
}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////