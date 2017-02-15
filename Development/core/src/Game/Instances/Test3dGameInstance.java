////////////////////////////////////////////////
// Development
// Chris Dalke
////////////////////////////////////////////////
// Module: TestGameInstance
////////////////////////////////////////////////

package Game.Instances;

import Engine.Game.Instance.AbstractGameInstance;
import Engine.System.Config.Configuration;
import Game.Entities.FirstPersonFlightCamera;
import Game.OutlineCube;

public class Test3dGameInstance extends AbstractGameInstance {
    
    public FirstPersonFlightCamera fpsCam;

    @Override
    public void init(Configuration config) {
        super.init(config);
        
        OutlineCube outlineCube = new OutlineCube();
        addObject(outlineCube);

        fpsCam = new FirstPersonFlightCamera();
        setCamera(fpsCam.getCam());
        addObject(fpsCam);
    }

    @Override
    public void update() {
        
    }

    @Override
    public void render() {
        
        startWorld();
        renderModels();
        endWorld();
        
        //Renderer.startUI();
    
        //PostProcessManager.start();
        //Renderer.draw(Renderer.getFrameBufferTexture(),0,0, (float)Display.getWidth(),(float)Display.getHeight());
        //PostProcessManager.end();
    
        //Renderer.endUI();

    }

    @Override
    public void dispose() {

        //Dispose of any extra game resources that we won't need.
        modelBatch.dispose();

    }
}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////