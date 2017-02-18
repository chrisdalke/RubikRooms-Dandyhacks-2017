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
import Engine.System.Logging.Logger;
import Game.Entities.BackgroundCube;
import Game.Entities.ControllerCube;
import Game.Entities.ToggleableCameraInputController;
import Game.Model.LevelDataObject;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ai.steer.utils.paths.LinePath;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Array;

import java.util.concurrent.ThreadLocalRandom;

import static Game.Model.LevelDataObject.PLANE.X;
import static Game.Model.LevelDataObject.PLANE.Y;
import static Game.Model.LevelDataObject.PLANE.Z;

public class ControllerInstance extends AbstractGameInstance implements InputProcessor {

   public ToggleableCameraInputController cameraInputController;
   public SkyBox skybox;


   public LevelDataObject testLevel;
   public ControllerCube[][][] controllerCubes;

   @Override
   public void init() {
      shadowViewportSize = 80f;
      hasShadows = false;
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
      testLevel = new LevelDataObject(testSize);

      controllerCubes = new ControllerCube[testSize][testSize][testSize];
      for (int x = 0; x < testSize; x++){
         for (int y = 0; y < testSize; y++){
            for (int z = 0; z < testSize; z++){
               controllerCubes[x][y][z] = new ControllerCube();
               if (y == 1 | y == 2){
                  controllerCubes[x][y][z].setRoom(true);
               } else {
                  //controllerCubes[x][y][z].setRoom(false);
               }
               addObject(controllerCubes[x][y][z]);
            }
         }
      }
      updateCubes(); //Update the position of the cubes.

      Texture skyTexTop = TextureLoader.load("Assets/Textures/skybox/skyboxTop.png").getTex();
      Texture skyTexSide = TextureLoader.load("Assets/Textures/skybox/skyboxSide.png").getTex();
      Texture skyTexBottom = TextureLoader.load("Assets/Textures/skybox/skyboxBottom.png").getTex();
      skybox = new SkyBox(skyTexSide,skyTexSide,skyTexTop,skyTexBottom,skyTexSide,skyTexSide);

      Camera3d cam = new Camera3d();
      setCamera(cam.getCam());
      cam.getCam().fieldOfView = 60f;
      cameraInputController = new ToggleableCameraInputController(camera);
      Input.addInputHandler(this);
      Input.addInputHandler(cameraInputController);
      cameraInputController.setEnabled(true);
      cameraInputController.zoom(testLevel.getSize()*-4.0f);

   }

   boolean camEnabled = false;
   boolean sentPacket;
   float center;

   public void updateCubes(){
      //Update the positions of the room cubes based on the room objects
      for (int x = 0; x < testLevel.getSize(); x++) {
         for (int y = 0; y < testLevel.getSize(); y++) {
            for (int z = 0; z < testLevel.getSize(); z++) {
               Matrix4 tempTransform = testLevel.getRoom(x, y, z).getWorldTransform();
               Vector3 pos = new Vector3();
               Quaternion rotation = new Quaternion();
               tempTransform.getTranslation(pos);
               tempTransform.getRotation(rotation);

               //  | . | . | . |
               center = (testLevel.getSize()-1);
               float wx = pos.x * 2 - center;
               float wy = pos.y * 2 - center;
               float wz = pos.z * 2 - center;

               controllerCubes[x][y][z].setPosition(wx, wy, wz);
               controllerCubes[x][y][z].setRotation(rotation.getYaw(), rotation.getPitch(), rotation.getRoll());
            }
         }
      }
   }

   @Override
   public void update() {
      super.update();

      testLevel.update();
      updateCubes();

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
      //Update camera
      cameraInputController.update();

      startWorld();
      skybox.render(camera);
      renderModels();
      endWorld();
      renderShadows();

      /*
      Shapes.begin(Renderer.cameraUI);
      Shapes.setColor(255,0,0,255);
      Shapes.drawBox(0,(float) Display.getHeight()/2,(float) Display.getWidth(),1);
      Shapes.drawBox((float) Display.getWidth()/2,0,1,(float) Display.getHeight());
      Shapes.end();
      */
   }
   
   @Override
   public void dispose() {
      
   }

   ////////////////////////////////////////////////
   // Gesture Detection
   ////////////////////////////////////////////////

   //Gestures we don't use
   public boolean keyDown (int keycode) { return false; }
   public boolean keyUp (int keycode) { return false; }
   public boolean keyTyped (char character) { return false; }
   public boolean mouseMoved (int x, int y) { return false; }
   public boolean scrolled (int amount) { return false; }

   public boolean touchDown (int x, int y, int pointer, int button) {

      //Mouse has been detected as down
      //Turn on dragging if we detect that we are over a rubiks cube object
      GameObject3d pickObj = getObjectStartDrag(x,y);
      if (pickObj != null){ //&  !cameraInputController.consumingDrag
         isDragging = true;
         cameraInputController.setEnabled(false);
         return true;
      } else {
         isDragging = false;
         cameraInputController.setEnabled(true);
         //cameraInputController.touchDown(x,y,pointer,button);
         return false;
      }

   }

   public boolean touchUp (int x, int y, int pointer, int button) {


      if (isDragging){
         dragMode = 0;
      }
      cameraInputController.setEnabled(true);
      isDragging = false;


      return false;
   }



   //Dragging logic code
   public boolean isDragging;
   float dragAngle;
   float dragStartAngle;
   LevelDataObject.PLANE dragPlane;
   LevelDataObject.PLANE dragNormalPlane;
   int dragIndex;
   int dragMode;
   float dragDirection;
   Vector3 dragStartPosition;
   Vector3 dragEndPosition;
   Vector3 dragPlaneVector;
   float dragDistanceThreshold = 0.8f;
   float dragPlaneDistanceFromOrigin = 0;
   float isReversedPlane = 0;

   public boolean touchDragged (int x, int y, int pointer) {

      if (isDragging){
         //Calculate the drag rotation of the object if we are dragging it

         switch (dragMode){
            case 0: { //Drag mode 0 aka the drag just started and we are in 'evaluation' mode

               //Find the 3d position of the drag along a plane along normal we previously found
               Ray ray = camera.getPickRay(x, y);
               Vector3 rayTouchPoint = new Vector3();
               int lvlSize = (testLevel.getSize());

               //Intersector.intersectRayPlane(ray,new Plane(dragPlaneVector,dragPlaneDistanceFromOrigin),rayTouchPoint)

               if (Intersector.intersectRayBounds(ray, new BoundingBox(new Vector3(-lvlSize,-lvlSize,-lvlSize),new Vector3(lvlSize,lvlSize,lvlSize)),rayTouchPoint)){
                  dragEndPosition = rayTouchPoint;
                  //Wait until the distance exceeds the threshold we have set
                  if (getVectorDistance(dragStartPosition, dragEndPosition) > dragDistanceThreshold){
                     //Determine which axis has a larger value and use that to choose which plane we are on
                     float distXa = (dragStartPosition.x - dragEndPosition.x);
                     float distYa = (dragStartPosition.y - dragEndPosition.y);
                     float distZa = (dragStartPosition.z - dragEndPosition.z);
                     float distX = Math.abs(distXa);
                     float distY = Math.abs(distYa);
                     float distZ = Math.abs(distZa);

                     Logger.log("Drag plane position: "+dragPlaneDistanceFromOrigin);
                     Logger.log("Finished the drag with a delta of "+distX+" "+distY+" "+distZ);
                     Logger.log("Drag end position:");
                     printVector(dragEndPosition);

                     switch (dragNormalPlane){
                        case X:{
                           //Compare Y and Z, y larger = Z plane, z larger = Y plane
                           if (distY > distZ){
                              dragPlane = Z;
                           } else {
                              dragPlane = Y;
                           }
                           break;
                        }
                        case Y:{
                           //Compare X and Z, x larger = Z plane, z larger = X plane
                           if (distX > distZ){
                              dragPlane = Z;
                           } else {
                              dragPlane = X;
                           }
                           break;
                        }
                        case Z:{
                           //Compare X and Y, x larger = Y plane, y larger = X plane
                           if (distX > distY){
                              dragPlane = Y;
                           } else {
                              dragPlane = X;
                           }
                           break;
                        }
                     }

                     //Determine the index of the plane based on original position
                     switch (dragPlane){
                        case X:{
                           dragDirection = findVectorWindingDirection(dragStartPosition,dragEndPosition,X);
                           Logger.log("Decided to rotate around X plane");
                           dragIndex = Math.min(Math.max(Math.round((dragStartPosition.x + center)/2.0f),0),testLevel.getSize()-1);
                           dragPlaneVector = xVectorPlane;
                           dragStartPosition = new Vector3((dragIndex * 2) - center,0,0);
                           break;
                        }
                        case Y:{
                           dragDirection = findVectorWindingDirection(dragStartPosition,dragEndPosition,Y);
                           Logger.log("Decided to rotate around Y plane");
                           dragIndex = Math.min(Math.max(Math.round((dragStartPosition.y + center)/2.0f),0),testLevel.getSize()-1);
                           dragPlaneVector = yVectorPlane;
                           dragStartPosition = new Vector3(0,(dragIndex * 2) - center,0);
                           break;
                        }
                        case Z:{
                           dragDirection = findVectorWindingDirection(dragStartPosition,dragEndPosition,Z);
                           Logger.log("Decided to rotate around Z plane");
                           dragIndex = Math.min(Math.max(Math.round((dragStartPosition.z + center)/2.0f),0),testLevel.getSize()-1);
                           dragPlaneVector = zVectorPlane;
                           dragStartPosition = new Vector3(0,0,(dragIndex * 2) - center);
                           break;
                        }
                     }
                     dragMode = 1;
                     Logger.log("Chose index "+dragIndex);

                     //Calculate drag start angle; should always start at 0.
                     if (Intersector.intersectRayPlane(ray,new Plane(dragPlaneVector,center + testLevel.getSize()),rayTouchPoint)){
                        switch (dragPlane){
                           case X:{
                              dragStartAngle = MathUtils.atan2(rayTouchPoint.y - dragStartPosition.y,rayTouchPoint.z - dragStartPosition.z) * MathUtils.radiansToDegrees;
                              break;
                           }
                           case Y:{
                              dragStartAngle = MathUtils.atan2(rayTouchPoint.x - dragStartPosition.x,rayTouchPoint.z - dragStartPosition.z) * MathUtils.radiansToDegrees;
                              break;
                           }
                           case Z:{
                              dragStartAngle = MathUtils.atan2(rayTouchPoint.x - dragStartPosition.x,rayTouchPoint.y - dragStartPosition.y) * MathUtils.radiansToDegrees;
                              break;
                           }
                        }
                     }

                  }
               }
               break;
            }
            case 1: {

               //Drag mode 1 aka we have decided what plane and will start actually triggering rotations
               //Calculate the drag direction and apply this!
               //The levelDataObject itself will automatically handle the rotation interpolation.
               Logger.log("Triggering drag of direction: "+dragDirection);
               if (dragDirection > 0) {
                  testLevel.triggerRotatePlane(dragPlane, dragIndex, LevelDataObject.PLANE_ROTATION.NINETY, 5);
               } else {
                  testLevel.triggerRotatePlane(dragPlane,dragIndex, LevelDataObject.PLANE_ROTATION.TWO_SEVENTY, -5);
               }
               isDragging = false;
               break;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   //Determines whether a given vector points clockwise or counterclockwise around a certain axis.
   public float findVectorWindingDirection(Vector3 startVec, Vector3 endVec, LevelDataObject.PLANE plane){
      //Figure out an angle and check the delta on that angle
      float angleA = 0;
      float angleB = 0;

      switch (plane){
         case X:{
            angleA = MathUtils.atan2(startVec.y, startVec.z) * MathUtils.radiansToDegrees;
            angleB = MathUtils.atan2(endVec.y, endVec.z) * MathUtils.radiansToDegrees;
            if (angleA < angleB){
               return -1;
            }  else {
               return 1;
            }
         }
         case Y:{
            angleA = MathUtils.atan2(startVec.x,startVec.z) * MathUtils.radiansToDegrees;
            angleB = MathUtils.atan2(endVec.x,endVec.z) * MathUtils.radiansToDegrees;
            if (angleA < angleB){
               return 1;
            }  else {
               return -1;
            }
         }
         case Z:{
            angleA = MathUtils.atan2(startVec.x,startVec.y) * MathUtils.radiansToDegrees;
            angleB = MathUtils.atan2(endVec.x,endVec.y) * MathUtils.radiansToDegrees;
            if (angleA < angleB){
               return -1;
            }  else {
               return 1;
            }
         }
      }
      return 0;
   }


   public Vector3 xVectorPlane = new Vector3(1,0,0);
   public Vector3 yVectorPlane = new Vector3(0,1,0);
   public Vector3 zVectorPlane = new Vector3(0,0,1);

   public GameObject3d getObjectStartDrag (int screenX, int screenY) {

      calculateClosestIntersection(screenX,screenY);

      if (lastIntersectionObject != null) {
         //Based on the ray touch point, determine what face we are in by which difference magnitudes are greatest
         float diffX = Math.abs(lastIntersectionPoint.x - lastIntersectionObject.getX());
         float diffY = Math.abs(lastIntersectionPoint.y - lastIntersectionObject.getY());
         float diffZ = Math.abs(lastIntersectionPoint.z - lastIntersectionObject.getZ());

         //Find the largest of the three values
         float largest = Math.max(Math.max(diffX, diffY), diffZ);
         dragNormalPlane = X;

         dragStartPosition = lastIntersectionPoint;
         dragEndPosition = lastIntersectionPoint;
         dragMode = 0;

         Logger.log("Drag starting position:");
         printVector(dragStartPosition);

         //Find out the face of the drag
         if (largest == diffX) {
            dragNormalPlane = X;
         }
         if (largest == diffY) {
            dragNormalPlane = Y;
         }
         if (largest == diffZ) {
            dragNormalPlane = Z;
         }

         //dragFace identifies the normal of the face that we are touching
         switch (dragNormalPlane) {
            case X: {
               //Normal is along X axis
               dragPlaneDistanceFromOrigin = lastIntersectionPoint.x;
               dragPlaneVector = xVectorPlane;
               Logger.log("Drag plane: X");
               break;
            }
            case Y: {
               //Normal is along Y axis
               dragPlaneDistanceFromOrigin = lastIntersectionPoint.y;
               dragPlaneVector = yVectorPlane;
               Logger.log("Drag plane: Y");
               break;
            }
            case Z: {
               //Normal is along Z axis
               dragPlaneDistanceFromOrigin = lastIntersectionPoint.z;
               dragPlaneVector = zVectorPlane;
               Logger.log("Drag plane: Z");
               break;
            }
         }
      }
      return lastIntersectionObject;
   }

   public Vector3 lastIntersectionPoint;
   public GameObject3d lastIntersectionObject;

   public void calculateClosestIntersection(int screenX, int screenY){
      Ray ray = camera.getPickRay(screenX, screenY);
      float objCollisionRadius = 1;
      float objMinDistance = 99999;
      lastIntersectionPoint = null;
      lastIntersectionObject = null;
      Vector3 rayTouchPoint = new Vector3();
      for (GameObject3d obj : levelObjects) {
         if (Intersector.intersectRayBounds(ray, new BoundingBox(new Vector3(obj.getX()-objCollisionRadius,obj.getY()-objCollisionRadius,obj.getZ()-objCollisionRadius),new Vector3(obj.getX()+objCollisionRadius,obj.getY()+objCollisionRadius,obj.getZ()+objCollisionRadius)),rayTouchPoint)) {
            float dist = getVectorDistance(camera.position,rayTouchPoint);
            if (dist < objMinDistance) {
               objMinDistance = dist;
               lastIntersectionObject = obj;
               lastIntersectionPoint = new Vector3(rayTouchPoint);
            }
         }
      }
   }

   public float getVectorDistance(Vector3 vec1, Vector3 vec2){
      Array<Vector3> vecPath = new Array<>(true,2);
      vecPath.add(vec1);
      vecPath.add(vec2);
      return new LinePath<Vector3>(vecPath).getLength();
   }

   public void printVector(Vector3 vec){
      Logger.log("["+vec.x+" "+vec.y+" "+vec.z+"]");
   }

}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////