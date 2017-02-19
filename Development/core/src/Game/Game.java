////////////////////////////////////////////////
// Frame Engine
// Chris Dalke
////////////////////////////////////////////////
// Module: Game
////////////////////////////////////////////////

package Game;

import Engine.Audio.Audio;
import Engine.Display.Display;
import Engine.Game.Instance.AbstractGameInstance;
import Engine.Input.Input;
import Engine.Networking.Networking;
import Engine.Renderer.Renderer;
import Engine.Renderer.Shapes;
import Engine.Renderer.Text;
import Engine.System.Commands.Commands;
import Engine.System.Config.Configuration;
import Engine.System.Logging.Logger;
import Engine.System.Platforms.PlatformManager;
import Engine.System.Timer.DeltaTimeManager;
import Engine.System.Utility.MethodInvoker;
import Engine.UI.Stages.UIStageManager;
import Game.Instances.ControllerInstance;
import Game.Instances.GameInstance;
import Game.Instances.MenuInstance;
import Game.Model.LevelDataObject;
import Game.Stages.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

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
    private static String gameCode;

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
        UIStageManager.addStage("GameSplashStage",new GameSplashStage());

        if (PlatformManager.getPlatform() != PlatformManager.IOS){
            UIStageManager.addStage("LevelSelectionStage",new LevelSelectionStage());
        }
        UIStageManager.addStage("ControllerStage",new ControllerStage());
        UIStageManager.addStage("MainMenuStage",new MainMenuStage());
        UIStageManager.addStage("ControllerMainMenuStage",new ControllerMainMenuStage());
        UIStageManager.addStage("ControllerGameCodeStage",new ControllerGameCodeStage());
        UIStageManager.addStage("OptionsStage",new OptionsStage());
        
        // Set up the game instance / menu instance
        //PlatformManager.setPlatform(1);

        if (PlatformManager.getPlatform() == PlatformManager.IOS){
            //IOS / ControllerMode
            // Load the controller menu
            menuInstance = new MenuInstance();
            menuInstance.init();
            UIStageManager.switchTo("ControllerMainMenuStage");

            gameInstance = new ControllerInstance();
            gameInstance.init();
            UIStageManager.switchTo("ControllerStage");
        } else {
            //Start desktop menu
            menuInstance = new MenuInstance();
            menuInstance.init();
            UIStageManager.switchTo("MainMenuStage");
            //gameInstance = new GameInstance();
            //gameInstance.init();
            //triggerGame(null);

            //We are on desktop, so start the server
            Networking.startServer();

        }

        //LevelDataObject.test();

    }

    ////////////////////////////////////////////////
    // Game Controller: Game State Methods
    ////////////////////////////////////////////////
    // These methods cause a state transition between
    // menu and game, and also between pieces of the menu.
    // Useful to have these in a globally available context.

    public static void connectControllerInstance(String connectionCode){

        Networking.startClient(connectionCode);

        //Check if we successfully started a session.
        //If we didn't start a session, return to menu and display error message.
        if (Networking.clientRunning){
             //Create client game instance
            gameInstance = new ControllerInstance();
            gameInstance.init();
            UIStageManager.switchTo("ControllerStage");
        } else {
            //Client isn't running; networking attempt has failed.
            //Return to the menu with an error message.
            UIStageManager.switchTo("ControllerGameCodeStage");
            UIStageManager.getCurrentStage().setFlag("error",1);
            UIStageManager.getCurrentStage().reset();
        }

    }


    public static void triggerMenu(){
        UIStageManager.fadeOutEvent(new Runnable() {
            @Override
            public void run() {
                gameInstance = null;
                UIStageManager.switchTo("MainMenuStage");
            }
        });
    }


    public static void triggerGame(LevelDataObject level){
        UIStageManager.fadeOutEvent(new Runnable() {
            @Override
            public void run() {
                Logger.log("Running game with instance "+level.getName());
                gameInstance = new GameInstance(level);
                gameInstance.init();
                setPaused(false);
                UIStageManager.switchTo("GameSplashStage");
            }
        });
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
        //Update networking
        Networking.updateServer();

        while (deltaTimeManager.consumeTick()) {
        
            //Update the command queue
            for (MethodInvoker i : methodInvokers){
                i.run();
            }
            methodInvokers.clear();

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
        Commands.render();

        //Draw controller status
        //If we are on Desktop, render a UI element in the bottom corner that displays if a controller is connected.
        //Make this element smaller when we are in the game.
        if (PlatformManager.getPlatform() != 1) {
            String text = "";
            boolean connected = false;
            if (Networking.getServerSession().getConnections().length < 1) {
                text = "No Controller! Game Code: " + Networking.getGameCode();
            } else {
                text = "Controller Connected";
                connected = true;
            }

            Shapes.begin(Renderer.cameraUI);
            Shapes.setColor(255, 255, 255, 40);
            float height = Text.getTextHeight(text);
            float width = Text.getTextWidth(text) + 20;
            if (!connected){
                width = (float) Display.getWidth();
            }
            Shapes.drawBox(0, 0, width, height + 20);
            Shapes.end();
            Renderer.startUI();
            Text.setFont(3);
            Text.setColor(Color.BLACK);
            if (connected){
                Text.setColor(Color.GREEN);
            }
            Text.draw(10, 10 + height, text);
            Text.setColor(Color.WHITE);
            Renderer.endUI();
        }


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

        //Dispose of networking server.
        Networking.stopServer();
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
        if (isPaused){
            Display.showCursor();
            Gdx.input.setCursorPosition((int)Display.getWidth()/2,(int)Display.getHeight()/2);
        } else {
            Display.hideCursor();

        }
    }

    public static void queueMethodInvoker(MethodInvoker item){
        methodInvokers.add(item);
    }


}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////