////////////////////////////////////////////////
// Development
// Chris Dalke
////////////////////////////////////////////////
// Module: Cube
////////////////////////////////////////////////

package Game;

import Engine.Game.Entity.GameObject3d;
import Engine.Renderer.Models.ModelTexturer;
import Engine.Renderer.Textures.TextureLoader;

public class Cube extends GameObject3d {

    public Cube() {
        super("Assets/Models/cube.obj");
        ModelTexturer.texture(this.getModel(), TextureLoader.load("Assets/Textures/debug.png"));
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