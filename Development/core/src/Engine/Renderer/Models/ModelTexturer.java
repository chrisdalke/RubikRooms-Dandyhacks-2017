////////////////////////////////////////////////
// Development
// Chris Dalke
////////////////////////////////////////////////
// Module: ModelTexturer
////////////////////////////////////////////////

package Engine.Renderer.Models;

import Engine.Renderer.Textures.Texture;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;

public class ModelTexturer {
    public static void texture(ModelInstance instance, Texture tex){
        Material material = instance.materials.get(0);
        material.set(TextureAttribute.createDiffuse(tex.getRegion()));
    }

}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////