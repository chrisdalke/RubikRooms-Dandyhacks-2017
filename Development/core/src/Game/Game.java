////////////////////////////////////////////////
// Frame Engine
// Chris Dalke
////////////////////////////////////////////////
// Module: Game
////////////////////////////////////////////////

package Game;

import Engine.Audio.Audio;
import Engine.Game.Instance.AbstractGameInstance;
import Engine.Input.Input;
import Engine.Networking.Tests.ClientTest;
import Engine.Networking.Tests.ServerTest;
import Engine.Renderer.Renderer;
import Engine.Renderer.Textures.Texture;
import Engine.Renderer.Textures.TextureLoader;
import Engine.System.Config.Configuration;
import Engine.System.Platforms.PlatformManager;
import Engine.System.Timer.DeltaTimeManager;
import Engine.System.Utility.MethodInvoker;
import Engine.UI.Stages.UIStageManager;
import Game.Instances.GameInstance;
import Game.Instances.MenuInstance;
import Game.Stages.GameStage;
import Game.Stages.LevelSelectionStage;
import Game.Stages.MainMenuStage;
import Game.Stages.OptionsStage;

import java.util.ArrayList;

public class Game {

    ////////////////////////////////////////////////
    // Game Controller: Variables
    ////////////////////////////////////////////////

    // Game subsystems
    private static DeltaTimeManager deltaTimeManager;
    static ArrayList<MethodInvoker> methodInvokers;

    // Constant game properties
    private static final double TICK_RATE = 1000.0/60.0;

    // Game variable properties
    public static boolean isPaused;

    // Game instances
    private static AbstractGameInstance gameInstance;
    private static AbstractGameInstance menuInstance;

    ////////////////////////////////////////////////
    // Game Controller: Initialization
    ////////////////////////////////////////////////
    
    public static void init(Configuration config){

        deltaTimeManager = new DeltaTimeManager(TICK_RATE);
        deltaTimeManager.startInterval();
        methodInvokers = new ArrayList<MethodInvoker>();
        
        // Set up the global interface
        UIStageManager.init();
        UIStageManager.addStage("GameStage",new GameStage());
        UIStageManager.addStage("LevelSelectionStage",new LevelSelectionStage());
        UIStageManager.addStage("MainMenuStage",new MainMenuStage());
        UIStageManager.addStage("OptionsStage",new OptionsStage());
        UIStageManager.switchTo("GameStage");
        
        // Set up the game instance / menu instance
        gameInstance = new GameInstance();
        gameInstance.init(config);
        menuInstance = new MenuInstance();
        menuInstance.init(config);

        /*
        if (PlatformManager.getPlatform() == PlatformManager.IOS){
            // Load the controller instance
            gameInstance = new ControllerInstance();
            gameInstance.init(config);
        }
        */

        //Start test networking
        if (PlatformManager.getPlatform() == PlatformManager.IOS){
            ClientTest.startClient("10.4.132.104",54555, 54556);
        } else {
            ServerTest.startServer();
        }
    }
    static boolean sentPacket;


    ////////////////////////////////////////////////
    // Game Controller: Game State Methods
    ////////////////////////////////////////////////
    // These methods cause a state transition between
    // menu and game, and also between pieces of the menu.
    // Useful to have these in a globally available context.


    public static void triggerMenu(){
        // Do nothing yet
    }

    public static void triggerGame(){
        // Do nothing yet
    }
    ////////////////////////////////////////////////
    // Game Controller: Main Loop
    ////////////////////////////////////////////////
    
    public static void update(){
        //Update delta timer
        deltaTimeManager.endInterval();
        deltaTimeManager.startInterval();
    
        //Update subsystems
        Audio.update();

        while (deltaTimeManager.consumeTick()) {
        
            //Update the command queue
            for (MethodInvoker i : methodInvokers){
                i.run();
            }
            methodInvokers.clear();


            //Test iOS networking
            if (PlatformManager.getPlatform() == PlatformManager.IOS) {
                if (Input.getMouseClicked() == 1) {
                    if (!sentPacket) {
                        sentPacket = true;
                        ClientTest.clientSendString("Message from IOS!!");
                    }
                } else {
                    sentPacket = false;
                }
            }

        
            if (gameInstance != null) {

                if (!isPaused) {
                    //Update the instance
                    gameInstance.update();
                
                    //Pause check
                    if (Input.getKeyPress(com.badlogic.gdx.Input.Keys.ESCAPE)){
                        Game.setPaused(true);
                    }
                } else {
                    //Handle pause status
                    //unpause check
                    if (Input.getKeyPress(com.badlogic.gdx.Input.Keys.ESCAPE)){
                        Game.setPaused(false);
                    }
                }
            } else {
                //Update the main menu game instance
                menuInstance.update();
            }
        }
    }

    public static void render(){

        //Render game stuff here
        if (gameInstance != null){
            //Render the game's view
            gameInstance.render();
        } else {
            //Render the menu instance's view
            menuInstance.render();
        }

        //Render Scene2d UI layers
        //This is all above the in-game UI layer
        Renderer.startUI();
        UIStageManager.render();
        Renderer.endUI();
    }
    
    ////////////////////////////////////////////////
    // Game Controller: Cleanup
    ////////////////////////////////////////////////
    
    public static void dispose(){
        if (gameInstance != null) {
            gameInstance.dispose();
            gameInstance = null;
        }
        //Dispose of any extra game resources that we won't need.
        UIStageManager.dispose();
    }

    ////////////////////////////////////////////////
    // Game Controller: Utility Methods
    ////////////////////////////////////////////////

    public static AbstractGameInstance getGameInstance(){
        return gameInstance;
    }

    public static void setGameInstance(AbstractGameInstance newGame){
        gameInstance = newGame;
    }

    public static boolean isPaused() {
        return isPaused;
    }
    
    public static void setPaused(boolean isPaused) {
        Game.isPaused = isPaused;
    }

    public static void queueMethodInvoker(MethodInvoker item){
        methodInvokers.add(item);
    }


}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////