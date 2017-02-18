////////////////////////////////////////////////
// Development
// Chris Dalke
////////////////////////////////////////////////
// Module: ControllerCube
////////////////////////////////////////////////

package Game.Entities;

import Engine.Game.Entity.GameObject3d;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;

public class ControllerCube extends GameObject3d {

    public boolean isRoom;

    public void setRoom(boolean room) {
        isRoom = room;

        if (isRoom){
            this.getModel().materials.get(0).set(ColorAttribute.createDiffuse(0.4f,0.4f,0.8f,1.0f));
            this.getModel().materials.get(0).set(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA, 1.0f));
        } else {
            this.getModel().materials.get(0).set(ColorAttribute.createDiffuse(0.9f,0.9f,0.9f,1.0f));
            this.getModel().materials.get(0).set(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA, 0.3f));
        }
    }

    public ControllerCube() {
        super("Assets/Models/cube.obj");

        setScale(0.9f,0.9f,0.9f);
        setRoom(true);

        //ModelTexturer.texture(this.getModel(), TextureLoader.load("Assets/Textures/debug.png"));
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