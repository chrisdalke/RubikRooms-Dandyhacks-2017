////////////////////////////////////////////////
// Development
// Chris Dalke
////////////////////////////////////////////////
// Module: LevelSelectionStage
////////////////////////////////////////////////

package Game.Stages;

import Engine.UI.Stages.UIStage;
import Game.Game;
import Game.Model.LevelDataObject;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.util.ArrayList;

import static Engine.UI.UISkinLoader.skin;
import static Game.Model.LevelDataObject.getListOfLevels;

public class LevelSelectionStage extends UIStage {

   ScrollPane scrollPane;
   Table scrollTable;

   @Override
   protected void init() {


      ArrayList<LevelDataObject> levels = getListOfLevels();

      Table scrollTable = new Table();
      for (int i = 0; i < levels.size(); i++){
         LevelDataObject level = levels.get(i);
         TextButton tmpButton = new TextButton(levels.get(i).getName()+"\n"+levels.get(i).getDescription(),skin);
         tmpButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
               Game.triggerGame(level);
            }
         });
         scrollTable.add(tmpButton).pad(20).height(100).expandX().fillX();
         if (i % 5 == 0){
            scrollTable.row();
         }
      }
      scrollTable.pack();

      scrollPane = new ScrollPane(scrollTable,skin);
      scrollPane.setFillParent(true);
      addActor(scrollPane);

      scrollPane.setFadeScrollBars(false);
      scrollPane.setClamp(true);

      
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