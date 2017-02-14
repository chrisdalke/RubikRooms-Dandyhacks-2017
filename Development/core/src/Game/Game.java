////////////////////////////////////////////////
// The Pyramid's Curse
// Chris Dalke
////////////////////////////////////////////////
// Module: Game
////////////////////////////////////////////////

package Game;

import Engine.Renderer.Renderer;
import Engine.System.Config.Configuration;
import Engine.System.Timer.DeltaTimeManager;
import Engine.UI.Stages.UIStageManager;
import Game.Instances.AbstractGameInstance;
import Game.Instances.GameInstance;
import Game.Instances.TestGameInstance;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class Game {

    //Handles the entirety of the game. This includes menu,
    //handling of game instances, all that stuff.
    //For now, menu is disabled, we just create a game instance and start that.
    
    static DeltaTimeManager deltaTimeManager;
    static final double TICK_RATE = 1000.0/60.0;
    private static int currentFrame;
    private static Configuration savedConfig;

    public static AbstractGameInstance currentGame;

    public static boolean isPaused;

    public static boolean isPaused() {
        return isPaused;
    }

    public static void setPaused(boolean isPaused) {
        Game.isPaused = isPaused;
    }

    public static GameInstance getGame(){
        if (currentGame != null) {
            if (currentGame instanceof GameInstance){
                return (GameInstance)currentGame;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public static void init(Configuration config){

        savedConfig = config;

        deltaTimeManager = new DeltaTimeManager(TICK_RATE);
        deltaTimeManager.startInterval();
        
        currentGame = new TestGameInstance();
        currentGame.init(config);

        //Setup user interface stages
        UIStageManager.init();
        /*
        UIStageManager.addStage("mainMenuStage",new MainMenuStage());
        UIStageManager.addStage("ingameStage",new IngameStage());
        UIStageManager.addStage("levelSelectStage",new LevelSelectStage());
        UIStageManager.addStage("levelEditorStage",new LevelEditorStage());
        */

    }

    public static void update(){
        deltaTimeManager.endInterval();
        while (deltaTimeManager.consumeTick()) {
            //Update game
            currentFrame++;

            if (currentGame != null) {
                currentGame.update();
            }
        }

        deltaTimeManager.startInterval();
    }

    public static void render(){

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glDepthFunc(GL20.GL_GREATER);

        if (currentGame != null) {
            currentGame.render();
        }

        //Render General purpose UI Stuff here
        Renderer.startUI();

        //Render UI stage
        UIStageManager.render();

        Renderer.endUI();
    }

    public static void dispose(){
        if (currentGame != null) {
            currentGame.dispose();
            currentGame = null;
        }
        UIStageManager.dispose();
    }

}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////