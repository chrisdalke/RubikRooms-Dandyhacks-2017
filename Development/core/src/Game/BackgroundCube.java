////////////////////////////////////////////////
// Development
// Chris Dalke
////////////////////////////////////////////////
// Module: BackgroundCube
////////////////////////////////////////////////

package Game;

import Engine.Game.Entity.GameObject3d;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;

import java.util.concurrent.ThreadLocalRandom;

public class BackgroundCube extends GameObject3d {

    float speedX = 0;
    float speedY = 0;
    float speedZ = 0;
    public BackgroundCube() {
        super("Assets/Models/cube.obj");
        this.getModel().materials.get(0).set(ColorAttribute.createDiffuse(0.8f,0.8f,0.8f,1.0f));
        this.getModel().materials.get(0).set(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA, ThreadLocalRandom.current().nextInt(0,100)/800.0f));

        speedX = ThreadLocalRandom.current().nextInt(-100,100)/100.0f;
        speedY = ThreadLocalRandom.current().nextInt(-100,100)/100.0f;
        speedZ = ThreadLocalRandom.current().nextInt(-100,100)/100.0f;

        //ModelTexturer.texture(this.getModel(), TextureLoader.load("Assets/Textures/debug.png"));
        setIsCollider(true);
    }

    @Override
    public void init() {

    }

    @Override
    public void update() {
        float speed = 0.1f;
        setRotation(getRX()+(speed*speedX),getRY()+(speed*speedY),getRZ()+(speed*speedZ));

    }


}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////