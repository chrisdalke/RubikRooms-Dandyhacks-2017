////////////////////////////////////////////////
// The Pyramid's Curse
// A game by Chris Dalke
////////////////////////////////////////////////
// Module: Display
////////////////////////////////////////////////

package Engine.Display;

import Engine.Display.Recording.Recording;
import Engine.Input.Input;
import Engine.System.Config.Configuration;
import Engine.System.Logging.Logger;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;

public class Display {

    private static Configuration config;
    public static long frameNumber;

    public static Configuration getConfig(){
        return config;
    }

    public static double getHeight(){return config.height;}
    public static double getWidth(){return config.width;}

    public static void init(Configuration config){
        Display.config = config;

        if (config.fullscreen_window){
            //Fullscreen window sets resolution to match monitor resolution
            Graphics.DisplayMode current = Gdx.graphics.getDisplayMode();
            config.width = current.width;
            config.height = current.height;
        }

        if (config.fullscreen){

            Graphics.DisplayMode[] modes = Gdx.graphics.getDisplayModes();
            Graphics.DisplayMode chosenMode = null;
            Logger.log("Available display modes for this monitor:");
            for (int i = 0; i < modes.length; i++) {
                Logger.log(modes[i].toString());
                if (modes[i].width == config.width & modes[i].height == config.height){
                    chosenMode = modes[i];
                }
            }
            if (chosenMode != null) {
                Logger.log("Chose mode: " + chosenMode.toString());
                if(!Gdx.graphics.setFullscreenMode(chosenMode)) {
                    // switching to full-screen mode failed
                    Logger.log("Couldn't switch to fullscreen mode!");
                }
            } else {

                Logger.log("Couldn't find a valid display mode. ");
            }
            Logger.log("Set to Fullscreen mode.");
        } else {
            Logger.log("Set to Windowed mode.");
            Gdx.graphics.setWindowedMode((int) config.width, (int) config.height);
        }

        config.width = Gdx.graphics.getWidth();
        config.height = Gdx.graphics.getHeight();

        Gdx.graphics.setVSync(config.vsync);
        Gdx.graphics.setTitle("Game Window");

        frameNumber = 0;
    }

    public static void startRender(){
        //Clear the background

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(0.5f,0.5f,0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glDepthFunc(GL20.GL_GREATER);

        /*
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(0.4f,0.4f,0.4f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glDepthFunc(GL20.GL_GREATER);
        */
    }
    
    public static void finishRender(){

        //take a screenshot?
        if (Input.getKeyPress(com.badlogic.gdx.Input.Keys.P)){
            Screenshot.saveScreenshot();
            Logger.log("Took a screenshot!");
        }

        //Handle video capture
        if (Recording.isRecording()){
            Recording.recordFrame();
        }

        if (Input.getKeyPress(com.badlogic.gdx.Input.Keys.GRAVE)){
            if (Recording.isRecording()){
                Recording.stopRecording();
                Logger.log("Stopped video recording!");
            } else {
                Recording.startRecording();
                Logger.log("Started video recording!");
            }
        }

        frameNumber++;
    }

    public static void dispose(){

    }

    public static void hideCursor(){
        Pixmap pm = new Pixmap(Gdx.files.internal("Assets/Textures/pixel_blank.png"));
        Gdx.graphics.setCursor(Gdx.graphics.newCursor(pm, 0, 0));
        pm.dispose();
    }

    public static void showCursor(){
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
    }

}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////