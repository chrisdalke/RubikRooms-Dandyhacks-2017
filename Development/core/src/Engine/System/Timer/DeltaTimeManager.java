////////////////////////////////////////////////
// The Pyramid's Curse
// A game by Chris Dalke
////////////////////////////////////////////////
// File: TimerManager.java (System.Timer Manager)
////////////////////////////////////////////////

package Engine.System.Timer;

public class DeltaTimeManager {

    private double timeBuffer;
    private double timeInterval;
    private double startTime;
    private double lastInterval;

    //create a new delta time manager with time interval in ms
    public DeltaTimeManager(double timeInterval){
        timeBuffer = 0;
        this.timeInterval = timeInterval;
    }

    //add an amount of time in milliseconds
    public void addTime(double amount){
        timeBuffer += amount;
    }

    //Starts a timer interval
    public void startInterval(){
        startTime = getTime();
    }

    //Ends an interval and adds the delta time to the buffer
    public void endInterval(){
        lastInterval = getTime() - startTime;
        addTime(lastInterval);

    }

    public double getLastInterval(){
        return lastInterval;
    }

    //returns the time in milliseconds
    public double getTime(){
        return TimerManager.getTime();
    }

    public double getTimeBuffer(){
        return timeBuffer;
    }

    public boolean consumeTick(){
        if (timeBuffer >= timeInterval){
            timeBuffer -= timeInterval;
            return true;
        } else {
            return false;
        }
    }
}
