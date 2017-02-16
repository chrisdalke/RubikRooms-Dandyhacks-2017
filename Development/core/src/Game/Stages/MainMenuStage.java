////////////////////////////////////////////////
// Development
// Chris Dalke
////////////////////////////////////////////////
// Module: MainMenuStage
////////////////////////////////////////////////

package Game.Stages;

import Engine.Renderer.Textures.TextureLoader;
import Engine.System.System;
import Engine.UI.Elements.UIModalWindow;
import Engine.UI.Stages.UIStage;
import Engine.UI.Stages.UIStageManager;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import static Engine.UI.UISkinLoader.skin;

public class MainMenuStage extends UIStage {

   TextButton levels;
   TextButton options;
   TextButton editor;
   TextButton quit;
   
   @Override
   protected void init() {
      //setDebugAll(true);

      Table table = new Table();
      table.setFillParent(true);
      addActor(table);

      Table buttonTable = new Table();

      Image titleImg = new Image(TextureLoader.load("Assets/Textures/title.png").getRegion());

      levels = new TextButton("Levels",skin);
      options = new TextButton("Options",skin);
      editor = new TextButton("Editor",skin);
      quit = new TextButton("Quit",skin);
      levels.addListener(new ChangeListener() {
         public void changed(ChangeListener.ChangeEvent event, Actor actor) {
            UIStageManager.switchTo("LevelSelectionStage");
         }
      });
      quit.addListener(new ChangeListener() {
         @Override
         public void changed(ChangeEvent event, Actor actor) {
            UIModalWindow.createYesNoDialog(MainMenuStage.this, "Quit Game", "Area you sure you want to quit?", new Runnable() {
               @Override
               public void run() {
                  System.setRunning(false);
               }
            },1);
         }
      });
      levels.pad(5).pack();
      options.pad(5).pack();
      editor.pad(5).pack();
      quit.pad(5).pack();

      buttonTable.add(levels).width(300).padBottom(10).row();
      buttonTable.add(options).width(300).padBottom(10).row();
      buttonTable.add(editor).width(300).padBottom(10).row();
      buttonTable.add(quit).width(300).padBottom(10).row();
      buttonTable.pack();

      table.add(titleImg).expandX().fillX();
      table.add(buttonTable).fillX().padRight(35);
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