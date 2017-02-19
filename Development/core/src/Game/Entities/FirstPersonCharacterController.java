////////////////////////////////////////////////
// Development
// Chris Dalke
////////////////////////////////////////////////
// Module: FirstPersonCharacterController
////////////////////////////////////////////////

package Game.Entities;

import Engine.Game.Entity.Types.Camera3d;
import Engine.Physics.PhysicsCharacterController;
import Engine.System.Logging.Logger;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

public class FirstPersonCharacterController extends Camera3d {
    private int STRAFE_LEFT = com.badlogic.gdx.Input.Keys.A;
    private int STRAFE_RIGHT = com.badlogic.gdx.Input.Keys.D;
    private int FORWARD = com.badlogic.gdx.Input.Keys.W;
    private int BACKWARD = com.badlogic.gdx.Input.Keys.S;
    private int JUMP = Input.Keys.SPACE;

    public PhysicsCharacterController characterController;
    private Matrix4 characterTransform;

    public FirstPersonCharacterController() {
        Logger.log("Trying to create fps controller!");
        init();
        Logger.log("Created first person character controller!");
    }

    private Camera camera;
    private float velocity = 0.20f;
    private float degreesPerPixel = 0.5f;
    private final Vector3 tmp = new Vector3();
    private float vLeft = 0;
    private float vRight = 0;
    private float vForward = 0;
    private float vBackward = 0;

    private float camAngleX = 0;
    private float camAngleY = 0;
    private float camAngleZ = 0;

    private Quaternion viewAngle;

    public void setVelocity (float velocity) {
        this.velocity = velocity;
    }
    public void setDegreesPerPixel (float degreesPerPixel) {
        this.degreesPerPixel = degreesPerPixel;
    }


    @Override
    public void init() {
        super.init();
        camera = getCam();
        characterController = new PhysicsCharacterController(this);
        viewAngle = new Quaternion();
    }

    @Override
    public void update() {

        //Handle player lookaround

        float deltaX = -Gdx.input.getDeltaX() * degreesPerPixel;
        float deltaY = -Gdx.input.getDeltaY() * degreesPerPixel;

        float viewBoundary = 80.0f;
        camAngleY = (camAngleY + deltaX) % 360.0f;
        camAngleX = Math.min(Math.max((camAngleX + deltaY),-viewBoundary),viewBoundary);

        viewAngle = new Quaternion().setEulerAngles(camAngleY,camAngleX,0);

        camera.direction.set(0, 0, -1);
        camera.up.set(0, 1, 0);
        camera.update();
        camera.rotate(viewAngle);

        float decay = 0.85f;
        float rampUp = 0.10f;
        if (Engine.Input.Input.getKey(FORWARD)) {
            vForward = Math.min(vForward+rampUp,1.0f);
        } else {
            vForward *= decay;
        }
        if (Engine.Input.Input.getKey(BACKWARD)) {
            vBackward = Math.min(vBackward+rampUp,1.0f);
        } else {
            vBackward *= decay;
        }
        if (Engine.Input.Input.getKey(STRAFE_LEFT)) {
            vLeft = Math.min(vLeft+rampUp,1.0f);
        } else {
            vLeft *= decay;
        }
        if (Engine.Input.Input.getKey(STRAFE_RIGHT)) {
            vRight = Math.min(vRight+rampUp,1.0f);
        } else {
            vRight *= decay;
        }
        if (Engine.Input.Input.getKey(JUMP) & characterController.canJump()){
            characterController.jump();
        }

        //Generate walk vector based on the speeds that we have calculated and the angle we'd like to go
        Vector3 walkVector = new Vector3();
        walkVector.set(vRight-vLeft,0,vBackward-vForward);
        walkVector.rotate(camAngleY,0f,1f,0f);
        walkVector.scl(velocity);
        characterController.setCharacterWalkVector(walkVector);
        characterController.update();

        //Set camera position based on the character walk vector
        //Will unfortunately be about a frame behind
        Vector3 resultPos = new Vector3();
        characterController.getCharacterPosition().getTranslation(resultPos);
        camera.position.set(resultPos.x,resultPos.y+1,resultPos.z);
        camera.update();

    }


    @Override
    public void dispose(){

    }

}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////