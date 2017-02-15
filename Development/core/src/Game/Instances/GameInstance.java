////////////////////////////////////////////////
// Development
// Chris Dalke
////////////////////////////////////////////////
// Module: GameInstance
////////////////////////////////////////////////

package Game.Instances;

import Engine.Game.Entity.GameObject3d;
import Engine.Game.Entity.Types.Camera3d;
import Engine.Game.Instance.AbstractGameInstance;
import Engine.Renderer.FrameBuffer;
import Engine.Renderer.Renderer;
import Engine.System.Config.Configuration;
import Game.Cube;
import Game.Entities.FirstPersonFlightCamera;
import Game.LevelDataObject;
import Game.OutlineCube;

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
                    OutlineCube outlineCube = new OutlineCube();
                    outlineCube.setPosition(x*10,y*10,z*10);
                    addObject(outlineCube);

                }
            }
        }

        for (int i = 0; i < 100; i++){
            GameObject3d cubeObj = new Cube();
            cubeObj.setPosition(ThreadLocalRandom.current().nextInt(-50,50),ThreadLocalRandom.current().nextInt(-50,50),ThreadLocalRandom.current().nextInt(-50,50));
            addObject(cubeObj);

        }
        Camera3d fpsCam = new FirstPersonFlightCamera();
        setCamera(fpsCam.getCam());
        addObject(fpsCam);

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
        //frameBuffer.start();
        startWorld();
        renderModels();
        endWorld();
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