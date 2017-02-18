////////////////////////////////////////////////
// Development
// Chris Dalke
////////////////////////////////////////////////
// Module: BackgroundCube
////////////////////////////////////////////////

package Game.Entities;

import Engine.Game.Entity.GameObject3d;
import Engine.Renderer.Models.ModelTexturer;
import Engine.Renderer.Textures.TextureLoader;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;

public class TestRoomObject extends GameObject3d {

    public TestRoomObject() {
        super("Assets/Models/roomTest.obj");
        this.getModel().materials.get(0).set(ColorAttribute.createDiffuse(0.5f,0.5f,0.5f,1.0f));
        ModelTexturer.texture(this.getModel(), TextureLoader.load("Assets/Textures/stone.png"));
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