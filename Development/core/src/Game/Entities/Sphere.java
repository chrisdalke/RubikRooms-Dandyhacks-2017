////////////////////////////////////////////////
// Development
// Chris Dalke
////////////////////////////////////////////////
// Module: Sphere
////////////////////////////////////////////////

package Game.Entities;

import Engine.Game.Entity.GameObject3d;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;

public class Sphere extends GameObject3d {
    public Sphere() {
        super("Assets/Models/sphere.obj");
        setScale(2.0f,2.0f,2.0f);
        this.getModel().materials.get(0).set(ColorAttribute.createDiffuse(1.0f,0.0f,0.0f,1.0f));
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////