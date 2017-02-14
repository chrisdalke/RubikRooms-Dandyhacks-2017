package Engine.Input;

//input manager

import Engine.System.Config.Configuration;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;

import java.util.HashMap;

public class Input {

    private static HashMap<Integer, Boolean> keyPresses;
    private static InputMultiplexer inputMultiplexer;

    public static void init(Configuration config){

        keyPresses = new HashMap<Integer, Boolean>();

        inputMultiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    public static void addInputHandler(InputProcessor inputHandler){
        if (inputHandler != null){
            inputMultiplexer.addProcessor(inputHandler);

        }
    }

    public static void removeInputHandler(InputProcessor inputHandler) {
        if (inputHandler != null){
            inputMultiplexer.removeProcessor(inputHandler);

        }
    }

    public static void update(){
        checkKeyPresses();
    }

    public static boolean getKey(int key){
        //Register keypress
        boolean keyPress = Gdx.input.isKeyPressed(key);
        keyPresses.put(key,keyPress);
        return (keyPress);
    }

    public static float getMouseX(){
        return Gdx.input.getX();
    }
    public static float getMouseY(){
        return Gdx.input.getY();
    }


    public static int getMouseClicked(){
        int left = Gdx.input.isButtonPressed(Buttons.LEFT) ? 1 : 0;
        int right = Gdx.input.isButtonPressed(Buttons.RIGHT) ? 2 : 0;
        return left + right;
    }

    public static void checkKeyPresses(){
        //check all the key presses and update their value
        for (Integer key : keyPresses.keySet()) {
            if (keyPresses.get(key) == true){
                keyPresses.put(key,Gdx.input.isKeyPressed(key));
            }
        }
    }

    public static boolean getAnyKeyPresses(){
        boolean hasAnyKeyPresses = false;
        for (Integer key : keyPresses.keySet()) {
            if (Gdx.input.isKeyPressed(key)){
                hasAnyKeyPresses = true;
            }
        }
        return hasAnyKeyPresses;
    }

    public static boolean getKeyPress(int key){
        //check if the key is already pressed
        if (keyPresses.get(key) == null){
            keyPresses.put(key,false);
        }

        boolean returnValue = false;
        if (!keyPresses.get(key)){

            if (Gdx.input.isKeyPressed(key)){
                returnValue = true;
                keyPresses.put(key,true);
            }
        }
        return returnValue;
    }

    public static void dispose(){
        keyPresses = null;
    }
}
