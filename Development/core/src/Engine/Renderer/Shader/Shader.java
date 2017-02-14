////////////////////////////////////////////////
// The Pyramid's Curse
// A game by Chris Dalke
////////////////////////////////////////////////
// File: Shader.java
////////////////////////////////////////////////

package Engine.Renderer.Shader;

//Java package imports
import java.util.HashMap;

////////////////////////////////////////////////
// Main class
////////////////////////////////////////////////

public class Shader {

    //Hashmap filled with the variable locations for a given shader
    private HashMap<String, Integer> uniformLocations;
    private int numAttribs;

    public String uuid;
    private int programID;
    private int vertexShaderID;
    private int fragmentShaderID;

    public Shader(String uuid, String vertexFilename, String fragmentFilename) {
        this.uuid = uuid;
    }

    public void cleanup(){

    }
}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////
