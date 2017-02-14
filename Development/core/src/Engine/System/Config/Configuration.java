////////////////////////////////////////////////
// The Pyramid's Curse
// A game by Chris Dalke
////////////////////////////////////////////////
// Module: Configuration
////////////////////////////////////////////////

package Engine.System.Config;

import java.io.Serializable;

//A simple class that stores the configuration of something
//it will be serialized and saved
public class Configuration implements Serializable, Cloneable {

    public long width;
    public long height;
    public long depth;
    public boolean vsync;
    public boolean fullscreen;
    public boolean fullscreen_window;
    public boolean debug;
    public boolean shaders;

    public Configuration(){
        width = 640;
        height = 480;
        depth = 32;
        vsync = false;
        fullscreen = false;
        fullscreen_window = false;
        debug = false;
        shaders = true;
    }

    public Configuration duplicate(){
        try {
            return (Configuration) this.clone();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void apply(Configuration configNew){
        width = configNew.width;
        height = configNew.height;
        depth = configNew.depth;
        vsync = configNew.vsync;
        fullscreen = configNew.fullscreen;
        fullscreen_window = configNew.fullscreen_window;
        debug = configNew.debug;
        shaders = configNew.shaders;

    }
}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////