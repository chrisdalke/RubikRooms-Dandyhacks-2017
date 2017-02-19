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
import Engine.Game.Entity.Types.Camera3d;
import Engine.Game.Instance.AbstractGameInstance;
import Engine.Input.Input;
import Engine.Physics.DynamicPhysicsEntity;
import Engine.Physics.PhysicsWorld;
import Engine.Renderer.FrameBuffer;
import Engine.Renderer.Renderer;
import Engine.Renderer.Textures.TextureLoader;
import Engine.System.Commands.Commands;
import Engine.System.Logging.Logger;
import Game.Entities.BackgroundCube;
import Game.Entities.FirstPersonFlightCamera;
import Game.Entities.RoomObject;
import Game.Entities.Sphere;
import Game.Game;
import Game.Model.LevelDataObject;
import com.badlogic.gdx.graphics.Texture;

import java.io.File;
import java.util.concurrent.ThreadLocalRandom;


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

    public GameInstance(){
        this((LevelDataObject)null);
    }

    public GameInstance(String filename){
        this(LevelDataObject.load(new File(filename)));
    }

    public GameInstance(LevelDataObject level){

        frameBuffer = new FrameBuffer(320,240);
        this.level = level;
        if (this.level == null){
            this.level = new LevelDataObject(1);
        }
    }

    ////////////////////////////////////////////////
    // Level Initialization
    ////////////////////////////////////////////////

    @Override
    public void init() {
        super.init();

        physicsWorld = new PhysicsWorld(this);
        physicsWorld.setDebug(false);

        for (int x = 0; x < level.getSize(); x++){
            for (int y = 0; y < level.getSize(); y++){
                for (int z = 0; z < level.getSize(); z++){
                    RoomObject roomObject = new RoomObject(level.getRoom(x,y,z));
                    physicsWorld.add(roomObject.getRoomPhysicsObject());
                    addObject(roomObject);
                }
            }
        }

        for (int i = 0; i < 20; i++){
            GameObject3d cubeObj = new BackgroundCube();
            float angle = ThreadLocalRandom.current().nextInt(0,360);
            float dist = ThreadLocalRandom.current().nextInt(150,300);
            float x = (float)Math.cos(Math.toRadians(angle)) * dist;
            float y = ThreadLocalRandom.current().nextInt(-100,100);
            float z = (float)Math.sin(Math.toRadians(angle)) * dist;
            float scale = ThreadLocalRandom.current().nextInt(5,40);
            cubeObj.setScale(scale,scale,scale);
            cubeObj.setPosition(x,y,z);
            addObject(cubeObj);

        }
        Camera3d fpsCam = new FirstPersonFlightCamera();
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

        Display.hideCursor();

        Texture skyTexTop = TextureLoader.load("Assets/Textures/skybox/skyboxTop.png").getTex();
        Texture skyTexSide = TextureLoader.load("Assets/Textures/skybox/skyboxSide.png").getTex();
        Texture skyTexBottom = TextureLoader.load("Assets/Textures/skybox/skyboxBottom.png").getTex();
        skybox = new SkyBox(skyTexSide,skyTexSide,skyTexTop,skyTexBottom,skyTexSide,skyTexSide);

        crosshairTex = TextureLoader.load("Assets/Textures/crosshair.png");
    }

    ////////////////////////////////////////////////
    // Level Update
    ////////////////////////////////////////////////

    @Override
    public void update() {
        super.update();

        //testAngle = (testAngle + 1f) % 360;
        //level.rotatePlane();
        /*
        float speed = 0.01f;
        if (Input.getKey(com.badlogic.gdx.Input.Keys.G)){
            speed = -1f;
        }*/
        //level.triggerRotatePlane(LevelDataObject.PLANE.Z,level.getSize()-1, LevelDataObject.PLANE_ROTATION.NINETY, 1f);
        level.update();

        physicsWorld.update();

        if (Input.getKey(com.badlogic.gdx.Input.Keys.SPACE)){
            Sphere sphere = new Sphere();
            sphere.setPosition(camera.position);
            sphere.setPosition(sphere.getX(),sphere.getY()-2,sphere.getZ());
            DynamicPhysicsEntity sphereEntity = new DynamicPhysicsEntity(sphere);
            physicsWorld.add(sphereEntity);
            addObject(sphere);
            float scale = 50.0f;
            sphereEntity.applyForce(camera.direction.x*scale,camera.direction.y*scale,camera.direction.z*scale);
        }

        
    }

    ////////////////////////////////////////////////
    // Level Rendering
    ////////////////////////////////////////////////

    float testAngle;

    @Override
    public void render() {
        //Center cursor
        //Gdx.input.setCursorPosition((int)Display.getWidth()/2,(int)Display.getHeight()/2);


        //frameBuffer.start();
        startWorld();

        //Render skybox under all the other models
        skybox.render(camera);

        renderModels();
        endWorld();
        renderShadows();

        physicsWorld.render();

        //frameBuffer.end();
        Renderer.startUI();
        //PostProcessManager.start();
        //Renderer.draw(frameBuffer.getRegion(),0,0, (float)Display.getWidth(),(float)Display.getHeight());
        //PostProcessManager.end();

        /*
        Text.setColor(Color.BLACK);
        Text.draw(100,100,"Test Angle: "+testAngle);
        */


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