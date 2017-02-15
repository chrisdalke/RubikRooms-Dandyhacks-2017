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
import Engine.Renderer.Textures.Texture;
import Engine.Renderer.Textures.TextureLoader;
import Engine.System.Timer.TimerManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
    private static Texture spotlightTex;
    private static Texture pharoahTex;


    public static void init(){
        stages = new HashMap<String, UIStage>();
        numStages = 0;
        currentStageName="";
        transitionState = 0;
        uiStageBatch = new SpriteBatch();
        spotlightTex = TextureLoader.load("Resources/Textures/spotlight.png");
        pharoahTex = TextureLoader.load("Resources/Textures/ra-eye.png");
    }

    public static UIStage getStage(String name){
        return stages.get(name);
    }

    //Fades to black, runs a function, and then fades back in
    public static void fadeOutEvent(Runnable event){
        fadeEvent = event;
        transitionState = 1;
        transitionDuration = 0.8f;
        transitionStartTime = TimerManager.getTime();
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

      // uiStageBatch.end();

        Renderer.endUI();
        Renderer.startUI();

        switch (transitionState){
            case 0:
                transitionAlpha = 0;
                transitionStartTime = TimerManager.getTime();
                break;
            case 1:
                transitionAlpha = (float)(TimerManager.getTime()-transitionStartTime)/(transitionDuration*1000);
                if (transitionAlpha > 1.0f){
                    transitionState = 3; //2 for instant transition
                    transitionAlpha = 1.0f;

                    //Trigger runnable event here
                    fadeEvent.run();

                    transitionStartTime = TimerManager.getTime();
                }
                break;
            case 3:{
                //Loading screen with pharoah
                if (TimerManager.getTime() - transitionStartTime > 1000){
                    transitionState = 2;
                    transitionStartTime = TimerManager.getTime();
                }

                break;
            }
            case 2:
                transitionAlpha = 1.0f-(float)(TimerManager.getTime()-transitionStartTime)/(transitionDuration*1000);
                if (transitionAlpha < 0.0f){
                    transitionState = 0;
                    transitionAlpha = 0.0f;
                }
                break;
        }

        if (transitionAlpha > 0){


            float spotlightSize = (0.9f - transitionAlpha) * (float)Display.getWidth()*1.5f;

            float spotlightOriginX = (float)Display.getWidth()/2 - spotlightSize/2;
            float spotlightOriginY = (float)Display.getHeight()/2 - spotlightSize/2;
            Renderer.draw(spotlightTex.getRegion(),spotlightOriginX,spotlightOriginY,spotlightSize,spotlightSize);

            //draw boxes to cover the areas that the spotlight texture has not drawn
            //Horizontal
            Shapes.setColor(0,0,0,1);
            Shapes.drawBox((int)Display.getWidth() - spotlightOriginX, 0,spotlightOriginX,(int)Display.getHeight());
            Shapes.drawBox(0, 0, spotlightOriginX, (int) Display.getHeight());

            //Vertical
            Shapes.drawBox(0, (int)Display.getHeight()-spotlightOriginY, (int)Display.getWidth(), spotlightOriginY);
            Shapes.drawBox(0, 0, (int) Display.getWidth(), spotlightOriginY);

            if (transitionState == 3){
                //Draw a pharoah in the center of the screen
                float pWidth = 507 / 4;
                float pHeight = 338 / 4;
                float alpha = (float)(TimerManager.getTime() - transitionStartTime)/200.0f;
                if (alpha > 1.0f){
                    if (alpha > 4.0f){
                        alpha -= 4.0f;
                        alpha = 1.0f - alpha;
                    } else {
                        alpha = 1.0f;
                    }
                }
                pWidth *= alpha;
                pHeight *= alpha;
                Renderer.setColor(new Color(255,255,255,alpha));
                Renderer.draw(pharoahTex.getRegion(),(float)Display.getWidth()/2 - pWidth/2, (float)Display.getHeight()/2 - pHeight/2,pWidth,pHeight,(float)Math.sin(TimerManager.getTime() / 100.0f) * 8.0f);
                Renderer.setColor(Color.WHITE);
            }


            /*
            //Step 1: Convert alpha to numbers between 1 and 10
            int animProgress = (int)(transitionAlpha * 10.0f);
            float verticalSize = (float)Display.getHeight()/10;
            float horizontalSize = (float)Display.getWidth()/10;

            //Do vertical rows
            for (int row = 0; row < 20; row++){
                int rowNumBoxes = animProgress - row;
                if (rowNumBoxes < 0){
                    rowNumBoxes = 0;
                }
                //Draw the number of boxes we want, as given by rowNumBoxes
                for (int i = 0; i < rowNumBoxes; i++){
                    Shapes.drawBox((i * horizontalSize) - ((i % 2) * (horizontalSize/2)), verticalSize * row, horizontalSize, verticalSize, new Color(0, 0, 0, 1));
                }
            }

            //Shapes.drawBox(0, 0, (int)Display.getWidth(),(int)Display.getHeight(), new Color(0,0,0,transitionAlpha));
            */
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