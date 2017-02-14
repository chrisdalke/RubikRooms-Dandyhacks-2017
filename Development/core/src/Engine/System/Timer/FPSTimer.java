////////////////////////////////////////////////
// Vaultland
// Chris Dalke
////////////////////////////////////////////////
// Module: FPSTimer
////////////////////////////////////////////////

package Engine.System.Timer;

public class FPSTimer {
    private static double lastFrameTime;
    private static double thisFrameTime;
    private static double thisFrameDelta;
    private static double timeFromLastUpdate;

    private static double outputDelta;
    private static final int pollRate = 500;

    public static void updateFrameTime(){
        if (lastFrameTime == 0) {
            lastFrameTime = TimerManager.getTime();
        }
        thisFrameDelta = TimerManager.getTime() - lastFrameTime;
        lastFrameTime = TimerManager.getTime();
        if (TimerManager.getTime() > (timeFromLastUpdate + pollRate)) {
            outputDelta = thisFrameDelta;
            timeFromLastUpdate = lastFrameTime;
        }
    }

    public static double getFrameTime(){
        return Math.round(outputDelta*100.0)/100.0;
    }

    public static double getFPS(){
        return Math.round((1000.0 / outputDelta)*100.0)/100.0;
    }


}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////