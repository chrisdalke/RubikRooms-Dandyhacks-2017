////////////////////////////////////////////////
// Development
// Chris Dalke
////////////////////////////////////////////////
// Module: GameInstance
////////////////////////////////////////////////

package Game.Instances;

import Engine.Display.Display;
import Engine.Renderer.CustomDirectionalShadowLight;
import Engine.Renderer.Renderer;
import Engine.Renderer.Text;
import Engine.System.Config.Configuration;
import Game.Camera;
import Game.Cube;
import Game.GameObject;
import Game.LevelDataObject;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;

import java.util.ArrayList;


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

    //Game state variables
    public ArrayList<GameObject> levelObjects;

    //Models, environment, resources
    public CustomDirectionalShadowLight shadowLight;
    public ModelBatch shadowBatch;
    public Environment environment;
    public ModelBatch modelBatch;

    public GameObject cube;
    public Camera camera;

    ////////////////////////////////////////////////
    // Level Loading
    ////////////////////////////////////////////////

    //Load from file
    public GameInstance(String levelFilename){
        level = new LevelDataObject(levelFilename);
    }

    //Load from precreated LevelDataObject
    public GameInstance(LevelDataObject newLevel){
        level = newLevel;
    }

    ////////////////////////////////////////////////
    // Level Initialization
    ////////////////////////////////////////////////

    @Override
    public void init(Configuration config) {

        //Set up 3d environment conditions
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.6f, 0.6f, 0.6f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
        //environment.add(new DirectionalLight().set(0.4f, 0.4f, 0.4f, -1f, -0.8f, -0.2f));
       // environment.add((shadowLight = new CustomDirectionalShadowLight(1024, 1024, 60f, 60f, .1f, 150f))
       //         .set(1f, 1f, 1f, 40.0f, -35f, -35f));
        //environment.shadowMap = shadowLight;
       // shadowBatch = new ModelBatch(new DepthShaderProvider());
        modelBatch = new ModelBatch();

        environment.set(new ColorAttribute(ColorAttribute.Fog, 0.964f,0.674f,0.513f, 1f));
        
        //Set up 3d scene objects
        camera = new Camera();
        cube = new Cube();
        addObject(camera);
        addObject(cube);
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

        /*
        shadowLight.begin(Vector3.Zero, camera.getCam().direction);
        shadowBatch.begin(shadowLight.getCamera());
        shadowBatch.render(collectModels());
        shadowBatch.end();
        shadowLight.end();
        */

        //PostProcessManager.start();
        modelBatch.begin(camera.getCam());
        modelBatch.render(collectModels(), environment);
        modelBatch.end();
        //PostProcessManager.end();

        //Display debug text here
        Renderer.startUI();
        Text.setColor(Color.BLACK);
        Text.draw(10,Display.getHeight() - 10,"Rubik's Room");
        Text.setColor(Color.WHITE);
        Renderer.endUI();
    }


    ////////////////////////////////////////////////
    // Level System Commands
    ////////////////////////////////////////////////

    public void addObject(GameObject object){
        levelObjects.add(object);
    }

    public void removeObject(GameObject object){
        levelObjects.remove(object);
    }

    public ArrayList<ModelInstance> collectModels(){
        ArrayList<ModelInstance> modelInstances = new ArrayList<ModelInstance>();
        for (GameObject curObj : levelObjects){
            modelInstances.add(curObj.getModel());
        }
        return modelInstances;
    }

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