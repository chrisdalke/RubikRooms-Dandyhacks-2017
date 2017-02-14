////////////////////////////////////////////////
// The Pyramid's Curse
// A game by Chris Dalke
////////////////////////////////////////////////
// File: TimerManager.java (System.Timer Manager)
////////////////////////////////////////////////

package Engine.System.Timer;

public class TimerManager {
    //returns the time in milliseconds
    public static double getTime(){
        return System.nanoTime()/1e6;
    }
}
