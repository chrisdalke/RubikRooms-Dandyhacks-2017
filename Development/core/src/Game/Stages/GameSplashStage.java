////////////////////////////////////////////////
// Development
// Chris Dalke
////////////////////////////////////////////////
// Module: GameStage
////////////////////////////////////////////////

package Game.Stages;

import Engine.Renderer.Textures.Texture;
import Engine.Renderer.Textures.TextureLoader;
import Engine.System.Timer.TimerManager;
import Engine.UI.Stages.UIStage;
import Engine.UI.Stages.UIStageManager;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import static Engine.UI.UISkinLoader.skin;

public class GameSplashStage extends UIStage {

   Texture pixelTex;

   double curTime;
   boolean doingTransition;

   @Override
   protected void init() {

      pixelTex = TextureLoader.load("Assets/Textures/pixel.png");

      Table table = new Table();
      table.setFillParent(true);
      addActor(table);

      Label title = new Label("Untitled Level",skin);
      Label description = new Label("Description Goes Here",skin);

      table.add(title).padBottom(10).row();
      table.add(description).row();
      table.pack();

      curTime = TimerManager.getTime();
      doingTransition = false;
   }
   
   @Override
   protected void update() {

      if (TimerManager.getTime() - curTime > 3000 & !doingTransition){
         UIStageManager.switchTo("GameStage");
         doingTransition = true;
      }
   }
   
   @Override
   protected void render() {
      //Renderer.draw(pixelTex.getRegion(),0,0,(float) Display.getWidth(),(float)Display.getHeight());
   }
}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////