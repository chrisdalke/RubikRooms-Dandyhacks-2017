////////////////////////////////////////////////
// Development
// Chris Dalke
////////////////////////////////////////////////
// Module: GameStage
////////////////////////////////////////////////

package Game.Stages;

import Engine.UI.Stages.UIStage;
import Game.Game;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import static Engine.UI.UISkinLoader.skin;

public class GameStage extends UIStage {

   @Override
   protected void init() {

      TextButton menu = new TextButton("Exit to Menu",skin);
      //addActor(menu);
      menu.addListener(new ChangeListener() {
         public void changed(ChangeListener.ChangeEvent event, Actor actor) {
            Game.triggerMenu();
         }
      });
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