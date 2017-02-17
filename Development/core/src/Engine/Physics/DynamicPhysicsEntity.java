////////////////////////////////////////////////
// Development
// Chris Dalke
////////////////////////////////////////////////
// Module: DynamicPhysicsEntity
////////////////////////////////////////////////

package Engine.Physics;

import Engine.Game.Entity.GameObject3d;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;

public class DynamicPhysicsEntity extends PhysicsEntity {

    public Matrix4 transform;
    public MotionState motionState;

    //Base class, defines a physics entity which drives a parent entity
    public DynamicPhysicsEntity(GameObject3d parent) {
        super(parent);

        transform = new Matrix4(new Vector3(parent.getX(),parent.getY(),parent.getZ()),new Quaternion().setEulerAngles(parent.getRX(),parent.getRY(),parent.getRZ()),new Vector3(1,1,1));
        motionState = new MotionState(transform,parent,null);

        float mass = 10.0f;
        Vector3 localInertia = new Vector3();
        btCollisionShape shape = new btSphereShape(1.0f);
        shape.calculateLocalInertia(mass,localInertia);
        btRigidBody.btRigidBodyConstructionInfo bodyInfo = new btRigidBody.btRigidBodyConstructionInfo(mass,motionState,shape,localInertia);
        body = new btRigidBody(bodyInfo);
        motionState.setBody(body);
        motionState.setEntity(this);
        bodyInfo.dispose();

    }

    public void applyForce(float x, float y, float z){
        body.applyCentralImpulse(new Vector3(x,y,z));
    }

    static class MotionState extends btMotionState {
        private final Matrix4 transform;
        public GameObject3d parent;
        public DynamicPhysicsEntity entity;
        public btRigidBody body;

        public MotionState (final Matrix4 transform, GameObject3d parent, btRigidBody body) {
            this.transform = transform;
            this.parent = parent;
            this.body = body;
        }

        public DynamicPhysicsEntity getEntity() {
            return entity;
        }

        public void setEntity(DynamicPhysicsEntity entity) {
            this.entity = entity;
        }

        public void setBody(btRigidBody body) {
            this.body = body;
        }

        /** For dynamic and static bodies this method is called by bullet once to get the initial state of the body. For kinematic
         * bodies this method is called on every update, unless the body is deactivated. */
        @Override
        public void getWorldTransform (final Matrix4 worldTrans) {
            worldTrans.set(transform);
        }

        /** For dynamic bodies this method is called by bullet every update to inform about the new position and rotation. */
        @Override
        public void setWorldTransform (final Matrix4 worldTrans) {
            transform.set(worldTrans);
            updateParent();

        }

        public void updateParent(){
            Vector3 position = new Vector3();
            Quaternion rotation = new Quaternion();
            Vector3 rotationEuler = new Vector3();
            transform.getTranslation(position);
            transform.getRotation(rotation);
            parent.setPosition(position.x,position.y,position.z);
            parent.setRotation(rotation.getYaw(),rotation.getPitch(),rotation.getRoll());

            //If the y is less than -100, delete physics object and kill gameobject
            if (position.y < -100){
                if (entity != null){
                    entity.getWorld().remove(entity);
                    parent.kill();
                }
            }
        }
    }

    public void dispose () {
        // Don't rely on the GC
        if (motionState != null) motionState.dispose();
        if (body != null) body.dispose();
        // And remove the reference
        motionState = null;
        body = null;
    }

}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////