////////////////////////////////////////////////
// The Pyramid's Curse
// A game by Chris Dalke
////////////////////////////////////////////////
// File: ShaderCache.java
////////////////////////////////////////////////

package Engine.Renderer.Shader;

//Java package imports
import java.util.ArrayList;

////////////////////////////////////////////////
// Main class
////////////////////////////////////////////////

public class ShaderCache {

    //Simple array list containing our shader cache
    private static ArrayList<Shader> shaderCache;

    //Boolean so we can tell if the cache is ready
    private static boolean cacheReady;

    public static void initCache(){
        shaderCache = new ArrayList<Shader>();
        cacheReady = true;
    }

    //Check if a shader with a certain UUID is already in the cache
    //Otherwise we will need to load this shader
    public static Shader checkCache(String UUID){
        //check if the Cache is ready, if not, initialize it
        if (!cacheReady) { initCache(); }

        Shader cachedShader = null;
        //check if the mesh exists in the cache
        for (int i = 0; i < shaderCache.size(); i++){
            if (shaderCache.get(i).uuid.equals(UUID)){
                cachedShader = shaderCache.get(i);
            }
        }

        return cachedShader;
    }

    public static void addToCache(Shader newShader){
        //check if the Cache is ready, if not, initialize it
        if (!cacheReady) { initCache(); }

        shaderCache.add(newShader);
    }

    //Load all of the shaders from the shader folder
    public static void fillCache(){
        //check if the Cache is ready, if not, initialize it
        if (!cacheReady) { initCache(); }

        //this is a function for the ShaderLoader
        System.out.println("Call to ShaderCache.fillCache, which has not yet been implemented!");
    }

    //Clean up and delete all resources
    public static void cleanup(){
        for (Shader shader : shaderCache) {
            shader.cleanup();
        }
        shaderCache = null;
        cacheReady = false;
    }
}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////