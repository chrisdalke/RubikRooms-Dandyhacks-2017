////////////////////////////////////////////////
// Development
// Chris Dalke
////////////////////////////////////////////////
// Module: PhysicsWorld
////////////////////////////////////////////////

package Engine.Physics;

import Engine.System.Logging.Logger;
import Game.Instances.GameInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.dynamics.btConstraintSolver;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;

import java.util.ArrayList;

public class PhysicsWorld {

    public DebugDrawer debugDrawer;
    public final btCollisionConfiguration collisionConfiguration;
    public final btCollisionDispatcher dispatcher;
    public final btBroadphaseInterface broadphase;
    public final btConstraintSolver solver;
    public final btAxisSweep3 sweep;
    public final btGhostPairCallback ghostPairCallback;
    public final btDynamicsWorld collisionWorld;
    public final Vector3 gravity;
    public int maxSubSteps = 5;
    public float fixedTimeStep = 1f / 60f;
    public double lastTime;
    public ArrayList<PhysicsEntity> objects;

    public boolean debug;
    public GameInstance instance;

    public PhysicsWorld(GameInstance instance) {
        Logger.log("Trying to create physics world...");
        this.instance = instance;
        debug = false;

        collisionConfiguration = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisionConfiguration);
        broadphase = new btDbvtBroadphase();
        sweep = new btAxisSweep3(new Vector3(-1000, -1000, -1000), new Vector3(1000, 1000, 1000));
        solver = new btSequentialImpulseConstraintSolver();
        collisionWorld = new btDiscreteDynamicsWorld(dispatcher, sweep, solver, collisionConfiguration);
        ghostPairCallback = new btGhostPairCallback();
        sweep.getOverlappingPairCache().setInternalGhostPairCallback(ghostPairCallback);

        gravity = new Vector3(0,-10,0);
        collisionWorld.setGravity(gravity);
        objects = new ArrayList<>();

        debugDrawer = new DebugDrawer();
        collisionWorld.setDebugDrawer(debugDrawer);
        debugDrawer.setDebugMode(1);
        Logger.log("Created physics world!");
    }

    public void add(PhysicsEntity physicsObject){
        objects.add(physicsObject);
        collisionWorld.addRigidBody(physicsObject.body);
        physicsObject.setWorld(this);
    }

    public void remove(PhysicsEntity physicsObject){
        objects.remove(physicsObject);
        collisionWorld.removeRigidBody(physicsObject.body);
    }

    //TODO convert to smooth timestep based on delta time
    public void update(){
        collisionWorld.stepSimulation(fixedTimeStep*1.5f, maxSubSteps, fixedTimeStep);
    }

    public void render(){
        if (debug){
            debugDrawer.begin(instance.camera);
            collisionWorld.debugDrawWorld();
            debugDrawer.end();
        }

    }

    public void dispose(){

    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////