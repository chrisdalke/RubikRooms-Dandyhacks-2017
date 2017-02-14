////////////////////////////////////////////////
// The Pyramid's Curse
// A game by Chris Dalke
////////////////////////////////////////////////
// File: TextureCache.java
////////////////////////////////////////////////

package Engine.Renderer.Textures;

//Java packages
import java.util.ArrayList;

////////////////////////////////////////////////
// Main class
////////////////////////////////////////////////

public class TextureCache {

    //Simple array list containing our texture cache
    private static ArrayList<Texture> texCache;

    //Boolean so we can tell if the cache is ready
    private static boolean cacheReady;

    public static void initCache(){
        texCache = new ArrayList<Texture>();
        cacheReady = true;
    }

    //Check if a texture with a certain UUID is already in the cache
    //Otherwise we will need to load this texture
    public static Texture checkCache(String UUID){
        //check if the Cache is ready, if not, initialize it
        if (!cacheReady) { initCache(); }

        Texture cachedTexture = null;
        //check if the texture exists in the cache
        for (int i = 0; i < texCache.size(); i++){
            if (texCache.get(i).uuid.equals(UUID)){
                cachedTexture = texCache.get(i);
            }
        }

        return cachedTexture;
    }

    public static void addToCache(Texture newTex){
        //check if the Cache is ready, if not, initialize it
        if (!cacheReady) { initCache(); }

        texCache.add(newTex);
    }

    //Load all of the textures from the texture folder
    public static void fillCache(){
        //check if the Cache is ready, if not, initialize it
        if (!cacheReady) { initCache(); }

        //this is a function for the TextureLoader
        System.out.println("Call to TextureCache.fillCache, which has not yet been implemented!");
    }

    //Clean up and delete all resources
    public static void cleanup(){
        try {
            for (Texture tex : texCache) {
                tex.cleanup();
            }
            texCache = null;
            cacheReady = false;
        }catch (Exception e) {

        }
    }

}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////