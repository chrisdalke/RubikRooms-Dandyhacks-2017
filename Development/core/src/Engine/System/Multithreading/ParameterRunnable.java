////////////////////////////////////////////////
// Development
// Chris Dalke
////////////////////////////////////////////////
// Module: ParameterRunnable
////////////////////////////////////////////////

package Engine.System.Multithreading;

public abstract class ParameterRunnable implements Runnable {

    public String data;
    public ParameterRunnable() {

    }

    public void setParam(String data){
        this.data = data;
    }

    //Use the data here
    public abstract void run();
}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////