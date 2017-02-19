////////////////////////////////////////////////
// Development
// Chris Dalke
////////////////////////////////////////////////
// Module: MainMenuStage
////////////////////////////////////////////////

package Game.Stages;

import Engine.Renderer.Textures.TextureLoader;
import Engine.UI.Stages.UIStage;
import Engine.UI.Stages.UIStageManager;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import static Engine.UI.UISkinLoader.skin;

public class ControllerMainMenuStage extends UIStage {

   TextButton play;
   TextButton options;


   @Override
   protected void init() {
      setDebugAll(false);

      Table table = new Table();
      table.setFillParent(true);
      addActor(table);

      Table buttonTable = new Table();

      Image titleImg = new Image(TextureLoader.load("Assets/Textures/title.png").getRegion());

      play = new TextButton("Play",skin);
      options = new TextButton("Options",skin);
      play.addListener(new ChangeListener() {
         public void changed(ChangeEvent event, Actor actor) {
            UIStageManager.switchTo("ControllerGameCodeStage");
         }
      });

      play.pad(5).pack();
      options.pad(5).pack();
      buttonTable.add(play).width(300).padBottom(10).fillY().expandY().row();
      buttonTable.add(options).width(300).padBottom(10).fillY().expandY().row();
      buttonTable.pack();

      table.add(titleImg).expandX().fillX();
      table.add(buttonTable).fillX().fillY().padRight(35);
      table.pack();

   }
   
   @Override
   protected void update() {
      
   }
   
   @Override
   protected void render() {
      
   }
}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////