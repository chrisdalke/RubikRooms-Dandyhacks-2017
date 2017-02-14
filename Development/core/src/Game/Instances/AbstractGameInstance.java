package Game.Instances;

import Engine.System.Config.Configuration;

import java.util.HashMap;

public abstract class AbstractGameInstance {

    public abstract void init(Configuration config);
    public void update(){
        clearEvents();
    }
    public abstract void render();
    public abstract void dispose();


    private HashMap<String,Boolean> events;
    //Sets an event. Will be cleared at the end of the frame
    public void setEvent(String event){
        if (events == null){
            events = new HashMap<String,Boolean>();
        }
        events.put(event,true);
    }

    public void clearEvents(){
        if (events == null){
            events = new HashMap<String,Boolean>();
        }
        for (String curEvent : events.keySet()){
            events.put(curEvent,false);
        }
    }
    //Checks the status of an event
    public boolean checkEvent(String event){
        if (events == null){
            events = new HashMap<String,Boolean>();
        }
        boolean result = false;
        if (events.containsKey(event)){
            result = events.get(event);
        }
        return result;
    }
}
