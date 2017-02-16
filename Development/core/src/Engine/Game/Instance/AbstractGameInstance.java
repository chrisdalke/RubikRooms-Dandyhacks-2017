package Engine.Game.Instance;

import Engine.Display.Display;
import Engine.Game.Entity.GameObject3d;
import Engine.Renderer.PostProcess.CustomDirectionalShadowLight;
import Engine.Renderer.Renderer;
import Engine.Renderer.Text;
import Engine.System.Config.Configuration;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.DepthShaderProvider;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class AbstractGameInstance {
    
    
    //Models, environment, resources
    public CustomDirectionalShadowLight shadowLight;
    public ModelBatch shadowBatch;
    public Environment environment;
    public ModelBatch modelBatch;
    public Camera camera;
    public Camera camera2d;

    public Environment getEnvironment() {
        return environment;
    }

    public void setCamera(Camera cam){
        camera = cam;
    }

    public void init(Configuration config){
        //Initialize list of objects
        levelObjects = new ArrayList<>();
        
        //Initialize 3d batch and environment settings
        //Set up 3d environment conditions
        environment = new Environment();
        //environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.8f, 0.8f, 0.8f, 1f));
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 1f,1f,1f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
        //environment.add(new DirectionalLight().set(0.4f, 0.4f, 0.4f, -1f, -0.8f, -0.2f));


         environment.add((shadowLight = new CustomDirectionalShadowLight(1024, 1024, 60f, 60f, .1f, 150f))
                 .set(1f, 1f, 1f, 40.0f, -35f, -35f));
        environment.shadowMap = shadowLight;
         shadowBatch = new ModelBatch(new DepthShaderProvider());
        modelBatch = new ModelBatch();
    
        //environment.set(new ColorAttribute(ColorAttribute.Fog, 1.0,0.513f, 1f));

    
    }
    
    public void update(){
        clearEvents();
        for (GameObject3d levelObject : levelObjects){
            levelObject.update();
        }
    }
    public abstract void render();
    public abstract void dispose();
    
    
    //Game state variables
    public ArrayList<GameObject3d> levelObjects;

    private HashMap<String,Boolean> events;
    //Sets an event. Will be cleared at the end of the frame
    public void setEvent(String event){
        if (events == null){
            events = new HashMap<String,Boolean>();
        }
        events.put(event,true);
    }

    public void clearEvents(){
        if (events == null){
            events = new HashMap<String,Boolean>();
        }
        for (String curEvent : events.keySet()){
            events.put(curEvent,false);
        }
    }
    //Checks the status of an event
    public boolean checkEvent(String event){
        if (events == null){
            events = new HashMap<String,Boolean>();
        }
        boolean result = false;
        if (events.containsKey(event)){
            result = events.get(event);
        }
        return result;
    }
    
    public void addObject(GameObject3d object){
        levelObjects.add(object);
    }
    
    public void removeObject(GameObject3d object){
        levelObjects.remove(object);
    }
    
    public ArrayList<ModelInstance> collectModels(){
        ArrayList<ModelInstance> modelInstances = new ArrayList<ModelInstance>();
        for (GameObject3d curObj : levelObjects){
            if (curObj.getModel() != null) {
                modelInstances.add(curObj.getModel());
            }
        }
        return modelInstances;
    }
    
    public void startWorld(){
        modelBatch.begin(camera);
        /*
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glDepthFunc(GL20.GL_GREATER);
        */
        renderModels();
    
    }
    
    public void endWorld(){
    
        modelBatch.end();
    }
    
    public void startWorld2d(){
        
    }
    
    public void endWorld2d(){
        
    }
    
    public void drawDebugUI(){
    
        //Display debug text here
        Renderer.startUI();
        Text.setColor(Color.BLACK);
        Text.draw(10, Display.getHeight() - 10,"Rubik's Room");
        Text.setColor(Color.WHITE);
        Renderer.endUI();
    }
    
    public void renderModels(){
    
        //PostProcessManager.start();
        modelBatch.render(collectModels(), environment);
        
        //PostProcessManager.end();
    
    }
    
    public void renderShadows(){
        

        shadowLight.begin(camera.position, camera.direction);
        shadowBatch.begin(shadowLight.getCamera());
        shadowBatch.render(collectModels());
        shadowBatch.end();
        shadowLight.end();
    
    }
}
