////////////////////////////////////////////////
// Development
// Chris Dalke
////////////////////////////////////////////////
// Module: Recording
////////////////////////////////////////////////

package Engine.Display.Recording;

import Engine.Display.Display;
import Engine.Display.Screenshot;
import Engine.System.Logging.Logger;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class Recording {

    private static boolean isRecording;
    private static PixmapEncoder encoder;

    public static boolean isRecording(){
        return isRecording;
    }

    public static void startRecording(){
        try {
            //First, figure out the filename to save to
            int counter = 1;
            FileHandle fh;
            do {
                fh = new FileHandle("Resources/Recordings/recording" + counter++ + ".mp4");
            } while (fh.exists());

            encoder = new PixmapEncoder();
            encoder.initalize(fh.file(), (int) Display.getWidth(), (int) Display.getHeight());

            isRecording = true;
        } catch (Exception e){
            Logger.log("Unable to start screen recording!");
            isRecording = false;
            encoder = null;
        }
    }

    public static void recordFrame(){
        try {
            if (isRecording & encoder != null) {
                encoder.encodeFrame(encoder.pixmapToPicture(Screenshot.getScreenshot(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true)));
            }
        } catch (Exception e){
            //Do nothing
            Logger.log("Unable to record frame!");
        }
    }
    public static void stopRecording(){
        try {
            encoder.close();
        } catch (Exception e){
            Logger.log("Unable to finish and save screen recording!");
        }
        isRecording = false;
        encoder = null;
        Logger.log("Done screen recording!");
    }

}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////