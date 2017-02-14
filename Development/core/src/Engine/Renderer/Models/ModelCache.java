////////////////////////////////////////////////
// Development
// Chris Dalke
////////////////////////////////////////////////
// Module: ModelCache
////////////////////////////////////////////////

package Engine.Renderer.Models;

////////////////////////////////////////////////
// Main class
////////////////////////////////////////////////

import com.badlogic.gdx.graphics.g3d.Model;

import java.util.HashMap;

public class ModelCache {

    //Simple array list containing our model cache
    private static HashMap<String,Model> modelCache;

    //Boolean so we can tell if the cache is ready
    private static boolean cacheReady;

    public static void initCache(){
        modelCache = new HashMap<String,Model>();
        cacheReady = true;
    }

    //Check if a model is already in the cache
    //Otherwise we will need to load this model
    public static Model checkCache(String filename){
        //check if the Cache is ready, if not, initialize it
        if (!cacheReady) { initCache(); }

        Model cachedModel = modelCache.get(filename);

        return cachedModel;
    }

    public static void addToCache(String filename, Model newModel){
        //check if the Cache is ready, if not, initialize it
        if (!cacheReady) { initCache(); }

        modelCache.put(filename,newModel);
    }

    //Clean up and delete all resources
    public static void cleanup(){
        try {
            for (Model model : modelCache.values()) {
                model.dispose();
            }
            modelCache = null;
            cacheReady = false;
        }catch (Exception e) {

        }
    }

}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////