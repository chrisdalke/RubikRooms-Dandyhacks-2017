////////////////////////////////////////////////
// Development
// Chris Dalke
////////////////////////////////////////////////
// Module: ModelLoader
////////////////////////////////////////////////

package Engine.Renderer.Models;

import Engine.System.Logging.Logger;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.utils.JsonReader;

import java.io.UnsupportedEncodingException;

public class ModelLoader {
    private static ObjLoader objLoader;
    private static G3dModelLoader g3dModelLoader;

    public static Model load(String filename){

        //initialize modelloader
        if (objLoader == null){
            objLoader = new ObjLoader();
            g3dModelLoader = new G3dModelLoader(new JsonReader());
        }

        Model newModel = ModelCache.checkCache(filename);

        if (newModel == null){
            try {
                //Load the model we specified from the given filename
                if (filename.endsWith(".obj")) {
                    newModel = objLoader.loadModel(Gdx.files.internal(filename));
                } else if (filename.endsWith(".g3dj")){
                    newModel = g3dModelLoader.loadModel(Gdx.files.internal(filename));
                } else {
                    throw new UnsupportedEncodingException();
                }

            } catch (Exception e){
                Logger.log("Failed to load model.");
                e.printStackTrace();
                newModel = null;
            }
            if (newModel != null){
                //add it to the cache
                ModelCache.addToCache(filename,newModel);
                Logger.log("Loaded model from file " + filename);
            }
        }

        return newModel;
    }
}


////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////