////////////////////////////////////////////////
// The Pyramid's Curse
// A game by Chris Dalke
////////////////////////////////////////////////
// File: ConfigManager.java (Main program)
////////////////////////////////////////////////
//Stores config data for rendering and game

package Engine.System.Config;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

//Import java packages
//stores a configuration setting
//it can be loaded from default or from a file
public class ConfigManager {

    public Configuration config;

    public Configuration getConfig(){
        return config;
    }

    public void setConfig(Configuration config){
        this.config = config;
    }

    public static final String CONFIG_FILE = "Assets/Config/default.cfg";

    public ConfigManager(){
        load(CONFIG_FILE);
    }

    public void load(){
        load(CONFIG_FILE);
    }

    public void save(){
        save(CONFIG_FILE);
    }

    //Loads a config from a file
    public void load(String file){
        try {
            //Try to load the config by loading a serialized Configuration instance

            FileInputStream loadFile = new FileInputStream(file);
            ObjectInputStream restore = new ObjectInputStream(loadFile);
            config = (Configuration)restore.readObject();
            restore.close();

            System.out.println("Loaded config.");

        } catch (Exception e){
            //If we fail to load the config, set the config to the default values.
            //e.printStackTrace();
            System.out.println("Failed to load config. Set to default values...");
            config = new Configuration();
            save(CONFIG_FILE);
        }

    }

    //Saves a config from a file
    public void save(String file){
        try {

            FileOutputStream saveFile=new FileOutputStream(file);
            ObjectOutputStream save = new ObjectOutputStream(saveFile);
            save.writeObject(config);
            save.close();

            System.out.println("Saved config.");
        } catch (Exception e){
            //e.printStackTrace();
            System.out.println("Failed to save config.");
        }
    }
}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////