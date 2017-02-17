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
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;

public class StaticPhysicsEntity extends PhysicsEntity {

    public Matrix4 transform;

    //Base class, defines a physics entity which drives a parent entity
    public StaticPhysicsEntity(GameObject3d parent) {
        super(parent);

        transform = new Matrix4(new Vector3(parent.getX(),parent.getY(),parent.getZ()),new Quaternion().setEulerAngles(parent.getRX(),parent.getRY(),parent.getRZ()),new Vector3(1,1,1));
        btCollisionShape shape = Bullet.obtainStaticNodeShape(parent.getModel().nodes);
        body = new btRigidBody(0f,null,shape);
    }


    public void dispose () {
        // Don't rely on the GC
        if (body != null) body.dispose();
        // And remove the reference
        body = null;
    }

}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////