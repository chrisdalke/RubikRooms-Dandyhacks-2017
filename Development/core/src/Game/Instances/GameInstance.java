////////////////////////////////////////////////
// Development
// Chris Dalke
////////////////////////////////////////////////
// Module: GameInstance
////////////////////////////////////////////////

package Game.Instances;

import Engine.Display.Display;
import Engine.Game.Entity.GameObject3d;
import Engine.Game.Entity.SkyBox;
import Engine.Game.Instance.AbstractGameInstance;
import Engine.Input.Input;
import Engine.Networking.Networking;
import Engine.Networking.NetworkingEventListener;
import Engine.Physics.PhysicsWorld;
import Engine.Renderer.FrameBuffer;
import Engine.Renderer.Renderer;
import Engine.Renderer.Textures.TextureLoader;
import Engine.System.Commands.Commands;
import Engine.System.Logging.Logger;
import Game.Entities.BackgroundCube;
import Game.Entities.FirstPersonCharacterController;
import Game.Entities.RoomObject;
import Game.Game;
import Game.Model.LevelDataObject;
import Game.Model.LevelNetworkFile;
import Game.Model.LevelNetworkPacket;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

import java.io.File;
import java.util.concurrent.ThreadLocalRandom;

import static Game.Entities.RoomObject.ROOM_DIAMETER;


////////////////////////////////////////////////
// Notes
////////////////////////////////////////////////

// Blank for now

////////////////////////////////////////////////
// Main Class
////////////////////////////////////////////////

public class GameInstance extends AbstractGameInstance {

    ////////////////////////////////////////////////
    // Variables
    ////////////////////////////////////////////////

    //Loaded level data
    public LevelDataObject level;
    public SkyBox skybox;
    Engine.Renderer.Textures.Texture crosshairTex;
    PhysicsWorld physicsWorld;

    ////////////////////////////////////////////////
    // Level Loading
    ////////////////////////////////////////////////

    FrameBuffer frameBuffer;

    public GameInstance(String filename){
        this(LevelDataObject.load(new File(filename)));
    }

    public GameInstance(LevelDataObject level){
        frameBuffer = new FrameBuffer(320,240);
        this.level = level;
    }

    ////////////////////////////////////////////////
    // Level Initialization
    ////////////////////////////////////////////////

    @Override
    public void init() {
        shadowViewportSize = 200f;
        super.init();

        physicsWorld = new PhysicsWorld(this);
        physicsWorld.setDebug(false);

        level.calculateRoomPositions();

        Networking.serverSetTCPCallback(new GameEventListener());
        Networking.serverSendTCP(new LevelNetworkFile(level.getFilenameShort()));

        for (int x = 0; x < level.getSize(); x++){
            for (int y = 0; y < level.getSize(); y++){
                for (int z = 0; z < level.getSize(); z++){
                    RoomObject roomObject = new RoomObject(level.getRoom(x,y,z));
                    physicsWorld.add(roomObject.getRoomPhysicsObject());
                    addObject(roomObject);
                }
            }
        }

        for (int i = 0; i < 40; i++){
            GameObject3d cubeObj = new BackgroundCube();
            float angle = ThreadLocalRandom.current().nextInt(0,360);
            float dist = ThreadLocalRandom.current().nextInt(150,350);
            float x = (float)Math.cos(Math.toRadians(angle)) * dist;
            float y = ThreadLocalRandom.current().nextInt(-100,100);
            float z = (float)Math.sin(Math.toRadians(angle)) * dist;
            float scale = ThreadLocalRandom.current().nextInt(5,40);
            cubeObj.setScale(scale,scale,scale);
            cubeObj.setPosition(x,y,z);
            addObject(cubeObj);

        }
        /*
        Camera3d fpsCam = new FirstPersonFlightCamera();
        setCamera(fpsCam.getCam());
        addObject(fpsCam);
        */

        Logger.log("Test");

        FirstPersonCharacterController fpsCam = new FirstPersonCharacterController();
        fpsCam.characterController.setCharacterPosition(new Vector3(level.getStartX()*ROOM_DIAMETER,level.getStartY()*ROOM_DIAMETER,level.getStartZ()*ROOM_DIAMETER));
        fpsCam.characterController.addToPhysicsWorld(physicsWorld);
        setCamera(fpsCam.getCam());
        addObject(fpsCam);

        Commands.bind("test", new Runnable() {
            @Override
            public void run() {
                fpsCam.getCam().translate(fpsCam.getX(),100,fpsCam.getZ());
            }
        });

        Commands.bind("reload", new Runnable() {
            @Override
            public void run() {

                Logger.log("Reloading game instance!");
                Game.setGameInstance(new GameInstance(LevelDataObject.load(new File(level.getFilename()))));
                Game.getGameInstance().init();
            }
        });

        Commands.bind("pos", new Runnable() {
            @Override
            public void run() {
                Logger.log(fpsCam.getCam().position.x / ROOM_DIAMETER+","+fpsCam.getCam().position.y / ROOM_DIAMETER+","+fpsCam.getCam().position.z / ROOM_DIAMETER);
            }
        });

        Display.hideCursor();

        Texture skyTexTop = TextureLoader.load("Assets/Textures/skybox/skyboxTop.png").getTex();
        Texture skyTexSide = TextureLoader.load("Assets/Textures/skybox/skyboxSide.png").getTex();
        Texture skyTexBottom = TextureLoader.load("Assets/Textures/skybox/skyboxBottom.png").getTex();
        skybox = new SkyBox(skyTexSide,skyTexSide,skyTexTop,skyTexBottom,skyTexSide,skyTexSide);

        crosshairTex = TextureLoader.load("Assets/Textures/crosshair.png");
    }


    private class GameEventListener implements NetworkingEventListener {
        @Override
        public void get(Object msg) {
            if (msg instanceof LevelNetworkPacket){
                LevelNetworkPacket packet = (LevelNetworkPacket)msg;
                level.triggerRotatePlane(LevelDataObject.PLANE.values()[packet.plane],packet.planeId, LevelDataObject.PLANE_ROTATION.values()[packet.planeRotation],packet.delta);
            }
        }
    }
    ////////////////////////////////////////////////
    // Level Update
    ////////////////////////////////////////////////

    @Override
    public void update() {
        super.update();

        //Demo test cases
        if (level.getFilenameShort().equals("demo1.txt")){
            if (Input.getKeyPress(com.badlogic.gdx.Input.Keys.NUM_1)){
                level.triggerRotatePlane(LevelDataObject.PLANE.Z,0, LevelDataObject.PLANE_ROTATION.NINETY,1f);
            }
            if (Input.getKeyPress(com.badlogic.gdx.Input.Keys.NUM_2)){
                level.triggerRotatePlane(LevelDataObject.PLANE.X,2, LevelDataObject.PLANE_ROTATION.ONE_EIGHTY,1f);
            }
        }
        if (level.getFilenameShort().equals("demo2.txt")){
            if (Input.getKeyPress(com.badlogic.gdx.Input.Keys.NUM_1)){
                level.triggerRotatePlane(LevelDataObject.PLANE.X,0, LevelDataObject.PLANE_ROTATION.NINETY,1f);
            }
            if (Input.getKeyPress(com.badlogic.gdx.Input.Keys.NUM_2)){
                level.triggerRotatePlane(LevelDataObject.PLANE.X,1, LevelDataObject.PLANE_ROTATION.NINETY,1f);
            }
        }
        if (level.getFilenameShort().equals("demo3.txt")){
            if (Input.getKeyPress(com.badlogic.gdx.Input.Keys.NUM_1)){
                level.triggerRotatePlane(LevelDataObject.PLANE.X,0, LevelDataObject.PLANE_ROTATION.NINETY,1f);
            }
            if (Input.getKeyPress(com.badlogic.gdx.Input.Keys.NUM_2)){
                level.triggerRotatePlane(LevelDataObject.PLANE.X,1, LevelDataObject.PLANE_ROTATION.ONE_EIGHTY,1f);
            }
        }
        if (level.getFilenameShort().equals("demo4.txt")){
            if (Input.getKeyPress(com.badlogic.gdx.Input.Keys.NUM_1)){
                level.triggerRotatePlane(LevelDataObject.PLANE.X,3, LevelDataObject.PLANE_ROTATION.NINETY,1f);
            }
            if (Input.getKeyPress(com.badlogic.gdx.Input.Keys.NUM_2)){
                level.triggerRotatePlane(LevelDataObject.PLANE.Y,3, LevelDataObject.PLANE_ROTATION.ONE_EIGHTY,1f);
            }
            if (Input.getKeyPress(com.badlogic.gdx.Input.Keys.NUM_3)){
                level.triggerRotatePlane(LevelDataObject.PLANE.Z,3, LevelDataObject.PLANE_ROTATION.ONE_EIGHTY,1f);
            }
        }


        level.update();

        physicsWorld.update();

        /*
        if (Input.getKey(com.badlogic.gdx.Input.Keys.SPACE)){
            Sphere sphere = new Sphere();
            sphere.setPosition(camera.position);
            sphere.setPosition(sphere.getX(),sphere.getY()-2,sphere.getZ());
            DynamicPhysicsEntity sphereEntity = new DynamicPhysicsEntity(sphere);
            physicsWorld.add(sphereEntity);
            addObject(sphere);
            float scale = 50.0f;
            sphereEntity.applyForce(camera.direction.x*scale,camera.direction.y*scale,camera.direction.z*scale);
        }*/
    }

    ////////////////////////////////////////////////
    // Level Rendering
    ////////////////////////////////////////////////

    @Override
    public void render() {
        //frameBuffer.start();
        startWorld();

        //Render skybox under all the other models
        skybox.render(camera);
        //Render models
        renderModels();

        endWorld();
        //frameBuffer.end();

        renderShadows();
        physicsWorld.render();

        Renderer.startUI();
        //PostProcessManager.start();
        //Renderer.draw(frameBuffer.getRegion(),0,0, (float)Display.getWidth(),(float)Display.getHeight());
        //PostProcessManager.end();

        float crosshairSize = 20;
        Renderer.setInvert();
        Renderer.draw(crosshairTex.getRegion(), (float)Display.getWidth()/2,(float)Display.getHeight()/2,crosshairSize,crosshairSize);
        Renderer.resetBlending();

        Renderer.endUI();
    }


    ////////////////////////////////////////////////
    // Level System Commands
    ////////////////////////////////////////////////

    ////////////////////////////////////////////////
    // Level Ending
    ////////////////////////////////////////////////

    @Override
    public void dispose() {
    }

}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////