////////////////////////////////////////////////
// Development
// Chris Dalke
////////////////////////////////////////////////
// Module: PhysicsEntity
////////////////////////////////////////////////

package Engine.Physics;

import Engine.Game.Entity.GameObject3d;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;

public class PhysicsEntity {

    public btRigidBody body;
    public GameObject3d parent;
    public PhysicsWorld world;

    public PhysicsWorld getWorld() {
        return world;
    }

    public void setWorld(PhysicsWorld world) {
        this.world = world;
    }

    public PhysicsEntity(GameObject3d parent) {
        this.parent = parent;
    }

}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////