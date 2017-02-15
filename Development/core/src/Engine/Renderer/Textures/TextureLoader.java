////////////////////////////////////////////////
// The Pyramid's Curse
// A game by Chris Dalke
////////////////////////////////////////////////
// File: TextureLoader.java
////////////////////////////////////////////////

package Engine.Renderer.Textures;

//Java package imports

import Engine.System.Logging.Logger;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

////////////////////////////////////////////////
// Main class
////////////////////////////////////////////////

public class TextureLoader {

    public static Texture load(String filename){
        return load(filename, 1,1);
    }
    public static Texture load(String filename, int numRegionsX, int numRegionsY){

        Texture newTex = TextureCache.checkCache(filename);
        if (newTex == null){
            newTex = new Texture();
            newTex.setUuid(filename);
            newTex.setID(null);
            try {

                com.badlogic.gdx.graphics.Texture tempTex = new com.badlogic.gdx.graphics.Texture(Gdx.files.internal(filename));

                newTex.setID(tempTex);
                newTex.setHasFiltering(true);

                newTex.filename = filename;

                //Figure out how many regions the given texture has (a parameter in the texture function)

                newTex.numRegions = numRegionsX * numRegionsY;
                newTex.regions = new TextureRegion[newTex.numRegions];
                int currentRegion = 0;
                int texSizeX = tempTex.getWidth();
                int texSizeY = tempTex.getHeight();

                for (int y = 0; y < numRegionsY; y++){
                    for (int x = 0; x < numRegionsX; x++){
                        newTex.regions[currentRegion++] = new TextureRegion(tempTex,x * (texSizeX / numRegionsX),y * (texSizeY / numRegionsY),texSizeX / numRegionsX,texSizeY / numRegionsY);
                    }
                }



            } catch (Exception e){
                Logger.log("Failed to load texture.");
                newTex = null;
            }
            if (newTex != null){
                //add it to the cache
                TextureCache.addToCache(newTex);
                Logger.log("Loaded texture "+newTex.toString());
            }
        }


        return newTex;
    }
}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////