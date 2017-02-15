////////////////////////////////////////////////
// Development
// Chris Dalke
////////////////////////////////////////////////
// Module: GameStage
////////////////////////////////////////////////

package Game.Stages;

import Engine.UI.Stages.UIStage;
import Engine.UI.Stages.UIStageManager;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import static Engine.UI.UISkinLoader.skin;

public class GameStage extends UIStage {

   @Override
   protected void init() {

      TextButton store = new TextButton("Toggle Store",skin);
      addActor(store);
      store.addListener(new ChangeListener() {
         public void changed(ChangeListener.ChangeEvent event, Actor actor) {
            UIStageManager.fadeOutEvent(new Runnable() {
               @Override
               public void run() {
                  UIStageManager.switchTo("GameStage");
               }
            });
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