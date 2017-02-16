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
import Engine.Renderer.FrameBuffer;
import Engine.Renderer.Renderer;
import Engine.Renderer.Textures.TextureLoader;
import Engine.System.Config.Configuration;
import Game.BackgroundCube;
import Game.Entities.FirstPersonFlightCamera;
import Game.LevelDataObject;
import Game.RoomObject;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;

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

    ////////////////////////////////////////////////
    // Level Loading
    ////////////////////////////////////////////////

    /*
    //Load from file
    public GameInstance(String levelFilename){
        level = new LevelDataObject(levelFilename);
    }

    //Load from precreated LevelDataObject
    public GameInstance(LevelDataObject newLevel){
        level = newLevel;
    }
    */

    FrameBuffer frameBuffer;

    public GameInstance(){
        frameBuffer = new FrameBuffer(320,240);
    }

    ////////////////////////////////////////////////
    // Level Initialization
    ////////////////////////////////////////////////

    @Override
    public void init(Configuration config) {
        super.init(config);

        for (int x = 0; x < 3; x++){
            for (int y = 0; y < 3; y++){
                for (int z = 0; z < 3; z++){
                    //OutlineCube outlineCube = new OutlineCube();
                    //outlineCube.setPosition(x*10,y*10,z*10);
                    RoomObject room = new RoomObject();
                    room.setPosition(x*20,y*20,z*20);
                    //addObject(outlineCube);
                    addObject(room);

                    PointLight light = new PointLight();
                    light.setPosition(x*20,y*20+10,z*20);
                    light.setIntensity(100.0f);

                    getEnvironment().add(light);

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

        Display.hideCursor();

        Texture skyTexTop = TextureLoader.load("Assets/Textures/skybox/skyboxTop.png").getTex();
        Texture skyTexSide = TextureLoader.load("Assets/Textures/skybox/skyboxSide.png").getTex();
        Texture skyTexBottom = TextureLoader.load("Assets/Textures/skybox/skyboxBottom.png").getTex();
        skybox = new SkyBox(skyTexSide,skyTexSide,skyTexTop,skyTexBottom,skyTexSide,skyTexSide);

    }

    ////////////////////////////////////////////////
    // Level Update
    ////////////////////////////////////////////////

    @Override
    public void update() {
        super.update();


        
    }

    ////////////////////////////////////////////////
    // Level Rendering
    ////////////////////////////////////////////////

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
        //renderShadows();

        //frameBuffer.end();
        Renderer.startUI();
        //PostProcessManager.start();
        //Renderer.draw(frameBuffer.getRegion(),0,0, (float)Display.getWidth(),(float)Display.getHeight());
        //PostProcessManager.end();
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