////////////////////////////////////////////////
// The Pyramid's Curse
// A game by Chris Dalke
////////////////////////////////////////////////
// Module: System
////////////////////////////////////////////////

package Engine.System;

import Engine.System.Config.Configuration;
import Engine.System.Logging.Logger;
import Engine.System.Utility.NativeDialog;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;

//Main subsystem class
//Should handle all the other subsystem classes in this category
public class System {
    public static void init(Configuration config){
        Logger.init();
        NativeDialog.init();

        isRunning = true;
    }

    public static void update(){
        if (!System.getRunning()) {
            Gdx.app.exit();
        }
    }

    public static void dispose(){
        Logger.dispose();
    }

    private static boolean isRunning;

    public static boolean getRunning(){
        return isRunning;
    }

    public static void setRunning(boolean isRunning){
        System.isRunning = isRunning;
    }

    private static final boolean isDesktop = (Gdx.app.getType() == Application.ApplicationType.Desktop);

    public static boolean isDesktop(){
        return isDesktop;
    }


}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////