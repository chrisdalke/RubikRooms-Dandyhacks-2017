////////////////////////////////////////////////
// The Pyramid's Curse
// A game by Chris Dalke
////////////////////////////////////////////////
// File: ShaderLoader.java
////////////////////////////////////////////////

package Engine.Renderer.Shader;

////////////////////////////////////////////////
// Main class
////////////////////////////////////////////////

public class ShaderLoader {

    public static Shader load(String filename){

        Shader newShader = ShaderCache.checkCache(filename);
        if (newShader == null){
            //load it and then add it to the cache

            String vertexFilename = ("Resources/Shaders/"+filename+"/vertex.fx");
            String fragmentFilename = ("media/shaders/"+filename+"/fragment.fx");

            newShader = new Shader(filename,vertexFilename,fragmentFilename);
            ShaderCache.addToCache(newShader);
        }

        return newShader;
    }
}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////

