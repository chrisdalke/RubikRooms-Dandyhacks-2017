////////////////////////////////////////////////
// ORBITAL SPACE
// A game by Chris Dalke
////////////////////////////////////////////////
// Module: UIStageManager
////////////////////////////////////////////////

package Engine.UI.Stages;

import Engine.Display.Display;
import Engine.Input.Input;
import Engine.Renderer.Renderer;
import Engine.Renderer.Shapes;
import Engine.System.Timer.TimerManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import java.util.HashMap;

public class UIStageManager {

    private static HashMap<String,UIStage> stages;
    private static int numStages;
    private static String currentStageName;
    private static String nextStageName;
    private static float transitionAlpha;
    private static int transitionState;
    private static double transitionStartTime;
    private static float transitionDuration;
    private static SpriteBatch uiStageBatch;
    private static Runnable fadeEvent;

    //Fades, runs a function, and then fades back in
    public static void fadeOutEvent(Runnable event){
        fadeEvent = event;
        transitionState = 1;
        transitionDuration = 0.8f;
        transitionStartTime = TimerManager.getTime();
        //disable the input for the current stage early
        //This assumes the fade event will reset the stage
        UIStage currentStage = stages.get(currentStageName);
        Input.removeInputHandler(currentStage);
    }

    public static void init(){
        stages = new HashMap<String, UIStage>();
        numStages = 0;
        currentStageName="";
        transitionState = 0;
        uiStageBatch = new SpriteBatch();
    }

    public static UIStage getStage(String name){
        return stages.get(name);
    }

    public static void switchTo(String name){
        UIStage currentStage = stages.get(currentStageName);
        if (currentStage != null) {
            //disable the old stage
            currentStage.getRoot().getColor().a = 0;
            currentStage.hide();
            Input.removeInputHandler(currentStage);
        }

        currentStageName = name;

        //enable the new stage
        currentStage = stages.get(currentStageName);
        if (currentStage != null) {
            currentStage.getRoot().getColor().a = 1.0f;
            //create the stage elements.
            currentStage.show();
            Input.addInputHandler(currentStage);
        }

        //UIModalWindow.clearModals();
    }

    public static void crossFadeTo(String name, float duration){
        //start fading out the current stage
        //System.out.println("Crossfading from stage "+currentStageID+" to "+id);
        UIStage currentStage = stages.get(currentStageName);
        UIStage nextStage = stages.get(name);
        fadeOut(currentStage, duration);
        fadeIn(nextStage, duration);
        currentStageName = name;
    }

    //Fades out from the current stage, to black, then in to the new stage
    public static void fadeTo(String name, float duration){
        nextStageName = name;
        final UIStage currentStage = stages.get(currentStageName);
        final UIStage newStage = stages.get(name);
        //disable the input for the current stage early
        Input.removeInputHandler(currentStage);
        //trigger the transition
        transitionState = 1;
        transitionDuration = duration;
        transitionStartTime = TimerManager.getTime();
    }

    public static void fadeIn(UIStage stage, float duration){
        stage.getRoot().getColor().a = 0;
        stage.show();
        Input.addInputHandler(stage);
        stage.getRoot().addAction(Actions.fadeIn(duration));
    }

    public static void fadeOut(UIStage stage, float duration){
        stage.getRoot().getColor().a = 1.0f;
        Input.removeInputHandler(stage);
        stage.getRoot().addAction(Actions.fadeOut(duration));
    }

    public static UIStage getCurrentStage(){
        return stages.get(currentStageName);
    }

    public static void render(){
        // uiStageBatch.begin();
        UIStage currentStage;
        for (String name : stages.keySet()) {
            currentStage = stages.get(name);
            if (currentStage.getRoot().getColor().a > 0){
                currentStage.updateAll();
                currentStage.renderAll();
            }
        }
        //Apply the viewport from the current stage to the game batch
        Renderer.getBatch().setProjectionMatrix(getCurrentStage().getViewport().getCamera().combined);
        Renderer.getBatch().setTransformMatrix(new Matrix4().idt());

        // uiStageBatch.end();

        switch (transitionState){
            case 0:
                transitionAlpha = 0;
                transitionStartTime = TimerManager.getTime();
                break;
            case 1:
                transitionAlpha = (float)(TimerManager.getTime()-transitionStartTime)/(transitionDuration*1000);
                if (transitionAlpha > 1.0f){
                    transitionState = 2;
                    transitionAlpha = 1.0f;

                    //Trigger runnable event here
                    fadeEvent.run();

                    //Switch to the new stage here (Deprecated, use fade event instead)
                    //switchTo(nextStageName);
                    transitionStartTime = TimerManager.getTime();
                }
                break;
            case 2:
                transitionAlpha = 1.0f-(float)(TimerManager.getTime()-transitionStartTime)/(transitionDuration*1000);
                if (transitionAlpha < 0.0f){
                    transitionState = 0;
                    transitionAlpha = 0.0f;
                }
                break;
        }

        if (transitionAlpha > 0){
            Shapes.begin(Renderer.cameraUI);
            Shapes.setColor(255,255,255,(int)(255.0f*transitionAlpha));
            Shapes.drawBox(0, 0, (int) Display.getWidth(),(int) Display.getHeight());
            Shapes.end();
        }
    }

    public static void addStage(String name, UIStage newStage){
        if (Display.getConfig().debug) {
            newStage.setDebugAll(true);
        }
        stages.put(name,newStage);
        newStage.getRoot().getColor().a = 0;
        newStage.hide();

    }

    public static void removeStage(String name){
        stages.remove(name);
    }

    public static void dispose(){

    }

}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////