////////////////////////////////////////////////
// Development
// Chris Dalke
////////////////////////////////////////////////
// Module: KinematicPhysicsEntity
////////////////////////////////////////////////

package Engine.Physics;

import Engine.Game.Entity.GameObject3d;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;

public class KinematicPhysicsEntity extends PhysicsEntity {
    public Matrix4 transform;

    //Base class, defines a physics entity which drives a parent entity
    public KinematicPhysicsEntity(GameObject3d parent) {
        this(parent, createConvexHullShape(parent.getModel().model, true));
        //This will error of the parent doesn't have a model; you must use the other constructor in that case.
    }

    public KinematicPhysicsEntity(GameObject3d parent, btCollisionShape collisionShape){
        super(parent);

        Vector3 localInertia = new Vector3();
        collisionShape.calculateLocalInertia(0f,localInertia);
        btRigidBody.btRigidBodyConstructionInfo bodyInfo = new btRigidBody.btRigidBodyConstructionInfo(0f,null,collisionShape,localInertia);
        body = new btRigidBody(bodyInfo);

        body.setCollisionFlags(body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_KINEMATIC_OBJECT);
        body.setActivationState(Collision.DISABLE_DEACTIVATION);
    }



    //Updates state of the kinematic body to match the state of the parent gameObject
    public void updateFromParent(){
        transform = new Matrix4(new Vector3(parent.getX(),parent.getY(),parent.getZ()),new Quaternion().setEulerAngles(parent.getRX(),parent.getRY(),parent.getRZ()),new Vector3(1,1,1));

        body.proceedToTransform(transform);
        body.setActivationState(Collision.ACTIVE_TAG);
    }

    public void updateFromTransform(Matrix4 transformInput){
        body.proceedToTransform(transformInput);
        body.setActivationState(Collision.ACTIVE_TAG);
    }

    public static btConvexHullShape createConvexHullShape (final Model model, boolean optimize) {
        final Mesh mesh = model.meshes.get(0);
        final btConvexHullShape shape = new btConvexHullShape(mesh.getVerticesBuffer(), mesh.getNumVertices(), mesh.getVertexSize());
        if (!optimize) return shape;
        // now optimize the shape
        final btShapeHull hull = new btShapeHull(shape);
        hull.buildHull(shape.getMargin());
        final btConvexHullShape result = new btConvexHullShape(hull);
        // delete the temporary shape
        shape.dispose();
        hull.dispose();
        return result;
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