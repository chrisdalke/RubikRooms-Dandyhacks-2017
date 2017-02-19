////////////////////////////////////////////////
// Development
// Chris Dalke
////////////////////////////////////////////////
// Module: PhysicsCharacterController
////////////////////////////////////////////////

package Engine.Physics;

import Engine.Game.Entity.GameObject3d;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.dynamics.btKinematicCharacterController;

public class PhysicsCharacterController extends PhysicsEntity {

    btPairCachingGhostObject ghostObject;
    btConvexShape ghostShape;
    btKinematicCharacterController characterController;
    Matrix4 characterTransform;
    Vector3 characterDirection = new Vector3();
    Vector3 walkDirection = new Vector3();

    public PhysicsCharacterController(GameObject3d parent) {
        super(parent);

        characterTransform = new Matrix4();

        // Create physics representation of the character
        ghostObject = new btPairCachingGhostObject();
        ghostObject.setWorldTransform(characterTransform.rotate(1,0,0,90));
        ghostShape = new btCapsuleShape(2.0f,4.0f);
        ghostObject.setCollisionShape(ghostShape);
        ghostObject.setCollisionFlags(btCollisionObject.CollisionFlags.CF_CHARACTER_OBJECT);
        characterController = new btKinematicCharacterController(ghostObject, ghostShape, .35f, Vector3.Y);
        characterController.setMaxJumpHeight(15.0f);
    }

    public void addToPhysicsWorld(PhysicsWorld world){
        setWorld(world);
        world.collisionWorld.addCollisionObject(ghostObject,
                (short) btBroadphaseProxy.CollisionFilterGroups.CharacterFilter,
                (short)(btBroadphaseProxy.CollisionFilterGroups.StaticFilter | btBroadphaseProxy.CollisionFilterGroups.DefaultFilter));
        (world.collisionWorld).addAction(characterController);
    }

    public void setCharacterPosition(Vector3 pos){
        ghostObject.setWorldTransform(characterTransform.setToTranslation(pos));

    }

    public Matrix4 getCharacterPosition(){
        ghostObject.getWorldTransform(characterTransform);
        return characterTransform;
    }

    public void setCharacterWalkVector(Vector3 walkVec){
        walkDirection.set(walkVec);
        characterController.setWalkDirection(walkDirection);
    }

    public void jump(){
        characterController.jump();
    }

    public boolean canJump(){
        return characterController.onGround();
    }

    public void update(){
    }

}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////