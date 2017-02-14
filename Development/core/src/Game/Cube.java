////////////////////////////////////////////////
// Development
// Chris Dalke
////////////////////////////////////////////////
// Module: Cube
////////////////////////////////////////////////

package Game;

import Engine.Renderer.Models.ModelTexturer;
import Engine.Renderer.Textures.TextureLoader;

public class Cube extends GameObject {
    public Cube() {
        super("Resources/Models/crate.obj");
        ModelTexturer.texture(this.getModel(), TextureLoader.load("Resources/Textures/crate.jpg"));
        setIsCollider(true);
    }

    @Override
    public void init() {

    }

    @Override
    public void update() {
        
    }
}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////