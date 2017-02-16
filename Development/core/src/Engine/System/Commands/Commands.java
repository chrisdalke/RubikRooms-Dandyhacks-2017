////////////////////////////////////////////////
// Development
// Chris Dalke
////////////////////////////////////////////////
// Module: Commands
////////////////////////////////////////////////

package Engine.System.Commands;

import Engine.Display.Display;
import Engine.Input.Input;
import Engine.Renderer.Renderer;
import Engine.Renderer.Shapes;
import Engine.Renderer.Text;
import Engine.System.Logging.Logger;
import Engine.System.System;
import Game.Game;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;

import java.util.HashMap;

public class Commands {

    private static HashMap<String,Runnable> commands;
    public static boolean isShown;
    public static boolean didSetPause;
    public static String[] logMessages;
    public static final int LOG_NUM_LINES = 20;
    public static boolean isInitialized = false;
    public static KeyboardBuffer buffer;

    public static void init(){
        commands = new HashMap<>();
        buffer = new KeyboardBuffer();

        //Build log messages array
        logMessages = new String[LOG_NUM_LINES];
        for (int i = 1; i < LOG_NUM_LINES; i++) {
            logMessages[i] = "";
        }

        isInitialized = true;

        Logger.log("Initialized console!");

        //Bind some default commands
        bind("exit", new Runnable() {
            @Override
            public void run() {
                System.setRunning(false);
            }
        });

        bind("quit", new Runnable() {
            @Override
            public void run() {
                System.setRunning(false);
            }
        });

        bind("chris dalke", new Runnable() {
            @Override
            public void run() {
                Logger.log("Hacker #1");
            }
        });

        bind("nate conroy", new Runnable() {
            @Override
            public void run() {
                Logger.log("Hacker #2");
            }
        });
    }


    public static void show(){
        isShown = true;
        buffer.start();
        if (!Game.isPaused()){
            didSetPause = true;
            Game.setPaused(true);
        }
    }

    public static void hide(){
        isShown = false;
        buffer.end();
        if (didSetPause){
            Game.setPaused(false);
            didSetPause = false;
        }
    }

    public static void toggle(){
        if (isShown){
            hide();
        } else {
            show();
        }
    }

    public static void render(){
        if (isShown){
            Shapes.begin(Renderer.cameraUI);
            Shapes.setColor(0,0,0,100);
            Text.setFont(2);
            Text.setColor(Color.WHITE);
            float lineHeight = Text.getTextHeight("I")+5;
            float height = LOG_NUM_LINES * lineHeight;
            Shapes.drawBox(0,(float)Display.getHeight() - height,(float) Display.getWidth(), height);
            Shapes.setColor(0,0,0,160);
            Shapes.drawBox(0,(float)Display.getHeight() - height - lineHeight - 10, (float)Display.getWidth(),lineHeight+10);
            Shapes.end();
            Renderer.startUI();
            for (int i = 1; i < LOG_NUM_LINES; i++) {
                Text.draw(10,(float)Display.getHeight() - (i*lineHeight),logMessages[i]);
            }
            Text.setColor(Color.YELLOW);
            String currentCommand = "Command: "+buffer.getString()+"_";
            Text.draw(10,(float)Display.getHeight() - (LOG_NUM_LINES*lineHeight)-5,currentCommand);

            if (Input.getKeyPress(com.badlogic.gdx.Input.Keys.ENTER)){
                String parsedCommand = buffer.getString().trim().replaceAll("\n ","");
                execute(parsedCommand);
                buffer.clear();
            }


            Renderer.endUI();
        }
    }

    public static void log(String message){
        if (isInitialized) {
            //Shift all items down in the log
            //Add last log item to end
            for (int i = 1; i < LOG_NUM_LINES; i++) {
                logMessages[i - 1] = logMessages[i];
            }
            logMessages[LOG_NUM_LINES - 1] = message;
        }
    }

    public static void bind(String command, Runnable method){
        if (isInitialized) {
            Logger.log("Bound command '" + command + "' to runnable " + method.toString());
            commands.put(command.toLowerCase(), method);
        }
    }

    public static void unbind(String command){
        if (isInitialized) {
            commands.remove(command.toLowerCase());
        }
    }

    public static void execute(String command){
        if (isInitialized) {
            if (commands.containsKey(command.toLowerCase())) {
                Runnable method = commands.get(command.toLowerCase());
                method.run();
            } else {
                Logger.log("The command '"+command+"' does not exist!");
            }
        }
    }

    private static class KeyboardBuffer implements InputProcessor {

        StringBuilder stringBuilder;
        public KeyboardBuffer(){
            clear();

        }

        public String getString(){
            return stringBuilder.toString();
        }

        public void clear(){
            stringBuilder = new StringBuilder();
        }

        public void start(){

            clear();
            Input.addInputHandler(this);
        }

        public void end(){

            clear();
            Input.removeInputHandler(this);
        }
        @Override
        public boolean keyDown(int keycode) {
            return false;
        }

        @Override
        public boolean keyUp(int keycode) {
            return false;
        }

        @Override
        public boolean keyTyped(char character) {
            if ( Character.compare(character,'\b') == 0){
                if (stringBuilder.length() > 0) {
                    stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                }
            } else {
                stringBuilder.append(character);
            }
            return false;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            return false;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }

        @Override
        public boolean scrolled(int amount) {
            return false;
        }
    }

}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////