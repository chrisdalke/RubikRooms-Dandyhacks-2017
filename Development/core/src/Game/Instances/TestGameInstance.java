////////////////////////////////////////////////
// Development
// Chris Dalke
////////////////////////////////////////////////
// Module: TestGameInstance
////////////////////////////////////////////////

package Game.Instances;

import Engine.Input.Input;
import Engine.Renderer.Models.ModelLoader;
import Engine.Renderer.Models.ModelTexturer;
import Engine.Renderer.Textures.TextureLoader;
import Engine.System.Config.Configuration;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalShadowLight;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;

import java.util.ArrayList;

public class TestGameInstance extends AbstractGameInstance {


    public Environment environment;
    public PerspectiveCamera cam;
    public FirstPersonCameraController camController;
    public ModelBatch modelBatch;
    public ModelInstance floorInstance;
    public ArrayList<ModelInstance> instances;
    public ModelInstance[][] cubeInstances;

    public DirectionalShadowLight shadowLight;
    public ModelBatch shadowBatch;

    @Override
    public void init(Configuration config) {
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.6f, 0.6f, 0.6f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        modelBatch = new ModelBatch();

        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(10f, 10f, 10f);
        cam.lookAt(0,0,0);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();

        instances = new ArrayList<ModelInstance>();

        cubeInstances = new ModelInstance[10][10];
        for (int x = 0; x < 10; x++){
            for (int y = 0; y < 10; y++){
                cubeInstances[x][y] = new ModelInstance(ModelLoader.load("Resources/Models/cube.obj"));

                cubeInstances[x][y].transform.rotate(1.0f, 0.5f, 1.0f, (float) Math.random() * 360.0f);
                cubeInstances[x][y].transform.translate(x*3,20,y*3);
                instances.add(cubeInstances[x][y]);

                ModelTexturer.texture(cubeInstances[x][y], TextureLoader.load("Resources/Textures/debug.png"));
            }
        }

        floorInstance = new ModelInstance(ModelLoader.load("Resources/Models/grid.obj"));
        floorInstance.transform.scale(100,100,100);
        instances.add(floorInstance);
        ModelTexturer.texture(floorInstance,TextureLoader.load("Resources/Textures/debug.png"));

        camController = new FirstPersonCameraController(cam);
        Input.addInputHandler(camController);
        camController.setVelocity(100.0f);

        /*
        environment.add((shadowLight = new DirectionalShadowLight(1024, 1024, 60f, 60f, .1f, 50f))
                .set(1f, 1f, 1f, 40.0f, -35f, -35f));
        environment.shadowMap = shadowLight;

        shadowBatch = new ModelBatch(new DepthShaderProvider());
        */
    }

    @Override
    public void update() {
        camController.update();


        for (int x = 0; x < 10; x++){
            for (int y = 0; y < 10; y++){
                cubeInstances[x][y].transform.rotate(1.0f, 1.0f,1.0f, (1.0f+x-y)*0.1f);
            }
        }
    }

    @Override
    public void render() {
        // PostProcessManager.start();

        //Render Game stuff here
        //None



/*
        shadowLight.begin(Vector3.Zero, cam.direction);
        shadowBatch.begin(shadowLight.getCamera());
        shadowBatch.render(instances);
        shadowBatch.end();
        shadowLight.end();*/


        modelBatch.begin(cam);
        modelBatch.render(instances, environment);

        modelBatch.end();

        // PostProcessManager.end();





    }

    @Override
    public void dispose() {

        //Dispose of any extra game resources that we won't need.
        modelBatch.dispose();

        Input.removeInputHandler(camController);
    }
}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////