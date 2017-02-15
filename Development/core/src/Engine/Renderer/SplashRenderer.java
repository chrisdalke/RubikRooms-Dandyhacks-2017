////////////////////////////////////////////////
// Development
// Chris Dalke
////////////////////////////////////////////////
// Module: SplashRenderer
////////////////////////////////////////////////

package Engine.Renderer;

import Engine.Display.Display;
import Engine.Renderer.Textures.Texture;
import Engine.Renderer.Textures.TextureLoader;
import Engine.System.Timer.TimerManager;
import com.badlogic.gdx.graphics.Color;

//Renders a splash screen
public class SplashRenderer {
   
   static double animTimer = 0;
   static double animSpeed = 100;
   static float deltaTime = 0;
   static double startTime = 0;
   static double elapsedTime = 0;
   static int alpha = 400;
   static Texture splashTextures[];
   static int frame = 0;
   
   static boolean isDone;
   static int state = 1;
   
   public static void init() {
      splashTextures = new Texture[28];
      for (int i = 1; i <= 28; i++){
         splashTextures[i-1] = TextureLoader.load("Assets/Textures/splash/splash"+i+".png");
      }

      startTime = TimerManager.getTime();


   }
   
   static public boolean isDone(){
      return isDone;
      
   }
   static public void display(){
      //Hijacks the display loop to show a splash screen

      elapsedTime = TimerManager.getTime() - startTime;

      if (!isDone){

         //Animate
         if (TimerManager.getTime()-animTimer > animSpeed){
            animTimer = TimerManager.getTime();
            frame++;
         }
         if (frame > 27){
            frame = 27;
         }
         
         switch (state){
            case 1:
               //Fade in
               alpha = 300 - (int)(elapsedTime/3);
               if (alpha <= 0){
                  alpha = 0;
                  state = 2;
                  startTime = TimerManager.getTime();
               }
               break;
            case 2:
               //Wait a bit
               if (elapsedTime > 2000){
                  state = 3;
                  startTime = TimerManager.getTime();
               }
               break;
            case 3:
               //Fade out
               alpha = (int)(elapsedTime/4);
               if (alpha >= 255){
                  alpha = 255;
                  state = 4;
                  startTime = TimerManager.getTime();
                  isDone = true;
               }
               break;
         }
         
         //Display our sprites

         Shapes.begin(Renderer.cameraUI);
         Shapes.setColor(255,255,255,255);
         Shapes.drawBox(0,0,(float)Display.getWidth(),(float)Display.getHeight());
         Shapes.end();

         Renderer.startUI();

         Renderer.setColor(Color.BLACK);
         Text.draw(0,0,"State: "+state);
         Renderer.setColor(Color.WHITE);

         Renderer.draw(splashTextures[frame % 28].getRegion(),(float) Display.getWidth()/2 - 128, (float)Display.getHeight()/2-128,256,256);

         Renderer.endUI();

         Shapes.begin(Renderer.cameraUI);
         Shapes.setColor(0,0,0,Math.min(255,alpha));
         Shapes.drawBox(0,0,(float)Display.getWidth(),(float)Display.getHeight());
         Shapes.end();

      }
   }
   
}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////