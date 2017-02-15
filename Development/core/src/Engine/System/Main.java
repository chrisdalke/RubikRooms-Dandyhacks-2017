////////////////////////////////////////////////
// Vaultland
// A game by Chris Dalke
////////////////////////////////////////////////
// Module: Main
////////////////////////////////////////////////

package Engine.System;

import Engine.Audio.Audio;
import Engine.Display.Display;
import Engine.Input.Input;
import Engine.Networking.Networking;
import Engine.Renderer.Renderer;
import Engine.System.Config.ConfigManager;
import Engine.System.Config.Configuration;
import Engine.System.Logging.Logger;
import Engine.System.Timer.FPSTimer;
import Engine.UI.UI;
import Game.Game;

public class Main {

    ////////////////////////////////////////////////
    // Initialization
    ////////////////////////////////////////////////

    private static ConfigManager configManager;

    public static void init() {
        configManager = new ConfigManager();
        Configuration config = configManager.getConfig();

        try {
            //Init all of our game's systems
            System.init(config);
            Display.init(config);
            Input.init(config);
            Renderer.init(config);
            Audio.init(config);
            UI.init(config);
            Networking.init(config);
            Game.init(config);

        } catch (Exception e){
            Logger.logError("Error while Initializing... Quitting Game!");
            e.printStackTrace();
            java.lang.System.exit(0);
        }
        Logger.log("Init completed.");

    }

    //Applys a configuration during the game. Also saves the config.
    public static void applyConfig(Configuration config){
        configManager.setConfig(config);
        configManager.save();
        //Display.init(config);
        //Renderer.init(config);
    }

    ////////////////////////////////////////////////
    // Update
    ////////////////////////////////////////////////

    public static void render(){
        FPSTimer.updateFrameTime();

        //update the input manager
        Networking.update();
        System.update();
        Input.update();

        Game.update(); //Handle game logic update

        Display.startRender();
        Game.render(); //Handle game rendering
        Display.finishRender();

    }


    ////////////////////////////////////////////////
    // Terminate
    ////////////////////////////////////////////////

    public static void dispose(){

        //Destroy all the game resources before we close the game
        Logger.logError("Disposing...");

        Networking.dispose();
        Game.dispose();
        Audio.dispose();
        Renderer.dispose();
        Input.dispose();
        UI.dispose();
        Display.dispose();
        System.dispose();
    }

}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////