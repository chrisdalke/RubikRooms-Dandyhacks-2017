////////////////////////////////////////////////
// Development
// Chris Dalke
////////////////////////////////////////////////
// Module: MainMenuStage
////////////////////////////////////////////////

package Game.Stages;

import Engine.UI.Stages.UIStage;
import Engine.UI.Stages.UIStageManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import static Engine.UI.UISkinLoader.skin;

public class ControllerGameCodeStage extends UIStage {

   TextButton confirm;
   TextButton cancel;
   TextField gameCodeInput;

   Table dialogTable;

   @Override
   protected void init() {
      setDebugAll(false);

      Table table = new Table();
      table.setFillParent(true);


      Table leftTable = new Table();

      dialogTable = new Table();
      table.setFillParent(true);

      Table buttonTable = new Table();

      Label gameCodeLabel = new Label("Please enter a Game Code:",skin);
      if (getFlag("error") == 1){
         gameCodeLabel.setText("Invalid Game Code. Please try again:");
         gameCodeLabel.setColor(Color.RED);
      } else if (getFlag("error") == 2){
         gameCodeLabel.setText("Session Closed. Please enter Game Code:");
         gameCodeLabel.setColor(Color.RED);
      }

      leftTable.add(gameCodeLabel).expandX().fillX().row();

      gameCodeInput = new TextField("",skin);
      leftTable.add(gameCodeInput).expandX().fillX().pad(10).row();

      confirm = new TextButton("Connect",skin);
      cancel = new TextButton("Cancel",skin);
      confirm.addListener(new ChangeListener() {
         public void changed(ChangeEvent event, Actor actor) {
            UIStageManager.fadeOutEvent(new Runnable() {
               @Override
               public void run() {
                  Game.Game.connectControllerInstance(gameCodeInput.getText());
               }
            });
         }
      });
      cancel.addListener(new ChangeListener() {
         public void changed(ChangeEvent event, Actor actor) {
            UIStageManager.switchTo("ControllerMainMenuStage");
         }
      });

      confirm.pad(5).pack();
      cancel.pad(5).pack();
      buttonTable.add(confirm).width(300).padBottom(10).fillY().expandY().row();
      buttonTable.add(cancel).width(300).padBottom(10).fillY().expandY().row();
      buttonTable.pack();

      table.add(leftTable).expandX().fillX().pad(20);
      table.add(buttonTable).fillX().fillY().padRight(35);
      table.pack();
      addActor(table);

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