////////////////////////////////////////////////
// Development
// Chris Dalke
////////////////////////////////////////////////
// Module: Camera
////////////////////////////////////////////////

package Engine.Game.Entity.Types;

import Engine.Game.Entity.GameObject3d;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;

public class Camera3d extends GameObject3d {
    
    private PerspectiveCamera cam;
    private float cameraShakeAmount = 0;
    
    @Override
    public void init() {
        super.init();
        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(getX(), getY(), getZ());
        cam.near = 0.1f;
        cam.far = 100f;
        cam.update();
    }

    public PerspectiveCamera getCam() {
        return cam;
    }

    public void setCam(PerspectiveCamera cam) {
        this.cam = cam;
    }
    
    public float getCameraShakeAmount() {
        return cameraShakeAmount;
    }

    public void setCameraShakeAmount(float cameraShakeAmount) {
        this.cameraShakeAmount = cameraShakeAmount;
    }
    
    @Override
    public void update(){
        //Handle camera shake
        cameraShakeAmount = Math.max(cameraShakeAmount - 0.05f, 0);
        float shakeX = (float)(Math.random() * 0.05f) * cameraShakeAmount;
        float shakeY = (float)(Math.random() * 0.05f) * cameraShakeAmount;
        float shakeZ = (float)(Math.random() * 0.05f) * cameraShakeAmount;
        cam.position.set(getX()+shakeX, getY()+shakeY, getZ()+shakeZ);
        cam.update();
    }
}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////