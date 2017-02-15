////////////////////////////////////////////////
// Frame Engine
// An engine by Chris Dalke
////////////////////////////////////////////////
// Module: Audio
////////////////////////////////////////////////

package Engine.Audio;

//handles all the audio playback
//this is all pretty much handled in one single module

import Engine.System.Config.Configuration;
import Engine.System.Logging.Logger;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;

public class Audio {

    private static HashMap<String, Sound> sounds;
    private static HashMap<String, Integer> soundState;
    private static HashMap<String, Music> music;

    private static int numSoundsPlaying;

    public static float VOLUME = 0.0f;

    public static void init(Configuration config){

        sounds = new HashMap<String, Sound>();
        music = new HashMap<String, Music>();
        soundState = new HashMap<String, Integer>();

        Logger.log("Initialized Audio subsystem.");
    }

    public static void loadSound(String name,String file){
        Sound sound = Gdx.audio.newSound(Gdx.files.internal(file));
        sounds.put(name,sound);
        soundState.put(name,0);

    }
    public static void loadMusic(String name,String file){
        Music music1 = Gdx.audio.newMusic(Gdx.files.internal(file));
        music.put(name,music1);
    }


    public static void playSound(String name){
        if (numSoundsPlaying < 100) {
            sounds.get(name).play(VOLUME);
            numSoundsPlaying += 10;
        }
    }

    public static void loopSound(String name){
        if (soundState.get(name).equals(0)){
            sounds.get(name).loop(VOLUME);
            soundState.put(name,1);
        }
    }

    public static void stopSound(String name){
        sounds.get(name).stop();
        soundState.put(name,0);
    }

    public static void playMusic(String name){
        music.get(name).play();
        music.get(name).setLooping(false);
    }

    public static void loopMusic(String name){
        music.get(name).play();
        music.get(name).setLooping(true);
    }

    public static void stopMusic(String name){
        music.get(name).stop();
    }

    public static void update(){
        numSoundsPlaying--;
    }

    public static void stopAll(){
        for (String soundID : sounds.keySet()){
            sounds.get(soundID).stop();
        }
        for (String musicID : music.keySet()){
            music.get(musicID).stop();
        }

    }

    public static void dispose(){
        for (String soundID : sounds.keySet()){
            sounds.get(soundID).dispose();
        }
        for (String musicID : music.keySet()){
            music.get(musicID).dispose();
        }
        Logger.log("Terminated Audio subsystem.");
    }
}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////